package earth.terrarium.common_storage_lib.resources.fluid.ingredient.impl;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import earth.terrarium.common_storage_lib.resources.ResourceLib;
import earth.terrarium.common_storage_lib.resources.fluid.FluidResource;
import earth.terrarium.common_storage_lib.resources.fluid.ingredient.FluidIngredient;
import earth.terrarium.common_storage_lib.resources.fluid.ingredient.FluidIngredientType;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public record DifferenceFluidIngredient(FluidIngredient minuend, FluidIngredient subtrahend) implements FluidIngredient {
    public static final MapCodec<DifferenceFluidIngredient> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            FluidIngredient.CODEC.fieldOf("minuend").forGetter(DifferenceFluidIngredient::minuend),
            FluidIngredient.CODEC.fieldOf("subtrahend").forGetter(DifferenceFluidIngredient::subtrahend)
    ).apply(instance, DifferenceFluidIngredient::new));

    public static final FluidIngredientType<DifferenceFluidIngredient> TYPE = new FluidIngredientType<>(ResourceLocation.fromNamespaceAndPath(ResourceLib.MOD_ID, "difference"), CODEC);

    @Override
    public List<FluidResource> getMatchingFluids() {
        List<FluidResource> stacks = new ArrayList<>(minuend.getMatchingFluids());
        stacks.removeIf(subtrahend);
        return stacks;
    }

    @Override
    public boolean requiresTesting() {
        return minuend.requiresTesting() || subtrahend.requiresTesting();
    }

    @Override
    public boolean test(FluidResource fluidResource) {
        return minuend.test(fluidResource) && !subtrahend.test(fluidResource);
    }

    @Override
    public FluidIngredientType<?> getType() {
        return TYPE;
    }
}
