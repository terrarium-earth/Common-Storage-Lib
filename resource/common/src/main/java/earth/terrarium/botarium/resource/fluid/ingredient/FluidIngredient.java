package earth.terrarium.botarium.resource.fluid.ingredient;

import earth.terrarium.botarium.resource.fluid.FluidResource;

import java.util.List;

public interface FluidIngredient {
    boolean test(FluidResource fluidResource);

    List<FluidResource> getMatchingFluids();

    boolean requiresTesting();

    FluidIngredientType<?> getType();
}
