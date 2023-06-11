package earth.terrarium.botarium.forge.fluid;

import earth.terrarium.botarium.common.fluid.base.FluidHolder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;

import java.util.Objects;

public class ForgeFluidHolder implements FluidHolder {
    protected FluidStack fluidStack;

    public ForgeFluidHolder(FluidStack stack) {
        this.fluidStack = stack.copy();
    }

    public ForgeFluidHolder(FluidHolder fluid) {
        this(toStack(fluid));
    }

    public ForgeFluidHolder(Fluid fluid, int amount, CompoundTag tag) {
        this.fluidStack = new FluidStack(fluid, amount, tag);
    }

    public FluidStack getFluidStack() {
        return fluidStack;
    }

    public static ForgeFluidHolder fromCompound(CompoundTag compound) {
        Fluid fluid = BuiltInRegistries.FLUID.get(new ResourceLocation(compound.getString("Fluid")));
        int amount = (int) compound.getLong("Amount");
        CompoundTag nbt = null;
        if (compound.contains("Nbt")) nbt = compound.getCompound("Nbt");
        return new ForgeFluidHolder(fluid, amount, nbt);
    }

    @Override
    public Fluid getFluid() {
        return fluidStack.getFluid();
    }

    @Override
    public void setFluid(Fluid fluid) {
        this.fluidStack = new FluidStack(fluid, this.fluidStack.getAmount(), this.fluidStack.getTag());
    }

    @Override
    public long getFluidAmount() {
        return fluidStack.getAmount();
    }

    @Override
    public void setAmount(long amount) {
        this.fluidStack = new FluidStack(this.fluidStack.getFluid(), (int) amount, this.fluidStack.getTag());
    }

    @Override
    public CompoundTag getCompound() {
        return fluidStack.getTag();
    }

    @Override
    public void setCompound(CompoundTag tag) {
        fluidStack.setTag(tag);
    }

    @Override
    public boolean matches(FluidHolder fluidHolder) {
        return this.getFluid().isSame(fluidHolder.getFluid()) && Objects.equals(fluidHolder.getCompound(), this.getCompound());
    }

    @Override
    public FluidHolder copyHolder() {
        return new ForgeFluidHolder(getFluid(), fluidStack.getAmount(), getCompound() == null ? null : getCompound().copy());
    }

    @Override
    public CompoundTag serialize() {
        CompoundTag compoundTag = new CompoundTag();
        compoundTag.putString("Fluid", BuiltInRegistries.FLUID.getKey(getFluid()).toString());
        compoundTag.putLong("Amount", getFluidAmount());
        if (this.getCompound() != null) {
            compoundTag.put("Nbt", getCompound());
        }
        return compoundTag;
    }

    @Override
    public void deserialize(CompoundTag compound) {
        long amount = compound.getLong("Amount");
        CompoundTag tag = null;
        if (compound.contains("Nbt")) {
            tag = compound.getCompound("Nbt");
        }
        this.fluidStack = new FluidStack(BuiltInRegistries.FLUID.get(new ResourceLocation(compound.getString("Fluid"))), (int) amount, tag);
    }

    @Override
    public boolean isEmpty() {
        return fluidStack.isEmpty();
    }

    public static FluidStack toStack(FluidHolder holder) {
        return new FluidStack(holder.getFluid(), (int) holder.getFluidAmount(), holder.getCompound());
    }

    public static ForgeFluidHolder empty() {
        return new ForgeFluidHolder(FluidStack.EMPTY);
    }
}
