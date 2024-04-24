package earth.terrarium.botarium.common.storage.impl;

import earth.terrarium.botarium.common.data.impl.SingleItemData;
import earth.terrarium.botarium.common.storage.base.UnitSlot;
import earth.terrarium.botarium.common.storage.util.UpdateManager;
import earth.terrarium.botarium.common.transfer.impl.ItemUnit;

import java.util.function.Predicate;

public class SimpleItemSlot implements UnitSlot<ItemUnit>, UpdateManager<SingleItemData> {
    private final long limit;
    private final Runnable update;
    private ItemUnit unit;
    private long amount;

    public SimpleItemSlot(long limit, Runnable update) {
        this.unit = ItemUnit.BLANK;
        this.amount = getAmount();
        this.limit = limit;
        this.update = update;
    }

    @Override
    public long getLimit() {
        return limit;
    }

    @Override
    public boolean isValueValid(ItemUnit unit) {
        return true;
    }

    @Override
    public ItemUnit getUnit() {
        return unit;
    }

    @Override
    public long getAmount() {
        return amount;
    }

    @Override
    public long insert(ItemUnit unit, long amount, boolean simulate) {
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
    public long extract(ItemUnit unit, long amount, boolean simulate) {
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
    public SingleItemData createSnapshot() {
        return SingleItemData.of(this);
    }

    @Override
    public void readSnapshot(SingleItemData snapshot) {
        this.unit = snapshot.unit();
        this.amount = snapshot.amount();
    }

    @Override
    public void update() {
        update.run();
    }

    public static class Filtered extends SimpleItemSlot {
        private final Predicate<ItemUnit> filter;

        public Filtered(long limit, Runnable update, Predicate<ItemUnit> filter) {
            super(limit, update);
            this.filter = filter;
        }

        @Override
        public boolean isValueValid(ItemUnit unit) {
            return filter.test(unit);
        }
    }
}