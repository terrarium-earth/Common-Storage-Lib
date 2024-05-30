package earth.terrarium.botarium.item.wrappers;

import earth.terrarium.botarium.resources.item.ItemResource;
import earth.terrarium.botarium.storage.base.CommonStorage;
import earth.terrarium.botarium.storage.base.StorageSlot;
import earth.terrarium.botarium.storage.base.UpdateManager;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;

public record NeoItemHandler(CommonStorage<ItemResource> container) implements IItemHandler {
    @Override
    public int getSlots() {
        return container.size();
    }

    @Override
    public ItemStack getStackInSlot(int i) {
        StorageSlot<ItemResource> slot = container.getSlot(i);
        return slot.getResource().toStack((int) slot.getAmount());
    }

    @Override
    public ItemStack insertItem(int i, ItemStack arg, boolean bl) {
        long inserted = container.getSlot(i).insert(ItemResource.of(arg), arg.getCount(), bl);
        if (!bl) {
            UpdateManager.batch(container);
        }
        return arg.getCount() == inserted ? ItemStack.EMPTY : arg.copyWithCount((int) (arg.getCount() - inserted));
    }

    @Override
    public ItemStack extractItem(int i, int j, boolean bl) {
        long extracted = container.getSlot(i).extract(ItemResource.of(getStackInSlot(i)), j, bl);
        if (!bl) {
            UpdateManager.batch(container);
        }
        return getStackInSlot(i).copyWithCount((int) extracted);
    }

    @Override
    public int getSlotLimit(int i) {
        StorageSlot<ItemResource> slot = container.getSlot(i);
        return (int) slot.getLimit(slot.getResource());
    }

    @Override
    public boolean isItemValid(int i, ItemStack arg) {
        return container.getSlot(i).isResourceValid(ItemResource.of(arg));
    }
}
