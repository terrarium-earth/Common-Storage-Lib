package earth.terrarium.botarium.common.item.impl;

import earth.terrarium.botarium.common.item.base.SingleSlotItemContainer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public record PlayerHandContainer(Player player, InteractionHand hand) implements SingleSlotItemContainer {

    @Override
    public long insert(ItemStack value, boolean simulate) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (itemStack.isEmpty()) {
            if (!simulate) player.setItemInHand(hand, value);
            return value.getCount();
        } else if (ItemStack.isSameItemSameComponents(itemStack, value)) {
            int freeSpace = itemStack.getMaxStackSize() - itemStack.getCount();
            int inserted = Math.min(freeSpace, value.getCount());
            if (!simulate) itemStack.grow(inserted);
            return inserted;
        }
        return 0;
    }

    @Override
    public ItemStack extract(long amount, boolean simulate) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (itemStack.isEmpty()) {
            return ItemStack.EMPTY;
        }
        int extracted = (int) Math.min(amount, itemStack.getCount());
        ItemStack result = itemStack.copy();
        result.setCount(extracted);
        if (!simulate) itemStack.shrink(extracted);
        return result;
    }

    @Override
    public ItemStack getValue() {
        return player.getItemInHand(hand);
    }

    @Override
    public int getLimit() {
        return player.getItemInHand(hand).getMaxStackSize();
    }

    @Override
    public boolean isValueValid(ItemStack value) {
        return true;
    }
}
