package earth.terrarium.botarium.common.storage.impl;

import earth.terrarium.botarium.common.data.impl.SingleFluidData;
import earth.terrarium.botarium.common.storage.base.UnitSlot;
import earth.terrarium.botarium.common.storage.util.UpdateManager;
import earth.terrarium.botarium.common.transfer.impl.FluidUnit;

import java.util.function.Predicate;

public class SimpleFluidSlot implements UnitSlot<FluidUnit>, UpdateManager<SingleFluidData> {
    private final long limit;
    private final Runnable update;
    private FluidUnit unit;
    private long amount;

    public SimpleFluidSlot(long limit, Runnable update) {
        this.unit = FluidUnit.BLANK;
        this.amount = getAmount();
        this.limit = limit;
        this.update = update;
    }

    @Override
    public long getLimit() {
        return limit;
    }

    @Override
    public boolean isValueValid(FluidUnit unit) {
        return true;
    }

    @Override
    public FluidUnit getUnit() {
        return unit;
    }

    @Override
    public long getAmount() {
        return amount;
    }

    @Override
    public long insert(FluidUnit unit, long amount, boolean simulate) {
        if (!isValueValid(unit)) return 0;
        if (this.unit.isBlank()) {
            long inserted = Math.min(amount, limit);
            if (!simulate) {
                this.unit = unit;
                this.amount = inserted;
            }
            return inserted;
        } else if (this.unit.matches(unit)) {
            long inserted = Math.min(amount, limit - this.amount);
            if (!simulate) {
                this.amount += inserted;
            }
            return inserted;
        }
        return 0;
    }

    @Override
    public long extract(FluidUnit unit, long amount, boolean simulate) {
        if (this.unit.matches(unit)) {
            long extracted = Math.min(amount, this.amount);
            if (!simulate) {
                this.amount -= extracted;
            }
            return extracted;
        }
        return 0;
    }

    @Override
    public SingleFluidData createSnapshot() {
        return SingleFluidData.of(this);
    }

    @Override
    public void readSnapshot(SingleFluidData snapshot) {
        this.unit = snapshot.unit();
        this.amount = snapshot.amount();
    }

    @Override
    public void update() {
        update.run();
    }

    public static class Filtered extends SimpleFluidSlot {
        private final Predicate<FluidUnit> filter;

        public Filtered(long limit, Runnable update, Predicate<FluidUnit> filter) {
            super(limit, update);
            this.filter = filter;
        }

        @Override
        public boolean isValueValid(FluidUnit unit) {
            return filter.test(unit);
        }
    }
}