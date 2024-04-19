package earth.terrarium.botarium.common.fluid.wrappers;

import earth.terrarium.botarium.common.fluid.ConversionUtils;
import earth.terrarium.botarium.common.storage.base.UnitContainer;
import earth.terrarium.botarium.common.storage.base.UnitSlot;
import earth.terrarium.botarium.common.transfer.impl.FluidUnit;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;

public interface AbstractCommonFluidContainer extends UnitContainer<FluidUnit> {
    IFluidHandler handler();

    @Override
    default int getSlotCount() {
        return handler().getTanks();
    }

    @Override
    default long insert(FluidUnit unit, long amount, boolean simulate) {
        return handler().fill(ConversionUtils.convert(unit, amount), simulate ? IFluidHandler.FluidAction.SIMULATE : IFluidHandler.FluidAction.EXECUTE);
    }

    @Override
    default long extract(FluidUnit unit, long amount, boolean simulate) {
        return handler().drain(ConversionUtils.convert(unit, amount), simulate ? IFluidHandler.FluidAction.SIMULATE : IFluidHandler.FluidAction.EXECUTE).getAmount();
    }

    @Override
    default @NotNull UnitSlot<FluidUnit> getSlot(int slot) {
        return new DelegatingFluidHandlerSlot(this, slot);
    }
}
