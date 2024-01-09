package earth.terrarium.botarium.common.fluid.utils;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import earth.terrarium.botarium.common.fluid.FluidConstants;
import earth.terrarium.botarium.common.fluid.base.FluidHolder;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class QuantifiedFluidIngredient extends FluidIngredient {
    public static final Codec<QuantifiedFluidIngredient> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            FluidIngredient.CODEC.fieldOf("ingredient").forGetter(QuantifiedFluidIngredient::getIngredient),
            Codec.LONG.fieldOf("millibuckets").orElse(1000L).forGetter(o -> FluidConstants.toMillibuckets(o.getFluidAmount()))
    ).apply(instance, (fluidIngredient, aLong) -> new QuantifiedFluidIngredient(fluidIngredient, FluidConstants.fromMillibuckets(aLong))));

    private final long fluidAmount;
    private final FluidIngredient ingredient;

    public QuantifiedFluidIngredient(FluidIngredient ingredient, long fluidAmount) {
        super(ingredient.getRawValues());
        this.ingredient = ingredient;
        this.fluidAmount = fluidAmount;
    }

    public long getFluidAmount() {
        return fluidAmount;
    }

    public FluidIngredient getIngredient() {
        return ingredient;
    }

    @Override
    public boolean test(FluidHolder fluidHolder) {
        return ingredient.test(fluidHolder) && fluidHolder.getFluidAmount() >= this.fluidAmount;
    }

    @Override
    public List<FluidHolder> getFluids() {
        return ingredient.getFluids();
    }

    @Override
    public List<Either<FluidValue, TagValue>> getRawValues() {
        return ingredient.getRawValues();
    }
}
