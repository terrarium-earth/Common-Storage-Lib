package earth.terrarium.botarium.common.storage.context;

import earth.terrarium.botarium.common.context.ItemContext;
import earth.terrarium.botarium.common.item.base.ItemContainer;
import earth.terrarium.botarium.common.storage.base.SingleSlotContainer;
import earth.terrarium.botarium.common.transfer.impl.ItemHolder;
import earth.terrarium.botarium.common.transfer.impl.ItemUnit;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;

public record CommonItemContext(ContainerItemContext context) implements ItemContext {
    @Override
    public ItemContainer outerContainer() {
        return new ContextItemContainer(context.getAdditionalSlots());
    }

    @Override
    public SingleSlotContainer<ItemUnit, ItemHolder> mainSlot() {
        return null;
    }
}
