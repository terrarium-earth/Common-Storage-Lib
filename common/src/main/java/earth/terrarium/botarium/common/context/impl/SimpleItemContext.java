package earth.terrarium.botarium.common.context.impl;

import earth.terrarium.botarium.common.context.ItemContext;
import earth.terrarium.botarium.common.storage.base.UnitContainer;
import earth.terrarium.botarium.common.storage.base.UnitSlot;
import earth.terrarium.botarium.common.transfer.impl.ItemUnit;

public record SimpleItemContext(UnitContainer<ItemUnit> outerContainer, UnitSlot<ItemUnit> mainSlot) implements ItemContext {
    public static SimpleItemContext of(UnitContainer<ItemUnit> container, int slot) {
        return new SimpleItemContext(container, container.getSlot(slot));
    }
}
