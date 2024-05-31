package earth.terrarium.common_storage_lib.resources.fluid.ingredient.impl;

import com.mojang.serialization.MapCodec;
import earth.terrarium.common_storage_lib.resources.ResourceLib;
import earth.terrarium.common_storage_lib.resources.fluid.FluidResource;
import earth.terrarium.common_storage_lib.resources.fluid.ingredient.FluidIngredient;
import earth.terrarium.common_storage_lib.resources.fluid.ingredient.FluidIngredientType;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public record AnyMatchFluidIngredient(List<FluidIngredient> children) implements ListFluidIngredient {
    public static final MapCodec<AnyMatchFluidIngredient> CODEC = FluidIngredient.CODEC.listOf().fieldOf("children").xmap(AnyMatchFluidIngredient::new, AnyMatchFluidIngredient::children);
    public static final FluidIngredientType<AnyMatchFluidIngredient> TYPE = new FluidIngredientType<>(new ResourceLocation(ResourceLib.MOD_ID, "any_match"), CODEC);

    @Override
    public boolean test(FluidResource fluidResource) {
        for (FluidIngredient fluidIngredient : children) {
            if (fluidIngredient.test(fluidResource)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<FluidResource> getMatchingFluids() {
        return children.stream()
                .flatMap(child -> child.getMatchingFluids().stream())
                .filter(this::test)
                .toList();
    }

    @Override
    public FluidIngredientType<?> getType() {
        return TYPE;
    }
}
