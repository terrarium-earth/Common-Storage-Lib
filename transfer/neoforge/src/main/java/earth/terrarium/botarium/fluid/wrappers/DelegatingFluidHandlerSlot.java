package earth.terrarium.botarium.fluid.wrappers;

import earth.terrarium.botarium.fluid.util.ConversionUtils;
import earth.terrarium.botarium.fluid.base.FluidUnit;
import earth.terrarium.botarium.storage.base.StorageSlot;

public record DelegatingFluidHandlerSlot(AbstractCommonFluidContainer provider, int slot) implements StorageSlot<FluidUnit> {
    @Override
    public long getLimit() {
        return provider.handler().getTankCapacity(slot);
    }

    @Override
    public boolean isValueValid(FluidUnit unit) {
        return provider.handler().isFluidValid(slot, ConversionUtils.convert(unit, 1));
    }

    @Override
    public FluidUnit getUnit() {
        return ConversionUtils.convert(provider.handler().getFluidInTank(slot));
    }

    @Override
    public long getAmount() {
        return provider.handler().getFluidInTank(slot).getAmount();
    }

    @Override
    public boolean isBlank() {
        return provider.handler().getFluidInTank(slot).isEmpty();
    }

    @Override
    public long insert(FluidUnit unit, long amount, boolean simulate) {
        return provider.insert(unit, amount, simulate);
    }

    @Override
    public long extract(FluidUnit unit, long amount, boolean simulate) {
        if (!unit.matches(getUnit())) return 0;
        return provider.extract(unit, amount, simulate);
    }
}
