package earth.terrarium.botarium.common.item.impl.vanilla;

import earth.terrarium.botarium.common.storage.base.UnitSlot;
import earth.terrarium.botarium.common.storage.util.UpdateManager;
import earth.terrarium.botarium.common.transfer.impl.ItemUnit;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;

public class VanillaDelegatingSlot implements UnitSlot<ItemUnit>, UpdateManager<ItemStack> {
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
    public boolean isValueValid(ItemUnit unit) {
        return container.canPlaceItem(slot, unit.toStack());
    }

    @Override
    public ItemUnit getUnit() {
        return ItemUnit.of(container.getItem(slot));
    }

    @Override
    public long getAmount() {
        return container.getItem(slot).getCount();
    }

    @Override
    public long insert(ItemUnit unit, long amount, boolean simulate) {
        ItemStack stack = container.getItem(slot);
        if (unit.matches(stack) || stack.isEmpty()) {
            if (stack.isEmpty()) {
                ItemStack inserted = unit.toStack(Math.min((int) amount, (int) getLimit()));
                if (!simulate) {
                    container.setItem(slot, inserted);
                }
                return inserted.getCount();
            } else {
                ItemStack inserted = unit.toStack(Math.min((int) amount + stack.getCount(), (int) getLimit()));
                if (!simulate) {
                    container.setItem(slot, inserted);
                }
                return inserted.getCount() - stack.getCount();
            }
        }
        return 0;
    }

    @Override
    public long extract(ItemUnit unit, long amount, boolean simulate) {
        ItemStack stack = container.getItem(slot).copy();
        if (unit.matches(stack)) {
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