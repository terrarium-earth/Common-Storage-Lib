package earth.terrarium.common_storage_lib.energy.impl;

import earth.terrarium.common_storage_lib.context.ItemContext;
import earth.terrarium.common_storage_lib.data.DataManager;
import earth.terrarium.common_storage_lib.storage.base.ValueStorage;
import earth.terrarium.common_storage_lib.storage.base.UpdateManager;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponentType;

public class SimpleValueStorage implements ValueStorage, UpdateManager<Long> {
    private final long capacity;
    private final Runnable onUpdate;
    private final Runnable save;
    private long amount;

    public SimpleValueStorage(long capacity) {
        this.capacity = capacity;
        this.onUpdate = () -> {};
        this.save = () -> {};
    }

    @SuppressWarnings("DataFlowIssue")
    public SimpleValueStorage(ItemContext context, DataComponentType<Long> componentType, long capacity) {
        this.capacity = capacity;
        this.onUpdate = context::updateAll;
        this.save = () -> context.set(componentType, this.amount);
        if (context.getResource().has(componentType)) {
            this.amount = context.getResource().get(componentType);
        }
    }

    public SimpleValueStorage(Object entityOrBlockEntity, DataManager<Long> dataManager, long capacity) {
        this.capacity = capacity;
        this.onUpdate = () -> dataManager.set(entityOrBlockEntity, this.amount);
        save = () -> {};
        this.amount = dataManager.get(entityOrBlockEntity);
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
            save.run();
        }
        return inserted;
    }

    @Override
    public long extract(long amount, boolean simulate) {
        long extracted = Math.min(amount, this.amount);
        if (!simulate) {
            this.amount -= extracted;
            save.run();
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
}
