package earth.terrarium.botarium.fluid.wrappers;

import earth.terrarium.botarium.fluid.util.ConversionUtils;
import earth.terrarium.botarium.fluid.base.FluidUnit;
import earth.terrarium.botarium.storage.base.CommonStorage;
import earth.terrarium.botarium.storage.base.StorageSlot;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;

public interface AbstractCommonFluidContainer extends CommonStorage<FluidUnit> {
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
    default @NotNull StorageSlot<FluidUnit> getSlot(int slot) {
        return new DelegatingFluidHandlerSlot(this, slot);
    }
}
