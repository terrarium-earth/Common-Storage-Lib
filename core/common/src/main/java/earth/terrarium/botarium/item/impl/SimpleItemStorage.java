package earth.terrarium.botarium.item.impl;

import earth.terrarium.botarium.context.ItemContext;
import earth.terrarium.botarium.item.util.ItemStorageData;
import earth.terrarium.botarium.resources.item.ItemResource;
import earth.terrarium.botarium.storage.base.CommonStorage;
import earth.terrarium.botarium.storage.base.UpdateManager;
import earth.terrarium.botarium.storage.util.TransferUtil;
import net.minecraft.core.NonNullList;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public class SimpleItemStorage implements CommonStorage<ItemResource>, UpdateManager<ItemStorageData> {
    public static final String KEY = "botarium:item_contents";

    protected final NonNullList<SimpleItemSlot> slots;
    private final Runnable onUpdate;

    public SimpleItemStorage(int size, Runnable onUpdate) {
        this.slots = NonNullList.withSize(size, new SimpleItemSlot(this::update));
        this.onUpdate = onUpdate;
    }

    public SimpleItemStorage(int size, ItemContext context) {
        this.slots = NonNullList.withSize(size, new SimpleItemSlot(this::update));
        this.onUpdate = () -> {
            ItemStorageData data = ItemStorageData.of(this);
            ItemResource resource = context.getResource().set(ItemStorageData.CODEC, KEY, data);
            context.exchange(resource, context.getAmount(), false);
        };
        readSnapshot(context.getResource().getOrDefault(ItemStorageData.CODEC, KEY, ItemStorageData.EMPTY));
    }

    public SimpleItemStorage filter(int slot, Predicate<ItemResource> predicate) {
        slots.set(slot, new SimpleItemSlot.Filtered(this::update, predicate));
        return this;
    }

    @Override
    public int size() {
        return slots.size();
    }

    @Override
    public @NotNull SimpleItemSlot get(int index) {
        return slots.get(index);
    }

    @Override
    public ItemStorageData createSnapshot() {
        return ItemStorageData.of(this);
    }

    @Override
    public void readSnapshot(ItemStorageData snapshot) {
        for (int i = 0; i < slots.size() && i < snapshot.stacks().size(); i++) {
            slots.get(i).readSnapshot(snapshot.stacks().get(i));
        }
    }

    @Override
    public void update() {
        onUpdate.run();
    }

    @Override
    public long insert(ItemResource resource, long amount, boolean simulate) {
        return TransferUtil.insertSlots(this, resource, amount, simulate);
    }

    public long insertAndUpdate(ItemResource resource, long amount, boolean simulate) {
        long inserted = TransferUtil.insertSlots(this, resource, amount, simulate);
        if (!simulate) {
            onUpdate.run();
        }
        return inserted;
    }

    @Override
    public long extract(ItemResource resource, long amount, boolean simulate) {
        return TransferUtil.extractSlots(this, resource, amount, simulate);
    }

    public long extractAndUpdate(ItemResource resource, long amount, boolean simulate) {
        long extracted = TransferUtil.extractSlots(this, resource, amount, simulate);
        if (!simulate) {
            onUpdate.run();
        }
        return extracted;
    }

    public static class ExtractOnly extends SimpleItemStorage {
        public ExtractOnly(int size, Runnable onUpdate) {
            super(size, onUpdate);
        }

        @Override
        public long insert(ItemResource resource, long amount, boolean simulate) {
            return 0;
        }

        @Override
        public ExtractOnly filter(int slot, Predicate<ItemResource> predicate) {
            return (ExtractOnly) super.filter(slot, predicate);
        }

        @Override
        public boolean allowsInsertion() {
            return false;
        }
    }

    public static class InsertOnly extends SimpleItemStorage {
        public InsertOnly(int size, Runnable onUpdate) {
            super(size, onUpdate);
        }

        @Override
        public long extract(ItemResource resource, long amount, boolean simulate) {
            return 0;
        }

        @Override
        public InsertOnly filter(int slot, Predicate<ItemResource> predicate) {
            return (InsertOnly) super.filter(slot, predicate);
        }

        @Override
        public boolean allowsExtraction() {
            return false;
        }
    }
}
