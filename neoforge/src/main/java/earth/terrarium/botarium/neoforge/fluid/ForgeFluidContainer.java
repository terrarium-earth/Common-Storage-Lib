package earth.terrarium.botarium.neoforge.fluid;

import earth.terrarium.botarium.common.fluid.base.FluidContainer;
import earth.terrarium.botarium.common.fluid.base.FluidHolder;
import earth.terrarium.botarium.util.Updatable;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;

public record ForgeFluidContainer<T extends FluidContainer & Updatable>(T container) implements IFluidHandler {

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
        return this.container.isFluidValid(i, new ForgeFluidHolder(fluidStack));
    }

    @Override
    public int fill(FluidStack fluidStack, FluidAction fluidAction) {
        int i = (int) this.container.insertFluid(new ForgeFluidHolder(fluidStack), fluidAction.simulate());
        if (i > 0 && fluidAction.execute()) {
            this.container.update();
        }
        return i;
    }

    @Override
    public @NotNull FluidStack drain(FluidStack fluidStack, FluidAction fluidAction) {
        FluidStack fluidStack1 = new ForgeFluidHolder(this.container.extractFluid(new ForgeFluidHolder(fluidStack), fluidAction.simulate())).getFluidStack();
        if(!fluidStack1.isEmpty() && fluidAction.execute()) {
            this.container.update();
        }
        return fluidStack1;
    }

    @Override
    public @NotNull FluidStack drain(int i, FluidAction fluidAction) {
        FluidHolder fluid = this.container.getFluids().get(0).copyHolder();
        if (fluid.isEmpty()) return FluidStack.EMPTY;
        fluid.setAmount(i);
        FluidStack fluidStack = new ForgeFluidHolder(this.container.extractFluid(fluid, fluidAction.simulate())).getFluidStack();
        if(!fluidStack.isEmpty() && fluidAction.execute()) {
            this.container.update();
        }
        return fluidStack;
    }
}
