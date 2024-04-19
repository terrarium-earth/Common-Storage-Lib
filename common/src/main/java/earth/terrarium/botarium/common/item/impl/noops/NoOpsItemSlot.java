package earth.terrarium.botarium.common.item.impl.noops;

import earth.terrarium.botarium.common.storage.base.UnitSlot;
import earth.terrarium.botarium.common.storage.util.NoUpdate;
import earth.terrarium.botarium.common.transfer.impl.ItemUnit;

public class NoOpsItemSlot implements UnitSlot<ItemUnit>, NoUpdate {
    public static final NoOpsItemSlot NO_OPS = new NoOpsItemSlot();

    @Override
    public long getLimit() {
        return 0;
    }

    @Override
    public boolean isValueValid(ItemUnit unit) {
        return false;
    }

    @Override
    public ItemUnit getUnit() {
        return ItemUnit.BLANK;
    }

    @Override
    public long getAmount() {
        return 0;
    }

    @Override
    public long insert(ItemUnit unit, long amount, boolean simulate) {
        return 0;
    }

    @Override
    public long extract(ItemUnit unit, long amount, boolean simulate) {
        return 0;
    }
}
