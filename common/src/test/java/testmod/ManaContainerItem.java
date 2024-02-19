package testmod;

import earth.terrarium.botarium.common.generic.util.AmountBasedContainer;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;

public class ManaContainerItem extends ManaContainer {
    private final ItemStack stack;

    public ManaContainerItem(ItemStack stack) {
        this.deserialize(stack.getOrCreateTag());
        this.stack = stack;
    }

    @Override
    public long insert(long amount, boolean simulate) {
        long insert = super.insert(amount, simulate);
        if (!simulate) this.serialize(stack.getOrCreateTag());
        return insert;
    }

    @Override
    public long extract(long amount, boolean simulate) {
        long extract = super.extract(amount, simulate);
        if (!simulate) this.serialize(stack.getOrCreateTag());
        return extract;
    }

    @Override
    public void clearContent() {
        super.clearContent();
        this.serialize(stack.getOrCreateTag());
    }
}
