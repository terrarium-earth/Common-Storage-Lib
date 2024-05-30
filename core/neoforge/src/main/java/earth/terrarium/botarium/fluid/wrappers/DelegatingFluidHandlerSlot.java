package earth.terrarium.botarium.fluid.wrappers;

import earth.terrarium.botarium.fluid.util.ConversionUtils;
import earth.terrarium.botarium.resources.fluid.FluidResource;
import earth.terrarium.botarium.storage.base.StorageSlot;

public record DelegatingFluidHandlerSlot(AbstractCommonFluidContainer provider, int slot) implements StorageSlot<FluidResource> {
    @Override
    public long getLimit(FluidResource resource) {
        return provider.handler().getTankCapacity(slot);
    }

    @Override
    public boolean isResourceValid(FluidResource resource) {
        return provider.handler().isFluidValid(slot, ConversionUtils.convert(resource, 1));
    }

    @Override
    public FluidResource getResource() {
        return ConversionUtils.convert(provider.handler().getFluidInTank(slot));
    }

    @Override
    public long getAmount() {
        return provider.handler().getFluidInTank(slot).getAmount();
    }

    @Override
    public long insert(FluidResource resource, long amount, boolean simulate) {
        return provider.insert(resource, amount, simulate);
    }

    @Override
    public long extract(FluidResource resource, long amount, boolean simulate) {
        if (!resource.equals(getResource())) return 0;
        return provider.extract(resource, amount, simulate);
    }
}
