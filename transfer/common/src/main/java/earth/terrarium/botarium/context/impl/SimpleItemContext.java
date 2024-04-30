package earth.terrarium.botarium.context.impl;

import earth.terrarium.botarium.context.ItemContext;
import earth.terrarium.botarium.resource.item.ItemResource;
import earth.terrarium.botarium.storage.base.CommonStorage;
import earth.terrarium.botarium.storage.base.StorageSlot;

public record SimpleItemContext(CommonStorage<ItemResource> outerContainer, StorageSlot<ItemResource> mainSlot) implements ItemContext {
    public static SimpleItemContext of(CommonStorage<ItemResource> container, int slot) {
        return new SimpleItemContext(container, container.getSlot(slot));
    }
}
