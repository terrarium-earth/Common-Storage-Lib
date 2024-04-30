package earth.terrarium.botarium.context.impl;

import earth.terrarium.botarium.context.ItemContext;
import earth.terrarium.botarium.item.base.ItemUnit;
import earth.terrarium.botarium.storage.base.CommonStorage;
import earth.terrarium.botarium.storage.base.StorageSlot;

public record SimpleItemContext(CommonStorage<ItemUnit> outerContainer, StorageSlot<ItemUnit> mainSlot) implements ItemContext {
    public static SimpleItemContext of(CommonStorage<ItemUnit> container, int slot) {
        return new SimpleItemContext(container, container.getSlot(slot));
    }
}
