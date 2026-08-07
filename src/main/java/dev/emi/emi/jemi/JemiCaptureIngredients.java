package dev.emi.emi.jemi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IIngredientType;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class JemiCaptureIngredients implements IIngredients {
	private final Map<IIngredientType<?>, List<List<?>>> inputs = new HashMap<>();
	private final Map<IIngredientType<?>, List<List<?>>> outputs = new HashMap<>();

	@SuppressWarnings("unchecked")
	private <T> List<List<T>> getMap(IIngredientType<T> type, Map<IIngredientType<?>, List<List<?>>> map) {
		return (List<List<T>>) (List<?>) map.computeIfAbsent(type, k -> new ArrayList<>());
	}

	@Override
	public <T> void setInput(IIngredientType<T> ingredientType, T ingredient) {
		getMap(ingredientType, inputs).add(Lists.newArrayList(ingredient));
	}

	@Override
	public <T> void setInputs(IIngredientType<T> ingredientType, List<T> ingredients) {
		getMap(ingredientType, inputs).add(new ArrayList<>(ingredients));
	}

	@Override
	public <T> void setInputLists(IIngredientType<T> ingredientType, List<List<T>> ingredients) {
		getMap(ingredientType, inputs).addAll(ingredients);
	}

	@Override
	public <T> void setOutput(IIngredientType<T> ingredientType, T ingredient) {
		getMap(ingredientType, outputs).add(Lists.newArrayList(ingredient));
	}

	@Override
	public <T> void setOutputs(IIngredientType<T> ingredientType, List<T> ingredients) {
		getMap(ingredientType, outputs).add(new ArrayList<>(ingredients));
	}

	@Override
	public <T> void setOutputLists(IIngredientType<T> ingredientType, List<List<T>> ingredients) {
		getMap(ingredientType, outputs).addAll(ingredients);
	}

	@Override
	public <T> List<List<T>> getInputs(IIngredientType<T> ingredientType) {
		return getMap(ingredientType, inputs);
	}

	@Override
	public <T> List<List<T>> getOutputs(IIngredientType<T> ingredientType) {
		return getMap(ingredientType, outputs);
	}

	@Override
	public <T> void setInput(Class<? extends T> ingredientClass, T ingredient) {
		if (ItemStack.class.isAssignableFrom(ingredientClass)) {
			setInput(VanillaTypes.ITEM, (ItemStack) ingredient);
		} else if (FluidStack.class.isAssignableFrom(ingredientClass)) {
			setInput(VanillaTypes.FLUID, (FluidStack) ingredient);
		}
	}

	@Override
	public <T> void setInputs(Class<? extends T> ingredientClass, List<T> ingredients) {
		if (ItemStack.class.isAssignableFrom(ingredientClass)) {
			@SuppressWarnings("unchecked")
			List<ItemStack> stacks = (List<ItemStack>) (List<?>) ingredients;
			setInputs(VanillaTypes.ITEM, stacks);
		} else if (FluidStack.class.isAssignableFrom(ingredientClass)) {
			@SuppressWarnings("unchecked")
			List<FluidStack> stacks = (List<FluidStack>) (List<?>) ingredients;
			setInputs(VanillaTypes.FLUID, stacks);
		}
	}

	@Override
	public <T> void setInputLists(Class<? extends T> ingredientClass, List<List<T>> ingredients) {
		if (ItemStack.class.isAssignableFrom(ingredientClass)) {
			@SuppressWarnings("unchecked")
			List<List<ItemStack>> stacks = (List<List<ItemStack>>) (List<?>) ingredients;
			setInputLists(VanillaTypes.ITEM, stacks);
		} else if (FluidStack.class.isAssignableFrom(ingredientClass)) {
			@SuppressWarnings("unchecked")
			List<List<FluidStack>> stacks = (List<List<FluidStack>>) (List<?>) ingredients;
			setInputLists(VanillaTypes.FLUID, stacks);
		}
	}

	@Override
	public <T> void setOutput(Class<? extends T> ingredientClass, T ingredient) {
		if (ItemStack.class.isAssignableFrom(ingredientClass)) {
			setOutput(VanillaTypes.ITEM, (ItemStack) ingredient);
		} else if (FluidStack.class.isAssignableFrom(ingredientClass)) {
			setOutput(VanillaTypes.FLUID, (FluidStack) ingredient);
		}
	}

	@Override
	public <T> void setOutputs(Class<? extends T> ingredientClass, List<T> ingredients) {
		if (ItemStack.class.isAssignableFrom(ingredientClass)) {
			@SuppressWarnings("unchecked")
			List<ItemStack> stacks = (List<ItemStack>) (List<?>) ingredients;
			setOutputs(VanillaTypes.ITEM, stacks);
		} else if (FluidStack.class.isAssignableFrom(ingredientClass)) {
			@SuppressWarnings("unchecked")
			List<FluidStack> stacks = (List<FluidStack>) (List<?>) ingredients;
			setOutputs(VanillaTypes.FLUID, stacks);
		}
	}

	@Override
	public <T> void setOutputLists(Class<? extends T> ingredientClass, List<List<T>> ingredients) {
		if (ItemStack.class.isAssignableFrom(ingredientClass)) {
			@SuppressWarnings("unchecked")
			List<List<ItemStack>> stacks = (List<List<ItemStack>>) (List<?>) ingredients;
			setOutputLists(VanillaTypes.ITEM, stacks);
		} else if (FluidStack.class.isAssignableFrom(ingredientClass)) {
			@SuppressWarnings("unchecked")
			List<List<FluidStack>> stacks = (List<List<FluidStack>>) (List<?>) ingredients;
			setOutputLists(VanillaTypes.FLUID, stacks);
		}
	}

	@Override
	public <T> List<List<T>> getInputs(Class<? extends T> ingredientClass) {
		if (ItemStack.class.isAssignableFrom(ingredientClass)) {
			@SuppressWarnings("unchecked")
			List<List<T>> cast = (List<List<T>>) (List<?>) getInputs(VanillaTypes.ITEM);
			return cast;
		}
		if (FluidStack.class.isAssignableFrom(ingredientClass)) {
			@SuppressWarnings("unchecked")
			List<List<T>> cast = (List<List<T>>) (List<?>) getInputs(VanillaTypes.FLUID);
			return cast;
		}
		return Lists.newArrayList();
	}

	@Override
	public <T> List<List<T>> getOutputs(Class<? extends T> ingredientClass) {
		if (ItemStack.class.isAssignableFrom(ingredientClass)) {
			@SuppressWarnings("unchecked")
			List<List<T>> cast = (List<List<T>>) (List<?>) getOutputs(VanillaTypes.ITEM);
			return cast;
		}
		if (FluidStack.class.isAssignableFrom(ingredientClass)) {
			@SuppressWarnings("unchecked")
			List<List<T>> cast = (List<List<T>>) (List<?>) getOutputs(VanillaTypes.FLUID);
			return cast;
		}
		return Lists.newArrayList();
	}

	public List<ItemStack> getFlatItemInputs() {
		return flatten(getInputs(VanillaTypes.ITEM));
	}

	public List<ItemStack> getFlatItemOutputs() {
		return flatten(getOutputs(VanillaTypes.ITEM));
	}

	public List<FluidStack> getFlatFluidInputs() {
		return flatten(getInputs(VanillaTypes.FLUID));
	}

	public List<FluidStack> getFlatFluidOutputs() {
		return flatten(getOutputs(VanillaTypes.FLUID));
	}

	private static <T> List<T> flatten(List<List<T>> nested) {
		List<T> flat = Lists.newArrayList();
		for (List<T> group : nested) {
			for (T t : group) {
				if (t != null && !isEmpty(t)) {
					flat.add(t);
				}
			}
		}
		return flat;
	}

	private static <T> boolean isEmpty(T value) {
		if (value instanceof ItemStack stack) {
			return stack.isEmpty();
		}
		if (value instanceof FluidStack fluid) {
			return fluid.amount <= 0 || fluid.getFluid() == null;
		}
		return value == null;
	}
}
