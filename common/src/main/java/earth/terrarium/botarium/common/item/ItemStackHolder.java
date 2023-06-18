package earth.terrarium.botarium.common.item;

import net.minecraft.world.item.ItemStack;

public class ItemStackHolder {
    private ItemStack stack;
    private boolean isDirty;

    public ItemStackHolder(ItemStack stack) {
        this.stack = stack;
    }

    public ItemStack getStack() {
        return stack;
    }

    public void setStack(ItemStack stack) {
        if (!ItemStack.matches(stack, this.stack)) {
            this.stack = stack;
            isDirty = true;
        }
    }

    public boolean isDirty() {
        return isDirty;
    }

    public ItemStackHolder copy() {
        return new ItemStackHolder(stack.copy());
    }
}
