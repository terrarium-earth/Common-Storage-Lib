package earth.terrarium.botarium.common.transfer.impl;

import earth.terrarium.botarium.common.transfer.base.UnitHolder;
import net.minecraft.world.item.ItemStack;

public record ItemHolder(ItemStack stack) implements UnitHolder<ItemUnit> {
    public static ItemHolder of(ItemStack stack) {
        return new ItemHolder(stack);
    }

    public static ItemHolder of(ItemUnit unit) {
        return new ItemHolder(unit.toStack());
    }

    public static ItemHolder of(ItemUnit stack, int amount) {
        return new ItemHolder(stack.toStack(amount));
    }

    public static final ItemHolder EMPTY = new ItemHolder(ItemStack.EMPTY);

    @Override
    public ItemUnit getUnit() {
        return ItemUnit.of(stack);
    }

    @Override
    public long getHeldAmount() {
        return stack.getCount();
    }
}
