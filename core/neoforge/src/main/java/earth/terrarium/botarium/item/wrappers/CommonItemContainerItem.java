package earth.terrarium.botarium.item.wrappers;

import earth.terrarium.botarium.context.ItemContext;
import earth.terrarium.botarium.resources.item.ItemResource;
import earth.terrarium.botarium.storage.base.CommonStorage;
import earth.terrarium.botarium.storage.base.StorageSlot;
import earth.terrarium.botarium.storage.util.TransferUtil;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

public record CommonItemContainerItem(IItemHandler handler, ItemStack stack, ItemContext context) implements CommonStorage<ItemResource> {
    @Override
    public int getSlotCount() {
        return handler.getSlots();
    }

    @Override
    public @NotNull StorageSlot<ItemResource> getSlot(int slot) {
        return new DelegatingItemSlot(handler, slot, this::updateContext);
    }

    public void updateContext() {
        if (!context.getUnit().test(stack)) {
            context.exchange(ItemResource.of(stack), context.getAmount(), false);
        }
        if (context.getAmount() != stack.getCount()) {
            TransferUtil.equalize(context.mainSlot(), stack.getCount());
        }
    }

    @Override
    public long insert(ItemResource unit, long amount, boolean simulate) {
        return TransferUtil.insertSlots(this, unit, amount, simulate);
    }

    @Override
    public long extract(ItemResource unit, long amount, boolean simulate) {
        return TransferUtil.extractSlots(this, unit, amount, simulate);
    }

    public record DelegatingItemSlot(IItemHandler handler, int slot, Runnable runnable) implements StorageSlot<ItemResource> {

        @Override
        public long getLimit() {
            return handler.getSlotLimit(slot);
        }

        @Override
        public boolean isValueValid(ItemResource unit) {
            return handler.isItemValid(slot, unit.toItemStack());
        }

        @Override
        public ItemResource getResource() {
            return ItemResource.of(handler.getStackInSlot(slot));
        }

        @Override
        public long getAmount() {
            return handler.getStackInSlot(slot).getCount();
        }

        @Override
        public boolean isBlank() {
            return handler.getStackInSlot(slot).isEmpty();
        }

        @Override
        public long insert(ItemResource unit, long amount, boolean simulate) {
            ItemStack leftover = handler.insertItem(slot, unit.toItemStack((int) amount), simulate);
            runnable.run();
            return amount - leftover.getCount();
        }

        @Override
        public long extract(ItemResource unit, long amount, boolean simulate) {
            if (!unit.test(handler.getStackInSlot(slot))) {
                return 0;
            }
            ItemStack extracted = handler.extractItem(slot, (int) amount, simulate);
            runnable.run();
            return extracted.getCount();
        }
    }
}
