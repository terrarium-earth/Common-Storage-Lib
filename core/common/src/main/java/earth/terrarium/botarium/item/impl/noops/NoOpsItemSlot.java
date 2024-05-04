package earth.terrarium.botarium.item.impl.noops;

import earth.terrarium.botarium.resources.item.ItemResource;
import earth.terrarium.botarium.storage.base.StorageSlot;

public class NoOpsItemSlot implements StorageSlot<ItemResource> {
    public static final NoOpsItemSlot NO_OPS = new NoOpsItemSlot();

    @Override
    public long getLimit() {
        return 0;
    }

    @Override
    public boolean isValueValid(ItemResource unit) {
        return false;
    }

    @Override
    public ItemResource getUnit() {
        return ItemResource.BLANK;
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
    public long insert(ItemResource unit, long amount, boolean simulate) {
        return 0;
    }

    @Override
    public long extract(ItemResource unit, long amount, boolean simulate) {
        return 0;
    }
}
