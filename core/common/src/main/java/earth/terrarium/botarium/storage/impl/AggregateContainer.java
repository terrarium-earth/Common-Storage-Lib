package earth.terrarium.botarium.storage.impl;

import earth.terrarium.botarium.storage.base.CommonStorage;
import earth.terrarium.botarium.storage.base.StorageSlot;
import earth.terrarium.botarium.storage.base.UpdateManager;
import org.jetbrains.annotations.NotNull;

public class AggregateContainer<T> implements CommonStorage<T>, UpdateManager<Object[]> {
    private final CommonStorage<T>[] containers;
    private final int[] indexOffsets;
    private final int slotCount;

    @SafeVarargs
    public AggregateContainer(CommonStorage<T>... containers) {
        this.containers = containers;
        this.indexOffsets = new int[containers.length];
        int index = 0;
        for (int i = 0; i < indexOffsets.length; i++) {
            index += containers[i].getSlotCount();
            indexOffsets[i] = index;
        }
        this.slotCount = index;
    }

    @Override
    public int getSlotCount() {
        return slotCount;
    }

    @Override
    public @NotNull StorageSlot<T> getSlot(int slot) {
        for (int i = 0; i < containers.length; i++) {
            if (slot < indexOffsets[i]) {
                return containers[i].getSlot(slot - indexOffsets[i]);
            }
        }
        throw new IndexOutOfBoundsException();
    }

    @Override
    public long insert(T resource, long amount, boolean simulate) {
        long inserted = 0;
        for (CommonStorage<T> container : containers) {
            inserted += container.insert(resource, amount - inserted, simulate);
            if (inserted >= amount) {
                break;
            }
        }
        return inserted;
    }

    @Override
    public long extract(T resource, long amount, boolean simulate) {
        long extracted = 0;
        for (CommonStorage<T> container : containers) {
            extracted += container.extract(resource, amount - extracted, simulate);
            if (extracted >= amount) {
                break;
            }
        }
        return extracted;
    }

    @Override
    public Object[] createSnapshot() {
        var snapshots = new Object[containers.length];
        for (int i = 0; i < containers.length; i++) {
            if (containers[i] instanceof UpdateManager) {
                snapshots[i] = ((UpdateManager<?>) containers[i]).createSnapshot();
            }
        }
        return snapshots;
    }

    @Override
    public void readSnapshot(Object[] snapshot) {
        for (int i = 0; i < containers.length; i++) {
            if (containers[i] instanceof UpdateManager<?> manager) {
                UpdateManager.forceRead(manager, snapshot[i]);
            }
        }
    }

    @Override
    public void update() {
        UpdateManager.batch(containers);
    }
}
