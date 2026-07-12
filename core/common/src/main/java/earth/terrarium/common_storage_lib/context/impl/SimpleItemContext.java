package earth.terrarium.common_storage_lib.context.impl;

import com.mojang.datafixers.util.Pair;
import earth.terrarium.common_storage_lib.context.ItemContext;
import earth.terrarium.common_storage_lib.resources.item.ItemResource;
import earth.terrarium.common_storage_lib.storage.base.CommonStorage;
import earth.terrarium.common_storage_lib.storage.base.StorageSlot;
import earth.terrarium.common_storage_lib.storage.base.UpdateManager;

public record SimpleItemContext(CommonStorage<ItemResource> outerContainer, StorageSlot<ItemResource> mainSlot) implements ItemContext, UpdateManager<Pair<Object, Object>> {
    public static SimpleItemContext of(CommonStorage<ItemResource> container, int slot) {
        return new SimpleItemContext(container, container.get(slot));
    }

    @Override
    public Pair<Object, Object> createSnapshot() {
        Object outerSnapshot = null;
        if (outerContainer instanceof UpdateManager<?> manager) {
            outerSnapshot = manager.createSnapshot();
        }

        Object mainSnapshot = null;
        if (mainSlot instanceof UpdateManager<?> manager) {
            mainSnapshot = manager.createSnapshot();
        }
        return Pair.of(outerSnapshot, mainSnapshot);
    }

    @Override
    public void readSnapshot(Pair<Object, Object> snapshot) {
        Object outerSnapshot = snapshot.getFirst();
        if (outerContainer instanceof UpdateManager<?> manager) {
            UpdateManager.forceRead(manager, outerSnapshot);
        }

        Object mainSnapshot = snapshot.getSecond();
        if (mainSlot instanceof UpdateManager<?> manager) {
            UpdateManager.forceRead(manager, mainSnapshot);
        }
    }

    @Override
    public void update() {
        if (outerContainer instanceof UpdateManager<?> manager) {
            manager.update();
        }

        if (mainSlot instanceof UpdateManager<?> manager) {
            manager.update();
        }
    }
}
