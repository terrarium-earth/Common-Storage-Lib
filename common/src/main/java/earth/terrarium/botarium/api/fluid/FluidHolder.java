package earth.terrarium.botarium.api.fluid;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.material.Fluid;

public interface FluidHolder {
    Fluid getFluid();
    void setFluid(Fluid fluid);
    long getFluidAmount();
    void setAmount(long amount);
    CompoundTag getCompound();
    void setCompound(CompoundTag tag);
    boolean isEmpty();
    boolean matches(FluidHolder fluidHolder);
    FluidHolder copyHolder();
    CompoundTag serialize();
    void deserialize(CompoundTag tag);
}
