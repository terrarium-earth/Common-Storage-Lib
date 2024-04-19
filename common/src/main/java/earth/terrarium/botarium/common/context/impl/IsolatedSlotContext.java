package earth.terrarium.botarium.common.context.impl;

import earth.terrarium.botarium.common.context.ItemContext;
import earth.terrarium.botarium.common.item.impl.StackSlot;
import earth.terrarium.botarium.common.item.impl.noops.NoOpsItemContainer;
import earth.terrarium.botarium.common.storage.base.UnitContainer;
import earth.terrarium.botarium.common.storage.base.UnitSlot;
import earth.terrarium.botarium.common.transfer.impl.ItemUnit;
import net.minecraft.world.item.ItemStack;

public record IsolatedSlotContext(StackSlot mainSlot) implements ItemContext {
    public IsolatedSlotContext(ItemStack stack) {
        this(new StackSlot(stack));
    }

    @Override
    public UnitContainer<ItemUnit> outerContainer() {
        return NoOpsItemContainer.NO_OPS;
    }
}
