package earth.terrarium.botarium.common.item.impl;

import earth.terrarium.botarium.common.item.base.ItemContainer;
import earth.terrarium.botarium.common.item.base.ItemSnapshot;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;

public class SimpleItemSnapshot implements ItemSnapshot {
    NonNullList<ItemStack> stacks;

    public SimpleItemSnapshot(NonNullList<ItemStack> stacks) {
        this.stacks = NonNullList.withSize(stacks.size(), ItemStack.EMPTY);
        for (int i = 0; i < stacks.size(); i++) {
            this.stacks.set(i, stacks.get(i).copy());
        }
    }

    @Override
    public void loadSnapshot(ItemContainer container) {
        if (container instanceof SimpleItemContainer) {
            ((SimpleItemContainer) container).stacks = stacks;
        }
    }
}
