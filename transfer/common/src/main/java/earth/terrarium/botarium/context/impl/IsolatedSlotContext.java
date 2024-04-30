package earth.terrarium.botarium.context.impl;

import earth.terrarium.botarium.context.ItemContext;
import earth.terrarium.botarium.item.base.ItemUnit;
import earth.terrarium.botarium.item.impl.SimpleItemSlot;
import earth.terrarium.botarium.item.impl.noops.NoOpsItemContainer;
import earth.terrarium.botarium.storage.base.CommonStorage;
import net.minecraft.world.item.ItemStack;

public record IsolatedSlotContext(SimpleItemSlot mainSlot) implements ItemContext {
    public IsolatedSlotContext(ItemStack stack) {
        this(new SimpleItemSlot(stack));
    }

    @Override
    public CommonStorage<ItemUnit> outerContainer() {
        return NoOpsItemContainer.NO_OPS;
    }
}
