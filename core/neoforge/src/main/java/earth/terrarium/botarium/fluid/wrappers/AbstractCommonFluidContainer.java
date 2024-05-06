package earth.terrarium.botarium.fluid.wrappers;

import earth.terrarium.botarium.fluid.util.ConversionUtils;
import earth.terrarium.botarium.resources.fluid.FluidResource;
import earth.terrarium.botarium.storage.base.CommonStorage;
import earth.terrarium.botarium.storage.base.StorageSlot;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;

public interface AbstractCommonFluidContainer extends CommonStorage<FluidResource> {
    IFluidHandler handler();

    @Override
    default int getSlotCount() {
        return handler().getTanks();
    }

    @Override
    default long insert(FluidResource resource, long amount, boolean simulate) {
        return handler().fill(ConversionUtils.convert(resource, amount), simulate ? IFluidHandler.FluidAction.SIMULATE : IFluidHandler.FluidAction.EXECUTE);
    }

    @Override
    default long extract(FluidResource resource, long amount, boolean simulate) {
        return handler().drain(ConversionUtils.convert(resource, amount), simulate ? IFluidHandler.FluidAction.SIMULATE : IFluidHandler.FluidAction.EXECUTE).getAmount();
    }

    @Override
    default @NotNull StorageSlot<FluidResource> getSlot(int slot) {
        return new DelegatingFluidHandlerSlot(this, slot);
    }
}
