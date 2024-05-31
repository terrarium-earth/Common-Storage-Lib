package earth.terrarium.common_storage_lib.resources.fluid.ingredient;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.teamresourceful.bytecodecs.base.ByteCodec;
import earth.terrarium.common_storage_lib.resources.fluid.FluidResource;
import earth.terrarium.common_storage_lib.resources.fluid.ingredient.impl.*;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public interface FluidIngredient extends Predicate<FluidResource> {
    Codec<FluidIngredient> TYPE_CODEC = FluidIngredientRegistry.TYPE_CODEC.dispatch(FluidIngredient::getType, fluidIngredientType -> fluidIngredientType.codec().codec());
    Codec<FluidIngredient> CODEC = Codec.either(BaseFluidIngredient.CODEC, TYPE_CODEC).xmap(either -> either.map(l -> l, r -> r), ingredient -> ingredient instanceof BaseFluidIngredient ? Either.left((BaseFluidIngredient) ingredient) : Either.right(ingredient));
    MapCodec<FluidIngredient> MAP_CODEC = FluidIngredientRegistry.TYPE_CODEC.dispatchMap(FluidIngredient::getType, fluidIngredientType -> fluidIngredientType.codec().codec());

    ByteCodec<FluidIngredient> BYTE_CODEC = FluidIngredientRegistry.STREAM_CODEC.dispatch(type -> (ByteCodec<FluidIngredient>) type.streamCodec(), FluidIngredient::getType);

    static FluidIngredient of(FluidResource... stacks) {
        return BaseFluidIngredient.of(stacks);
    }

    static FluidIngredient of(TagKey<Fluid> tag) {
        return BaseFluidIngredient.of(tag);
    }

    static FluidIngredient all(FluidIngredient... ingredients) {
        return new AllMatchFluidIngredient(Arrays.asList(ingredients));
    }

    static FluidIngredient any(FluidIngredient... ingredients) {
        return new AnyMatchFluidIngredient(Arrays.asList(ingredients));
    }

    static FluidIngredient difference(FluidIngredient minuend, FluidIngredient subtrahend) {
        return new DifferenceFluidIngredient(minuend, subtrahend);
    }

    /*
    static FluidIngredient components(FluidIngredient base, DataComponentPredicate components) {
        return new ComponentFluidIngredient(base, components);
    }
     */

    static SizedFluidIngredient sized(FluidIngredient ingredient, long size) {
        return new SizedFluidIngredient(ingredient, size);
    }

    boolean test(FluidResource fluidResource);

    List<FluidResource> getMatchingFluids();

    boolean requiresTesting();

    FluidIngredientType<?> getType();
}
