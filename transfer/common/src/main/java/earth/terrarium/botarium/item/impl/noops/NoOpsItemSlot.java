package earth.terrarium.botarium.item.impl.noops;

import earth.terrarium.botarium.item.base.ItemUnit;
import earth.terrarium.botarium.storage.base.StorageSlot;

public class NoOpsItemSlot implements StorageSlot<ItemUnit> {
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
    public boolean isBlank() {
        return true;
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
