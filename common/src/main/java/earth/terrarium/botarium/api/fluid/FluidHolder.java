package earth.terrarium.botarium.api.fluid;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.material.Fluid;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * An object that holds a fluid with an amount and a tag.
 * Similar to {@link net.minecraft.world.item.ItemStack}, but for fluids.
 */
@ParametersAreNonnullByDefault
public interface FluidHolder {

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
}
