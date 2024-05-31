package earth.terrarium.common_storage_lib.context.impl;

import earth.terrarium.common_storage_lib.context.ItemContext;
import earth.terrarium.common_storage_lib.resources.item.ItemResource;
import earth.terrarium.common_storage_lib.storage.base.CommonStorage;
import earth.terrarium.common_storage_lib.storage.base.StorageSlot;

public record SimpleItemContext(CommonStorage<ItemResource> outerContainer, StorageSlot<ItemResource> mainSlot) implements ItemContext {
    public static SimpleItemContext of(CommonStorage<ItemResource> container, int slot) {
        return new SimpleItemContext(container, container.get(slot));
    }
}
