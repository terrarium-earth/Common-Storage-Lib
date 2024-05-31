package earth.terrarium.common_storage_lib.fluid.wrappers;

import earth.terrarium.common_storage_lib.fluid.util.ConversionUtils;
import earth.terrarium.common_storage_lib.resources.fluid.FluidResource;
import earth.terrarium.common_storage_lib.storage.base.CommonStorage;
import earth.terrarium.common_storage_lib.storage.base.StorageSlot;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;

public interface AbstractCommonFluidContainer extends CommonStorage<FluidResource> {
    IFluidHandler handler();

    @Override
    default int size() {
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
    default @NotNull StorageSlot<FluidResource> get(int index) {
        return new DelegatingFluidHandlerSlot(this, index);
    }
}
