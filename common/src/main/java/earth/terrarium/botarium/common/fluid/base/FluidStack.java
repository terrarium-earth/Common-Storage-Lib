package earth.terrarium.botarium.common.fluid.base;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import earth.terrarium.botarium.common.data.utils.ComponentExtras;
import earth.terrarium.botarium.common.fluid.FluidConstants;
import net.minecraft.core.Holder;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public final class FluidStack implements DataComponentHolder, ComponentExtras {
    public static final FluidStack EMPTY = new FluidStack(null, 0, new PatchedDataComponentMap(DataComponentMap.EMPTY));

    public static final Codec<FluidStack> MILLIBUCKET_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BuiltInRegistries.FLUID.byNameCodec().fieldOf("id").forGetter(FluidStack::getFluid),
            Codec.LONG.fieldOf("millibuckets").forGetter(FluidStack::getAmountAsMb),
            DataComponentPatch.CODEC.optionalFieldOf("components", DataComponentPatch.EMPTY).forGetter(FluidStack::getPatch)
    ).apply(instance, FluidStack::ofMb));

    public static final Codec<FluidStack> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BuiltInRegistries.FLUID.byNameCodec().fieldOf("id").forGetter(FluidStack::getFluid),
            Codec.LONG.fieldOf("amount").forGetter(FluidStack::getAmount),
            DataComponentPatch.CODEC.optionalFieldOf("components", DataComponentPatch.EMPTY).forGetter(FluidStack::getPatch)
    ).apply(instance, FluidStack::of));

    public static final StreamCodec<RegistryFriendlyByteBuf, Holder<Fluid>> FLUID_HOLDER_STREAM_CODEC = ByteBufCodecs.holderRegistry(Registries.FLUID);

    public static final StreamCodec<RegistryFriendlyByteBuf, FluidStack> NETWORK_CODEC = new StreamCodec<RegistryFriendlyByteBuf, FluidStack>() {
        public FluidStack decode(RegistryFriendlyByteBuf registryFriendlyByteBuf) {
            long i = registryFriendlyByteBuf.readLong();
            if (i <= 0) {
                return FluidStack.EMPTY;
            } else {
                Holder<Fluid> holder = FLUID_HOLDER_STREAM_CODEC.decode(registryFriendlyByteBuf);
                DataComponentPatch dataComponentPatch = DataComponentPatch.STREAM_CODEC.decode(registryFriendlyByteBuf);
                return FluidStack.of(holder, i, dataComponentPatch);
            }
        }

        public void encode(RegistryFriendlyByteBuf registryFriendlyByteBuf, FluidStack fluidHolder) {
            if (fluidHolder.isEmpty()) {
                registryFriendlyByteBuf.writeLong(0);
            } else {
                registryFriendlyByteBuf.writeLong(fluidHolder.amount);
                FLUID_HOLDER_STREAM_CODEC.encode(registryFriendlyByteBuf, fluidHolder.getFluidHolder());
                DataComponentPatch.STREAM_CODEC.encode(registryFriendlyByteBuf, fluidHolder.components.asPatch());
            }
        }
    };

    public static final StreamCodec<RegistryFriendlyByteBuf, List<FluidStack>> LIST_CODEC = NETWORK_CODEC.apply(ByteBufCodecs.collection(NonNullList::createWithCapacity));

    private final Fluid fluid;
    private long amount;
    private final PatchedDataComponentMap components;

    private FluidStack(Fluid fluid, long amount, PatchedDataComponentMap components) {
        this.fluid = fluid;
        this.amount = amount;
        this.components = components;
    }

    public static FluidStack of(Fluid fluid) {
        return of(fluid, FluidConstants.BUCKET);
    }

    public static FluidStack of(Fluid fluid, long amount) {
        return new FluidStack(fluid, amount, new PatchedDataComponentMap(DataComponentMap.EMPTY));
    }

    public static FluidStack of(Fluid fluid, long amount, DataComponentPatch patch) {
        return new FluidStack(fluid, amount, PatchedDataComponentMap.fromPatch(DataComponentMap.EMPTY, patch));
    }

    public static FluidStack of(Holder<Fluid> fluid, long amount, DataComponentPatch patch) {
        return new FluidStack(fluid.value(), amount, PatchedDataComponentMap.fromPatch(DataComponentMap.EMPTY, patch));
    }

    public static FluidStack ofMb(Fluid fluid, long millibuckets) {
        return new FluidStack(fluid, FluidConstants.toPlatformAmount(millibuckets), new PatchedDataComponentMap(DataComponentMap.EMPTY));
    }

    public static FluidStack ofMb(Fluid fluid, long millibuckets, DataComponentPatch patch) {
        return new FluidStack(fluid, FluidConstants.toPlatformAmount(millibuckets), PatchedDataComponentMap.fromPatch(DataComponentMap.EMPTY, patch));
    }

    @Override
    public @NotNull DataComponentMap getComponents() {
        return isEmpty() ? DataComponentMap.EMPTY : components;
    }

    public DataComponentPatch getPatch() {
        return isEmpty() ? DataComponentPatch.EMPTY : components.asPatch();
    }

    public Fluid getFluid() {
        return isEmpty() ? Fluids.EMPTY : fluid;
    }

    public Holder<Fluid> getFluidHolder() {
        return fluid.builtInRegistryHolder();
    }

    public long getAmount() {
        return isEmpty() ? 0 : amount;
    }

    public long getAmountAsMb() {
        return isEmpty() ? 0 : FluidConstants.toMillibuckets(amount);
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public void grow(long amount) {
        this.amount += amount;
    }

    public void shrink(long amount) {
        this.amount -= amount;
    }

    public boolean isEmpty() {
        return this == EMPTY || this.fluid == Fluids.EMPTY || this.amount <= 0;
    }

    public boolean is(Fluid fluid) {
        return this.fluid == fluid;
    }

    public static boolean matches(FluidStack first, FluidStack second) {
        if (first == second) {
            return true;
        } else {
            return first.getAmount() == second.getAmount() && isSameFluidSameComponents(first, second);
        }
    }

    public static boolean isSameFluid(FluidStack first, FluidStack second) {
        return first.is(second.getFluid());
    }

    public static boolean isSameFluidSameComponents(FluidStack first, FluidStack second) {
        if (!first.is(second.getFluid())) {
            return false;
        } else {
            return first.isEmpty() && second.isEmpty() || Objects.equals(first.components, second.components);
        }
    }

    @Override
    public <T> T setComponent(DataComponentType<T> componentType, T data) {
        return isEmpty() ? null : components.set(componentType, data);
    }

    @Override
    public <T> T removeComponent(DataComponentType<T> componentType) {
        return isEmpty() ? null : components.remove(componentType);
    }
}
