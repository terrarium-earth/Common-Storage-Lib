package earth.terrarium.botarium.item.impl.noops;

import earth.terrarium.botarium.item.base.ItemUnit;
import earth.terrarium.botarium.storage.base.CommonStorage;
import earth.terrarium.botarium.storage.base.StorageSlot;
import org.jetbrains.annotations.NotNull;

public final class NoOpsItemContainer implements CommonStorage<ItemUnit> {
    public static final NoOpsItemContainer NO_OPS = new NoOpsItemContainer();

    @Override
    public int getSlotCount() {
        return 0;
    }

    @Override
    public @NotNull StorageSlot<ItemUnit> getSlot(int slot) {
        return NoOpsItemSlot.NO_OPS;
    }

    @Override
    public long insert(ItemUnit unit, long amount, boolean simulate) {
        return 0;
    }

    @Override
    public long extract(ItemUnit unit, long amount, boolean simulate) {
        return 0;
    }

    @Override
    public boolean allowsInsertion() {
        return false;
    }

    @Override
    public boolean allowsExtraction() {
        return false;
    }
}