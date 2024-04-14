package earth.terrarium.botarium.common.fluid.base;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import earth.terrarium.botarium.common.data.utils.ComponentExtras;
import earth.terrarium.botarium.common.fluid.FluidConstants;
import net.minecraft.core.component.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class FluidHolder implements DataComponentHolder, ComponentExtras {
    public static final FluidHolder EMPTY = new FluidHolder(null, 0, new PatchedDataComponentMap(DataComponentMap.EMPTY));

    public static final Codec<FluidHolder> MILLIBUCKET_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BuiltInRegistries.FLUID.byNameCodec().fieldOf("fluid").forGetter(FluidHolder::getFluid),
            Codec.LONG.fieldOf("millibuckets").forGetter(FluidHolder::getAmountAsMb),
            DataComponentPatch.CODEC.optionalFieldOf("components", DataComponentPatch.EMPTY).forGetter(FluidHolder::getPatch)
    ).apply(instance, FluidHolder::ofMb));

    public static final Codec<FluidHolder> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BuiltInRegistries.FLUID.byNameCodec().fieldOf("fluid").forGetter(FluidHolder::getFluid),
            Codec.LONG.fieldOf("amount").forGetter(FluidHolder::getAmount),
            DataComponentPatch.CODEC.optionalFieldOf("components", DataComponentPatch.EMPTY).forGetter(FluidHolder::getPatch)
    ).apply(instance, FluidHolder::of));

    public static final StreamCodec<? extends RegistryFriendlyByteBuf, FluidHolder> NETWORK_CODEC = null;

    private final Fluid fluid;
    private long amount;
    private final PatchedDataComponentMap components;

    private FluidHolder(Fluid fluid, long amount, PatchedDataComponentMap components) {
        this.fluid = fluid;
        this.amount = amount;
        this.components = components;
    }

    public static FluidHolder of(Fluid fluid) {
        return of(fluid, FluidConstants.BUCKET);
    }

    public static FluidHolder of(Fluid fluid, long amount) {
        return new FluidHolder(fluid, amount, new PatchedDataComponentMap(DataComponentMap.EMPTY));
    }

    public static FluidHolder of(Fluid fluid, long amount, DataComponentPatch patch) {
        return new FluidHolder(fluid, amount, PatchedDataComponentMap.fromPatch(DataComponentMap.EMPTY, patch));
    }

    public static FluidHolder ofMb(Fluid fluid, long millibuckets) {
        return new FluidHolder(fluid, FluidConstants.toPlatformAmount(millibuckets), new PatchedDataComponentMap(DataComponentMap.EMPTY));
    }

    public static FluidHolder ofMb(Fluid fluid, long millibuckets, DataComponentPatch patch) {
        return new FluidHolder(fluid, FluidConstants.toPlatformAmount(millibuckets), PatchedDataComponentMap.fromPatch(DataComponentMap.EMPTY, patch));
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

    public static boolean matches(FluidHolder first, FluidHolder second) {
        if (first == second) {
            return true;
        } else {
            return first.getAmount() == second.getAmount() && isSameFluidSameComponents(first, second);
        }
    }

    public static boolean isSameFluid(FluidHolder first, FluidHolder second) {
        return first.is(second.getFluid());
    }

    public static boolean isSameFluidSameComponents(FluidHolder first, FluidHolder second) {
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
