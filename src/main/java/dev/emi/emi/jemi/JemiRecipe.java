package dev.emi.emi.jemi;

import java.util.List;

import com.google.common.collect.Lists;

import dev.emi.emi.EmiPort;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.Bounds;
import dev.emi.emi.api.widget.SlotWidget;
import dev.emi.emi.api.widget.Widget;
import dev.emi.emi.api.widget.WidgetHolder;
import dev.emi.emi.runtime.EmiDrawContext;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IRecipeLayoutDrawable;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import shim.net.minecraft.client.gui.DrawContext;

public class JemiRecipe implements EmiRecipe {
	public final EmiRecipeCategory recipeCategory;
	public final IRecipeCategory<IRecipeWrapper> category;
	public final IRecipeWrapper recipe;
	public final ResourceLocation id;
	public final List<EmiIngredient> inputs;
	public final List<EmiStack> outputs;
	@Nullable
	private final IRecipeLayoutDrawable layout;

	@SuppressWarnings("unchecked")
	public JemiRecipe(EmiRecipeCategory recipeCategory, IRecipeCategory<?> category, IRecipeWrapper recipe, @Nullable IRecipeLayoutDrawable layout) {
		this.recipeCategory = recipeCategory;
		this.category = (IRecipeCategory<IRecipeWrapper>) category;
		this.recipe = recipe;
		this.layout = layout;
		this.id = EmiPort.id("jei", "/" + JemiUtil.namespaceFromUid(this.category.getUid()) + "/recipe/" + Integer.toHexString(System.identityHashCode(recipe)));

		JemiCaptureIngredients ingredients = new JemiCaptureIngredients();
		recipe.getIngredients(ingredients);

		this.inputs = Lists.newArrayList();
		this.outputs = Lists.newArrayList();

		if (!ingredients.getFlatItemInputs().isEmpty()) {
			inputs.add(JemiUtil.ingredient(ingredients.getFlatItemInputs()));
		}
		if (!ingredients.getFlatFluidInputs().isEmpty()) {
			inputs.add(JemiUtil.fluidIngredient(ingredients.getInputs(mezz.jei.api.ingredients.VanillaTypes.FLUID)));
		}
		for (EmiStack stack : JemiUtil.ingredient(ingredients.getFlatItemOutputs()).getEmiStacks()) {
			outputs.add(stack);
		}
		for (EmiStack stack : JemiUtil.fluidIngredient(ingredients.getOutputs(mezz.jei.api.ingredients.VanillaTypes.FLUID)).getEmiStacks()) {
			outputs.add(stack);
		}
	}

	@Override
	public EmiRecipeCategory getCategory() {
		return recipeCategory;
	}

	@Override
	public @Nullable ResourceLocation getId() {
		return id;
	}

	@Override
	public List<EmiIngredient> getInputs() {
		return inputs;
	}

	@Override
	public List<EmiStack> getOutputs() {
		return outputs;
	}

	@Override
	public int getDisplayWidth() {
		IDrawable background = category.getBackground();
		return background != null ? background.getWidth() : 160;
	}

	@Override
	public int getDisplayHeight() {
		IDrawable background = category.getBackground();
		return background != null ? background.getHeight() : 80;
	}

	@Override
	public void addWidgets(WidgetHolder widgets) {
		if (layout != null) {
			widgets.add(new JemiLayoutWidget(0, 0, getDisplayWidth(), getDisplayHeight(), layout));
			return;
		}
		int x = 8;
		int y = 8;
		for (EmiIngredient input : inputs) {
			widgets.add(new SlotWidget(input, x, y));
			x += 18;
		}
		x += 8;
		for (EmiStack output : outputs) {
			widgets.add(new SlotWidget(output, x, y));
			x += 18;
		}
	}

	private static class JemiLayoutWidget extends Widget {
		private final Bounds bounds;
		private final IRecipeLayoutDrawable layout;

		private JemiLayoutWidget(int x, int y, int w, int h, IRecipeLayoutDrawable layout) {
			this.bounds = new Bounds(x, y, w, h);
			this.layout = layout;
		}

		@Override
		public Bounds getBounds() {
			return bounds;
		}

		@Override
		public void render(DrawContext draw, int mouseX, int mouseY, float delta) {
			EmiDrawContext context = EmiDrawContext.wrap(draw);
			context.push();
			context.matrices().translate(bounds.x(), bounds.y(), 0);
			layout.draw(Minecraft.getMinecraft(), mouseX - bounds.x(), mouseY - bounds.y());
			context.pop();
		}
	}
}
