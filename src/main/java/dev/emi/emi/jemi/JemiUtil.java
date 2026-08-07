package dev.emi.emi.jemi;

import java.util.List;

import com.google.common.collect.Lists;

import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import mezz.jei.api.ingredients.VanillaTypes;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public final class JemiUtil {
	private JemiUtil() {
	}

	public static EmiStack of(ItemStack stack) {
		if (stack == null || stack.isEmpty()) {
			return EmiStack.EMPTY;
		}
		return EmiStack.of(stack);
	}

	public static EmiStack of(FluidStack stack) {
		if (stack == null || stack.amount <= 0 || stack.getFluid() == null) {
			return EmiStack.EMPTY;
		}
		return EmiStack.of(stack.getFluid(), stack.tag, stack.amount);
	}

	public static EmiIngredient ingredient(List<ItemStack> stacks) {
		if (stacks.isEmpty()) {
			return EmiStack.EMPTY;
		}
		List<EmiStack> emi = Lists.newArrayList();
		for (ItemStack stack : stacks) {
			EmiStack converted = of(stack);
			if (!converted.isEmpty()) {
				emi.add(converted);
			}
		}
		return EmiIngredient.of(emi);
	}

	public static EmiIngredient ingredientFromNested(List<List<ItemStack>> nested) {
		List<EmiStack> emi = Lists.newArrayList();
		for (List<ItemStack> group : nested) {
			for (ItemStack stack : group) {
				EmiStack converted = of(stack);
				if (!converted.isEmpty()) {
					emi.add(converted);
				}
			}
		}
		return EmiIngredient.of(emi);
	}

	public static EmiIngredient fluidIngredient(List<List<FluidStack>> nested) {
		List<EmiStack> emi = Lists.newArrayList();
		for (List<FluidStack> group : nested) {
			for (FluidStack stack : group) {
				EmiStack converted = of(stack);
				if (!converted.isEmpty()) {
					emi.add(converted);
				}
			}
		}
		return EmiIngredient.of(emi);
	}

	public static String namespaceFromUid(String uid) {
		int i = uid.indexOf(':');
		if (i >= 0) {
			return uid.substring(0, i);
		}
		return uid;
	}

	public static boolean isVanillaCategory(String uid) {
		return switch (uid) {
			case mezz.jei.api.recipe.VanillaRecipeCategoryUid.CRAFTING,
				 mezz.jei.api.recipe.VanillaRecipeCategoryUid.SMELTING,
				 mezz.jei.api.recipe.VanillaRecipeCategoryUid.FUEL,
				 mezz.jei.api.recipe.VanillaRecipeCategoryUid.BREWING,
				 mezz.jei.api.recipe.VanillaRecipeCategoryUid.ANVIL,
				 mezz.jei.api.recipe.VanillaRecipeCategoryUid.INFORMATION,
				 mezz.jei.api.recipe.VanillaRecipeCategoryUid.DESCRIPTION -> true;
			default -> false;
		};
	}
}
