package earth.terrarium.botarium.common.energy.wrappers;

import earth.terrarium.botarium.common.context.ItemContext;
import earth.terrarium.botarium.common.storage.util.TransferUtil;
import earth.terrarium.botarium.common.transfer.impl.ItemUnit;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.energy.IEnergyStorage;

public record CommonItemEnergyContainer(IEnergyStorage storage, ItemStack stack, ItemContext context) implements AbstractCommonEnergyContainer {
    @Override
    public long insert(long amount, boolean simulate) {
        long inserted = AbstractCommonEnergyContainer.super.insert(amount, simulate);
        if (!simulate) updateContext();
        return inserted;
    }

    @Override
    public long extract(long amount, boolean simulate) {
        long extract = AbstractCommonEnergyContainer.super.extract(amount, simulate);
        if (!simulate) updateContext();
        return extract;
    }

    public void updateContext() {
        if (!context.getUnit().matches(stack)) {
            context.exchange(ItemUnit.of(stack), context.getAmount(), false);
        }
        if (context.getAmount() != stack.getCount()) {
            TransferUtil.equalize(context.mainSlot(), stack.getCount());
        }
    }
}
