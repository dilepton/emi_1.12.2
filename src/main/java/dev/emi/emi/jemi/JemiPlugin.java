package dev.emi.emi.jemi;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import dev.emi.emi.EmiPort;
import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.platform.EmiAgnos;
import dev.emi.emi.registry.EmiPluginContainer;
import dev.emi.emi.registry.EmiRecipes;
import dev.emi.emi.runtime.EmiLog;
import dev.emi.emi.runtime.EmiReloadManager;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.gui.IRecipeLayoutDrawable;
import mezz.jei.api.ingredients.IModIngredientRegistration;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.ISubtypeRegistry;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.recipe.IFocus;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

@JEIPlugin
@EmiEntrypoint
public class JemiPlugin implements IModPlugin, EmiPlugin {
	public static IJeiRuntime runtime;

	@Override
	public void registerItemSubtypes(ISubtypeRegistry registry) {
	}

	@Override
	public void registerIngredients(IModIngredientRegistration registration) {
	}

	@Override
	public void registerCategories(IRecipeCategoryRegistration registry) {
	}

	@Override
	public void register(IModRegistry registry) {
	}

	@Override
	public void onRuntimeAvailable(IJeiRuntime runtime) {
		JemiPlugin.runtime = runtime;
		EmiLog.info("[JEMI] JEI runtime available");
		JeiOverlayHider.apply();
	}

	@Override
	@SuppressWarnings({"rawtypes", "unchecked"})
	public void register(EmiRegistry registry) {
		if (!EmiAgnos.isModLoaded("jei")) {
			EmiLog.info("[JEMI] JEI not loaded, skipping bridge");
			return;
		}

		EmiLog.info("[JEMI] Waiting for JEI runtime...");
		EmiReloadManager.step(EmiPort.literal("Waiting for JEI..."), 20_000);
		try {
			for (int i = 0; i < 200 && runtime == null; i++) {
				Thread.sleep(100);
			}
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			return;
		}
		if (runtime == null) {
			EmiLog.error("[JEMI] Timed out waiting for JEI runtime");
			return;
		}

		EmiLog.info("[JEMI] Importing JEI recipes...");
		EmiReloadManager.step(EmiPort.literal("Loading recipes from JEI..."), 10_000);

		Set<String> handledNamespaces = EmiAgnos.getPlugins().stream()
				.map(EmiPluginContainer::id)
				.collect(Collectors.toSet());
		Set<ResourceLocation> existingCategories = EmiRecipes.categories.stream()
				.map(EmiRecipeCategory::getId)
				.collect(Collectors.toSet());

		Set<String> seenCategoryUids = new HashSet<>();
		int recipesAdded = 0;

		for (IRecipeCategory category : runtime.getRecipeRegistry().getRecipeCategories()) {
			String uid = category.getUid();
			if (JemiUtil.isVanillaCategory(uid)) {
				continue;
			}
			String namespace = JemiUtil.namespaceFromUid(uid);
			if (handledNamespaces.contains(namespace)) {
				continue;
			}
			ResourceLocation emiCategoryId = EmiPort.id("jei", "/" + uid.replace(':', '/'));
			if (existingCategories.contains(emiCategoryId)) {
				continue;
			}
			if (!seenCategoryUids.add(uid)) {
				continue;
			}

			try {
				EmiRecipeCategory emiCategory = new JemiCategory(category);
				registry.addCategory(emiCategory);
				EmiReloadManager.step(EmiPort.literal("JEI: " + category.getTitle()), 5_000);

				for (Object catalyst : runtime.getRecipeRegistry().getRecipeCatalysts(category)) {
					EmiStack stack = catalyst instanceof ItemStack item ? JemiUtil.of(item) : EmiStack.EMPTY;
					if (!stack.isEmpty()) {
						registry.addWorkstation(emiCategory, stack);
					}
				}

				List<IRecipeWrapper> wrappers = runtime.getRecipeRegistry().getRecipeWrappers(category);
				for (IRecipeWrapper wrapper : wrappers) {
					try {
						IFocus<?> focus = createFocus(wrapper);
						IRecipeLayoutDrawable layout = runtime.getRecipeRegistry().createRecipeLayoutDrawable(category, wrapper, focus);
						registry.addRecipe(new JemiRecipe(emiCategory, category, wrapper, layout));
						recipesAdded++;
					} catch (Throwable t) {
						try {
							registry.addRecipe(new JemiRecipe(emiCategory, category, wrapper, null));
							recipesAdded++;
						} catch (Throwable t2) {
							EmiLog.error("[JEMI] Failed importing recipe from " + uid, t2);
						}
					}
				}
			} catch (Throwable t) {
				EmiLog.error("[JEMI] Failed importing category " + uid, t);
			}
		}

		EmiLog.info("[JEMI] Imported " + recipesAdded + " recipes from JEI");
		JeiOverlayHider.apply();
	}

	private static IFocus<?> createFocus(IRecipeWrapper wrapper) {
		JemiCaptureIngredients capture = new JemiCaptureIngredients();
		wrapper.getIngredients(capture);
		var registry = runtime.getRecipeRegistry();
		List<ItemStack> outputs = capture.getFlatItemOutputs();
		if (!outputs.isEmpty()) {
			return registry.createFocus(IFocus.Mode.OUTPUT, outputs.get(0));
		}
		List<ItemStack> inputs = capture.getFlatItemInputs();
		if (!inputs.isEmpty()) {
			return registry.createFocus(IFocus.Mode.INPUT, inputs.get(0));
		}
		return registry.createFocus(IFocus.Mode.OUTPUT, ItemStack.EMPTY);
	}
}
