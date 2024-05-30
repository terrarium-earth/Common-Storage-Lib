package earth.terrarium.botarium.item.impl.noops;

import earth.terrarium.botarium.resources.item.ItemResource;
import earth.terrarium.botarium.storage.base.CommonStorage;
import earth.terrarium.botarium.storage.base.StorageSlot;
import org.jetbrains.annotations.NotNull;

public final class NoOpsItemContainer implements CommonStorage<ItemResource> {
    public static final NoOpsItemContainer NO_OPS = new NoOpsItemContainer();

    @Override
    public int size() {
        return 0;
    }

    @Override
    public @NotNull StorageSlot<ItemResource> getSlot(int slot) {
        return NoOpsItemSlot.NO_OPS;
    }

    @Override
    public long insert(ItemResource resource, long amount, boolean simulate) {
        return 0;
    }

    @Override
    public long extract(ItemResource resource, long amount, boolean simulate) {
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