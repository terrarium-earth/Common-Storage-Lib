package earth.terrarium.botarium.common.transfer.impl;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import earth.terrarium.botarium.common.fluid.FluidConstants;
import earth.terrarium.botarium.common.fluid.base.FluidStack;
import earth.terrarium.botarium.common.transfer.base.TransferUnit;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

public record FluidUnit(Fluid type, DataComponentPatch components) implements TransferUnit<Fluid> {
    public static final FluidUnit BLANK = new FluidUnit(Fluids.EMPTY, DataComponentPatch.EMPTY);

    public static final Codec<FluidUnit> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BuiltInRegistries.FLUID.byNameCodec().fieldOf("id").forGetter(FluidUnit::type),
            DataComponentPatch.CODEC.optionalFieldOf("components", DataComponentPatch.EMPTY).forGetter(FluidUnit::components)
    ).apply(instance, FluidUnit::new));

    public static final StreamCodec<? super RegistryFriendlyByteBuf, Holder<Fluid>> FLUID_STREAM_CODEC = ByteBufCodecs.holderRegistry(Registries.FLUID);

    public static final StreamCodec<? super RegistryFriendlyByteBuf, FluidUnit> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public FluidUnit decode(RegistryFriendlyByteBuf object) {
            Holder<Fluid> holder = FLUID_STREAM_CODEC.decode(object);
            DataComponentPatch dataComponentPatch = DataComponentPatch.STREAM_CODEC.decode(object);
            return new FluidUnit(holder.value(), dataComponentPatch);
        }

        @Override
        public void encode(RegistryFriendlyByteBuf object, FluidUnit object2) {
            FLUID_STREAM_CODEC.encode(object, object2.type.builtInRegistryHolder());
            DataComponentPatch.STREAM_CODEC.encode(object, object2.components);
        }
    };

    public static FluidUnit of(Fluid fluid) {
        return new FluidUnit(fluid, DataComponentPatch.EMPTY);
    }

    public static FluidUnit of(Fluid fluid, DataComponentPatch components) {
        return new FluidUnit(fluid, components);
    }

    public static FluidUnit of(FluidStack holder) {
        return new FluidUnit(holder.getFluid(), holder.getPatch());
    }

    @Override
    public boolean isBlank() {
        return type == Fluids.EMPTY;
    }

    public boolean matches(FluidStack holder) {
        return isOf(holder.getFluid()) && componentsMatch(holder.getPatch());
    }

    public FluidStack toHolder(long amount) {
        return FluidStack.of(type, amount, components);
    }

    public FluidStack toHolder() {
        return toHolder(FluidConstants.BUCKET);
    }
}
