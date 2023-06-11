package earth.terrarium.botarium.forge.fluid;

import earth.terrarium.botarium.common.fluid.base.FluidHolder;
import earth.terrarium.botarium.common.fluid.base.PlatformFluidItemHandler;
import earth.terrarium.botarium.common.item.ItemStackHolder;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;

public record ForgeFluidItemHandler(IFluidHandlerItem handler) implements PlatformFluidItemHandler {
    @Override
    public long insertFluid(ItemStackHolder item, FluidHolder fluid, boolean simulate) {
        int fill = handler.fill(new ForgeFluidHolder(fluid).getFluidStack(), simulate ? IFluidHandler.FluidAction.SIMULATE : IFluidHandler.FluidAction.EXECUTE);
        item.setStack(handler.getContainer());
        return fill;
    }

    @Override
    public FluidHolder extractFluid(ItemStackHolder item, FluidHolder fluid, boolean simulate) {
        ForgeFluidHolder drained = new ForgeFluidHolder(handler.drain(new ForgeFluidHolder(fluid).getFluidStack(), simulate ? IFluidHandler.FluidAction.SIMULATE : IFluidHandler.FluidAction.EXECUTE));
        item.setStack(handler.getContainer());
        return drained;
    }

    @Override
    public int getTankAmount() {
        return handler.getTanks();
    }

    @Override
    public FluidHolder getFluidInTank(int tank) {
        return new ForgeFluidHolder(handler.getFluidInTank(tank));
    }

    @Override
    public long getTankCapacity(int tank) {
        return handler.getTankCapacity(tank);
    }

    @Override
    public boolean supportsInsertion() {
        return insertFluid(new ItemStackHolder(handler.getContainer()), getFluidInTank(0), true) > 0;
    }

    @Override
    public boolean supportsExtraction() {
        return extractFluid(new ItemStackHolder(handler.getContainer()), getFluidInTank(0), true).getFluidAmount() > 0;
    }
}
