package earth.terrarium.botarium.common.item.impl;

import earth.terrarium.botarium.common.storage.impl.SimpleSlot;
import earth.terrarium.botarium.common.transfer.impl.ItemUnit;
import net.minecraft.world.item.ItemStack;

public class SimpleItemSlot extends SimpleSlot<ItemUnit> {
    public SimpleItemSlot(long slotLimit, Runnable onUpdate) {
        super(ItemUnit.BLANK, slotLimit, onUpdate);
    }

    public ItemStack getStack() {
        return getUnit().toStack((int) getAmount());
    }
}
