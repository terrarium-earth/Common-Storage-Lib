package earth.terrarium.common_storage_lib.energy.impl;

import earth.terrarium.common_storage_lib.CommonStorageLib;
import earth.terrarium.common_storage_lib.context.ItemContext;
import earth.terrarium.common_storage_lib.storage.base.ValueStorage;
import earth.terrarium.common_storage_lib.storage.base.UpdateManager;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.world.item.ItemStack;

public class SimpleValueStorage implements ValueStorage, UpdateManager<Long> {
    private final long capacity;
    private final Runnable onUpdate;
    private long amount;

    public SimpleValueStorage(long capacity) {
        this.capacity = capacity;
        this.onUpdate = () -> {};
    }

    @SuppressWarnings("DataFlowIssue")
    public SimpleValueStorage(long capacity, ItemStack stack, ItemContext context) {
        this.capacity = capacity;
        this.onUpdate = () -> {
            DataComponentPatch data = DataComponentPatch.builder().set(CommonStorageLib.VALUE_CONTENT.componentType(), this.amount).build();
            context.modify(data);
            context.updateAll();
        };
        if (stack.has(CommonStorageLib.VALUE_CONTENT.componentType())) {
            this.amount = stack.get(CommonStorageLib.VALUE_CONTENT.componentType());
        }
    }

    public SimpleValueStorage(long capacity, Object entityOrBlockEntity) {
        this.capacity = capacity;
        this.onUpdate = () -> CommonStorageLib.VALUE_CONTENT.set(entityOrBlockEntity, this.amount);
        this.amount = CommonStorageLib.VALUE_CONTENT.get(entityOrBlockEntity);
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

    @Override
    public long extract(long amount, boolean simulate) {
        long extracted = Math.min(amount, this.amount);
        if (!simulate) {
            this.amount -= extracted;
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
        public ExtractOnly(long capacity) {
            super(capacity);
        }

        public ExtractOnly(long capacity, ItemStack stack, ItemContext context) {
            super(capacity, stack, context);
        }

        public ExtractOnly(long capacity, Object entityOrBlockEntity) {
            super(capacity, entityOrBlockEntity);
        }

        @Override
        public long insert(long amount, boolean simulate) {
            return 0;
        }

        public long internalInsert(long amount, boolean simulate) {
            return super.insert(amount, simulate);
        }

        @Override
        public boolean allowsInsertion() {
            return false;
        }
    }

    public static class InsertOnly extends SimpleValueStorage {
        public InsertOnly(long capacity) {
            super(capacity);
        }

        public InsertOnly(long capacity, ItemStack stack, ItemContext context) {
            super(capacity, stack, context);
        }

        public InsertOnly(long capacity, Object entityOrBlockEntity) {
            super(capacity, entityOrBlockEntity);
        }

        @Override
        public long extract(long amount, boolean simulate) {
            return 0;
        }

        public long internalExtract(long amount, boolean simulate) {
            return super.extract(amount, simulate);
        }

        @Override
        public boolean allowsExtraction() {
            return false;
        }
    }
}
