package earth.terrarium.botarium.resources.fluid.ingredient.impl;

import earth.terrarium.botarium.resources.fluid.ingredient.FluidIngredient;

import java.util.List;

public interface ListFluidIngredient extends FluidIngredient {
    List<FluidIngredient> children();

    @Override
    default boolean requiresTesting() {
        for (FluidIngredient ingredient : children()) {
            if (ingredient.requiresTesting()) {
                return true;
            }
        }
        return false;
    }
}
