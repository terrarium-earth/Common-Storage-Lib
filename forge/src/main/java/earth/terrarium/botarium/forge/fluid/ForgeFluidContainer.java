package earth.terrarium.botarium.forge.fluid;

import earth.terrarium.botarium.util.Serializable;
import earth.terrarium.botarium.common.fluid.base.FluidContainer;
import earth.terrarium.botarium.common.fluid.base.FluidHolder;
import earth.terrarium.botarium.forge.AutoSerializable;
import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record ForgeFluidContainer(FluidContainer container) implements IFluidHandler, ICapabilityProvider, AutoSerializable {

    @Override
    public int getTanks() {
        return container.getSize();
    }

    @Override
    public @NotNull FluidStack getFluidInTank(int i) {
        return new ForgeFluidHolder(container.getFluids().get(i)).fluidStack;
    }

    @Override
    public int getTankCapacity(int i) {
        return (int) this.container.getTankCapacity(i);
    }

    @Override
    public boolean isFluidValid(int i, @NotNull FluidStack fluidStack) {
        return this.container.getFluids().get(i).matches(new ForgeFluidHolder(fluidStack));
    }

    @Override
    public int fill(FluidStack fluidStack, FluidAction fluidAction) {
        return (int) this.container.insertFluid(new ForgeFluidHolder(fluidStack), fluidAction.simulate());
    }

    @Override
    public @NotNull FluidStack drain(FluidStack fluidStack, FluidAction fluidAction) {
        return new ForgeFluidHolder(this.container.extractFluid(new ForgeFluidHolder(fluidStack), fluidAction.simulate())).getFluidStack();
    }

    @Override
    public @NotNull FluidStack drain(int i, FluidAction fluidAction) {
        FluidHolder fluid = this.container.getFluids().get(0).copyHolder();
        if (fluid.isEmpty()) return FluidStack.EMPTY;
        fluid.setAmount(i);
        return new ForgeFluidHolder(this.container.extractFluid(fluid, fluidAction.simulate())).getFluidStack();
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction arg) {
        return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && container.getContainer(arg) != null ? LazyOptional.of(() -> this).cast() : LazyOptional.empty();
    }

    @Override
    public Serializable getSerializable() {
        return container;
    }
}
