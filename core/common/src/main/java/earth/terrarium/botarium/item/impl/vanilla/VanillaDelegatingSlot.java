package earth.terrarium.botarium.item.impl.vanilla;

import earth.terrarium.botarium.resources.item.ItemResource;
import earth.terrarium.botarium.storage.base.StorageSlot;
import earth.terrarium.botarium.storage.base.UpdateManager;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;

public class VanillaDelegatingSlot implements StorageSlot<ItemResource>, UpdateManager<ItemStack> {
    private final int slot;
    private final Container container;
    private final Runnable update;

    public VanillaDelegatingSlot(AbstractVanillaContainer container, int slot) {
        this.container = container.container;
        this.update = container instanceof UpdateManager<?> manager ? manager::update : () -> {};
        this.slot = slot;
    }

    @Override
    public long getLimit() {
        return container.getMaxStackSize(container.getItem(slot));
    }

    @Override
    public boolean isValueValid(ItemResource unit) {
        return container.canPlaceItem(slot, unit.toItemStack());
    }

    @Override
    public ItemResource getUnit() {
        return ItemResource.of(container.getItem(slot));
    }

    @Override
    public long getAmount() {
        return container.getItem(slot).getCount();
    }

    @Override
    public boolean isBlank() {
        return container.getItem(slot).isEmpty();
    }

    @Override
    public long insert(ItemResource unit, long amount, boolean simulate) {
        ItemStack stack = container.getItem(slot);
        if (unit.test(stack) || stack.isEmpty()) {
            if (stack.isEmpty()) {
                ItemStack inserted = unit.toItemStack(Math.min((int) amount, (int) getLimit()));
                if (!simulate) {
                    container.setItem(slot, inserted);
                }
                return inserted.getCount();
            } else {
                ItemStack inserted = unit.toItemStack(Math.min((int) amount + stack.getCount(), (int) getLimit()));
                if (!simulate) {
                    container.setItem(slot, inserted);
                }
                return inserted.getCount() - stack.getCount();
            }
        }
        return 0;
    }

    @Override
    public long extract(ItemResource unit, long amount, boolean simulate) {
        ItemStack stack = container.getItem(slot).copy();
        if (unit.test(stack)) {
            ItemStack extracted = stack.split((int) amount);
            if (!simulate) {
                if (stack.isEmpty()) {
                    container.setItem(slot, ItemStack.EMPTY);
                } else {
                    container.setItem(slot, stack);
                }
            }
            return extracted.getCount();
        }
        return 0;
    }

    @Override
    public ItemStack createSnapshot() {
        return container.getItem(slot).copy();
    }

    @Override
    public void readSnapshot(ItemStack snapshot) {
        container.setItem(slot, snapshot);
    }

    @Override
    public void update() {
        update.run();
    }
}