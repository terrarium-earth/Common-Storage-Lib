package earth.terrarium.botarium.forge.fluid;

import earth.terrarium.botarium.api.fluid.FluidHolder;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;

import java.util.Objects;

public class ForgeFluidHolder extends FluidStack implements FluidHolder {
    public static final ForgeFluidHolder EMPTY = new ForgeFluidHolder(FluidStack.EMPTY);

    public ForgeFluidHolder(FluidStack stack) {
        super(stack, stack.getAmount());
    }

    public ForgeFluidHolder(FluidHolder fluid) {
        this(fluid.getFluid(), (int) fluid.getFluidAmount(), fluid.getCompound());
    }

    public ForgeFluidHolder(Fluid fluid, int amount, CompoundTag tag) {
        super(fluid, amount, tag);
    }

    public static ForgeFluidHolder fromCompound(CompoundTag compound) {
        Fluid fluid = Registry.FLUID.get(new ResourceLocation(compound.getString("Fluid")));
        int amount = (int) compound.getLong("Amount");
        CompoundTag nbt = null;
        if (compound.contains("Nbt")) nbt = compound.getCompound("Nbt");
        return new ForgeFluidHolder(fluid, amount, nbt);
    }

    @Override
    public void setFluid(Fluid fluid) {
        //Not used in forge
    }

    @Override
    public long getFluidAmount() {
        return getAmount();
    }

    @Override
    public void setAmount(long amount) {
        this.setAmount((int) amount);
    }

    @Override
    public CompoundTag getCompound() {
        return getTag();
    }

    @Override
    public void setCompound(CompoundTag tag) {
        this.setTag(tag);
    }

    @Override
    public boolean matches(FluidHolder fluidHolder) {
        return this.getFluid().isSame(fluidHolder.getFluid()) && Objects.equals(fluidHolder.getCompound(), this.getCompound());
    }

    @Override
    public FluidHolder copyHolder() {
        return new ForgeFluidHolder(getFluid(), getAmount(), getCompound() == null ? null : getCompound().copy());
    }

    @Override
    public CompoundTag serialize() {
        CompoundTag compoundTag = new CompoundTag();
        compoundTag.putString("Fluid", Registry.FLUID.getKey(getFluid()).toString());
        compoundTag.putLong("Amount", getFluidAmount());
        if(this.getCompound() != null) {
            compoundTag.put("Nbt", getCompound());
        }
        return compoundTag;
    }

    @Override
    public void deserialize(CompoundTag tag) {
        //not used
    }

    @Override
    public boolean isEmpty() {
        return this.getFluid() == Fluids.EMPTY || this.getFluidAmount() == 0;
    }
}
