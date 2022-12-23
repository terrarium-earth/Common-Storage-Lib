package earth.terrarium.botarium.api.fluid;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.material.Fluid;

import java.util.Optional;

/**
 * An object that holds a fluid with an amount and a tag.
 * Similar to {@link net.minecraft.world.item.ItemStack}, but for fluids.
 */
public interface FluidHolder {

    Codec<FluidHolder> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Registry.FLUID.byNameCodec().fieldOf("fluid").forGetter(FluidHolder::getFluid),
            Codec.FLOAT.fieldOf("buckets").orElse(1f).forGetter(fluidHolder -> (float) fluidHolder.getFluidAmount() / FluidHooks.getBucketAmount()),
            CompoundTag.CODEC.optionalFieldOf("tag").forGetter(fluidHolder -> Optional.ofNullable(fluidHolder.getCompound()))
    ).apply(instance, (fluid, buckets, compoundTag) -> FluidHooks.newFluidHolder(fluid, FluidHooks.buckets(buckets), compoundTag.orElse(null))));

    static FluidHolder of(Fluid fluid) {
        return FluidHooks.newFluidHolder(fluid, FluidHooks.buckets(1D), null);
    }

    static FluidHolder of(Fluid fluid, double buckets, CompoundTag tag) {
        return FluidHooks.newFluidHolder(fluid, FluidHooks.buckets(buckets), tag);
    }

    /**
     * @return The {@link Fluid} in the holder.
     */
    Fluid getFluid();

    /**
     * Sets the {@link Fluid} in the holder.
     * @param fluid The {@link Fluid} to set in the holder.
     */
    void setFluid(Fluid fluid);

    /**
     * @return The amount of fluid in the holder.
     */
    long getFluidAmount();

    /**
     * Sets the amount of fluid in the holder.
     * @param amount The amount of fluid to set in the holder.
     */
    void setAmount(long amount);

    /**
     * @return The {@link CompoundTag} in the holder.
     */
    CompoundTag getCompound();

    /**
     * Sets the {@link CompoundTag} in the holder.
     * @param tag The {@link CompoundTag} to set in the holder.
     */
    void setCompound(CompoundTag tag);

    /**
     * @return True if the holder is empty, false otherwise.
     */
    boolean isEmpty();

    /**
     * Compares the {@link FluidHolder} to another {@link FluidHolder} ignoring the amount.
     * @param fluidHolder The {@link FluidHolder} to compare to.
     * @return True if the given holder is equal to this holder, false otherwise.
     */
    boolean matches(FluidHolder fluidHolder);

    /**
     * @return A copy of the {@link FluidHolder}.
     */
    FluidHolder copyHolder();

    /**
     * @return A serialized version of the {@link FluidHolder} into a {@link CompoundTag}.
     */
    CompoundTag serialize();

    /**
     * Deserializes a {@link FluidHolder} from a given {@link CompoundTag}.
     * @param tag The {@link CompoundTag} to deserialize from.
     */
    void deserialize(CompoundTag tag);

    /**
     * Does not set amount if the holder is empty.
     * @param amount The amount to set in the copy.
     * @return A copy of the {@link FluidHolder} with the given amount.
     */
    default FluidHolder copyWithAmount(long amount) {
        FluidHolder copy = copyHolder();
        if (!copy.isEmpty()) copy.setAmount(amount);
        return copy;
    }
}
