package earth.terrarium.botarium.item.wrappers;

import earth.terrarium.botarium.item.base.ItemUnit;
import earth.terrarium.botarium.storage.base.CommonStorage;
import earth.terrarium.botarium.storage.base.StorageSlot;
import earth.terrarium.botarium.storage.base.UpdateManager;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;

public record NeoItemHandler(CommonStorage<ItemUnit> container) implements IItemHandler {
    @Override
    public int getSlots() {
        return container.getSlotCount();
    }

    @Override
    public ItemStack getStackInSlot(int i) {
        StorageSlot<ItemUnit> slot = container.getSlot(i);
        return slot.getUnit().toItemStack((int) slot.getAmount());
    }

    @Override
    public ItemStack insertItem(int i, ItemStack arg, boolean bl) {
        long inserted = container.getSlot(i).insert(ItemUnit.of(arg), arg.getCount(), bl);
        if (!bl) {
            UpdateManager.batch(container);
        }
        return arg.getCount() == inserted ? ItemStack.EMPTY : arg.copyWithCount((int) (arg.getCount() - inserted));
    }

    @Override
    public ItemStack extractItem(int i, int j, boolean bl) {
        long extracted = container.getSlot(i).extract(ItemUnit.of(getStackInSlot(i)), j, bl);
        if (!bl) {
            UpdateManager.batch(container);
        }
        return getStackInSlot(i).copyWithCount((int) extracted);
    }

    @Override
    public int getSlotLimit(int i) {
        return (int) container.getSlot(i).getLimit();
    }

    @Override
    public boolean isItemValid(int i, ItemStack arg) {
        return container.getSlot(i).isValueValid(ItemUnit.of(arg));
    }
}
