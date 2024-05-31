package earth.terrarium.common_storage_lib.energy.impl;

import com.mojang.serialization.Codec;
import earth.terrarium.common_storage_lib.context.ItemContext;
import earth.terrarium.common_storage_lib.resources.item.ItemResource;
import earth.terrarium.common_storage_lib.storage.base.ValueStorage;
import earth.terrarium.common_storage_lib.storage.base.UpdateManager;

public class SimpleValueStorage implements ValueStorage, UpdateManager<Long> {
    public static final String KEY = "common_storage_lib:value_content";
    private final long capacity;
    private long amount;
    private final Runnable onUpdate;

    public SimpleValueStorage(long capacity, Runnable onUpdate) {
        this.capacity = capacity;
        this.onUpdate = onUpdate;
    }

    public SimpleValueStorage(long capacity, ItemContext context) {
        this.capacity = capacity;
        this.onUpdate = () -> {
            ItemResource updatedItem = context.getResource().set(Codec.LONG, KEY, amount);
            context.exchange(updatedItem, context.getAmount(), false);
        };
        this.amount = context.getResource().getOrDefault(Codec.LONG, KEY, 0L);
    }

    @Override
    public long getStoredAmount() {
        return amount;
    }

    @Override
    public long getCapacity() {
        return capacity;
    }

    public void set(long amount) {
        this.amount = amount;
    }

    @Override
    public boolean allowsInsertion() {
        return true;
    }

    @Override
    public boolean allowsExtraction() {
        return true;
    }

    @Override
    public long insert(long amount, boolean simulate) {
        long inserted = Math.min(amount, capacity - this.amount);
        if (!simulate) {
            this.amount += inserted;
        }
        return inserted;
    }

    public long insertAndUpdate(long amount, boolean simulate) {
        long inserted = Math.min(amount, capacity - this.amount);
        if (!simulate) {
            this.amount += inserted;
            onUpdate.run();
        }
        return inserted;
    }

    @Override
    public long extract(long amount, boolean simulate) {
        long extracted = Math.min(amount, this.amount);
        if (!simulate) {
            this.amount -= extracted;
        }
        return extracted;
    }

    public long extractAndUpdate(long amount, boolean simulate) {
        long extracted = Math.min(amount, this.amount);
        if (!simulate) {
            this.amount -= extracted;
            onUpdate.run();
        }
        return extracted;
    }

    @Override
    public Long createSnapshot() {
        return amount;
    }

    @Override
    public void readSnapshot(Long snapshot) {
        this.amount = snapshot;
    }

    @Override
    public void update() {
        onUpdate.run();
    }

    public static class ExtractOnly extends SimpleValueStorage {
        public ExtractOnly(long capacity, Runnable onUpdate) {
            super(capacity, onUpdate);
        }

        @Override
        public long insert(long amount, boolean simulate) {
            return 0;
        }

        @Override
        public boolean allowsInsertion() {
            return false;
        }
    }

    public static class InsertOnly extends SimpleValueStorage {
        public InsertOnly(long capacity, Runnable onUpdate) {
            super(capacity, onUpdate);
        }

        @Override
        public long extract(long amount, boolean simulate) {
            return 0;
        }

        @Override
        public boolean allowsExtraction() {
            return false;
        }
    }
}
