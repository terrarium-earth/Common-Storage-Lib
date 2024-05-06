package earth.terrarium.botarium.item.wrappers;

import earth.terrarium.botarium.resources.item.ItemResource;
import earth.terrarium.botarium.storage.base.CommonStorage;
import earth.terrarium.botarium.storage.base.StorageSlot;
import earth.terrarium.botarium.storage.util.TransferUtil;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

public record CommonItemContainer(IItemHandler handler) implements CommonStorage<ItemResource> {
    @Override
    public int getSlotCount() {
        return handler.getSlots();
    }

    @Override
    public @NotNull StorageSlot<ItemResource> getSlot(int slot) {
        return new DelegatingItemSlot(handler, slot);
    }

    @Override
    public long insert(ItemResource resource, long amount, boolean simulate) {
        return TransferUtil.insertSlots(this, resource, amount, simulate);
    }

    @Override
    public long extract(ItemResource resource, long amount, boolean simulate) {
        return TransferUtil.extractSlots(this, resource, amount, simulate);
    }

    public record DelegatingItemSlot(IItemHandler handler, int slot) implements StorageSlot<ItemResource> {

        @Override
        public long getLimit() {
            return handler.getSlotLimit(slot);
        }

        @Override
        public boolean isValueValid(ItemResource resource) {
            return handler.isItemValid(slot, resource.toItemStack());
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
        public long insert(ItemResource resource, long amount, boolean simulate) {
            ItemStack leftover = handler.insertItem(slot, resource.toItemStack((int) amount), simulate);
            return amount - leftover.getCount();
        }

        @Override
        public long extract(ItemResource resource, long amount, boolean simulate) {
            if (!resource.test(handler.getStackInSlot(slot))) {
                return 0;
            }
            ItemStack extracted = handler.extractItem(slot, (int) amount, simulate);
            return extracted.getCount();
        }
    }
}
