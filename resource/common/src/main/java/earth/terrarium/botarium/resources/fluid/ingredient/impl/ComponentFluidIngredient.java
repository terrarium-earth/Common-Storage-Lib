package earth.terrarium.botarium.resources.fluid.ingredient.impl;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import earth.terrarium.botarium.resources.fluid.FluidResource;
import earth.terrarium.botarium.resources.fluid.ingredient.FluidIngredient;
import earth.terrarium.botarium.resources.fluid.ingredient.FluidIngredientType;
import net.minecraft.core.component.DataComponentPredicate;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public record ComponentFluidIngredient(FluidIngredient ingredient, DataComponentPredicate predicate) implements FluidIngredient {
    public static final MapCodec<ComponentFluidIngredient> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            FluidIngredient.CODEC.fieldOf("ingredient").forGetter(ComponentFluidIngredient::ingredient),
            DataComponentPredicate.CODEC.fieldOf("predicate").forGetter(ComponentFluidIngredient::predicate)
    ).apply(instance, ComponentFluidIngredient::new));

    public static final FluidIngredientType<ComponentFluidIngredient> TYPE = new FluidIngredientType<>(new ResourceLocation("botarium", "component"), CODEC);

    @Override
    public boolean test(FluidResource fluidResource) {
        return ingredient.test(fluidResource) && predicate.test(fluidResource.getComponents());
    }

    @Override
    public List<FluidResource> getMatchingFluids() {
        return ingredient().getMatchingFluids().stream().map(fluidResource -> fluidResource.modify(predicate.asPatch())).toList();
    }

    @Override
    public boolean requiresTesting() {
        return true;
    }

    @Override
    public FluidIngredientType<?> getType() {
        return TYPE;
    }
}
