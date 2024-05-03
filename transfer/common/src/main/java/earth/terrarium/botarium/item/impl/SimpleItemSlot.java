package earth.terrarium.botarium.item.impl;

import earth.terrarium.botarium.resources.ResourceStack;
import earth.terrarium.botarium.resources.item.ItemResource;
import earth.terrarium.botarium.storage.base.StorageSlot;
import earth.terrarium.botarium.storage.base.UpdateManager;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.function.Predicate;

public class SimpleItemSlot implements StorageSlot<ItemResource>, UpdateManager<ResourceStack<ItemResource>> {
    private final Runnable update;
    private ItemResource unit;
    private long amount;

    public SimpleItemSlot(Runnable update) {
        this.unit = ItemResource.BLANK;
        this.amount = getAmount();
        this.update = update;
    }

    public SimpleItemSlot(ItemStack stack) {
        this.unit = ItemResource.of(stack);
        this.amount = stack.getCount();
        this.update = () -> {};
    }

    @Override
    public long getLimit() {
        return unit.isBlank() ? Item.ABSOLUTE_MAX_STACK_SIZE : unit.getCachedStack().getMaxStackSize();
    }

    @Override
    public boolean isValueValid(ItemResource unit) {
        return true;
    }

    @Override
    public ItemResource getUnit() {
        return unit;
    }

    @Override
    public long getAmount() {
        return amount;
    }

    @Override
    public boolean isBlank() {
        return unit.isBlank();
    }

    public void set(ItemResource unit, long amount) {
        this.unit = unit;
        this.amount = amount;
    }

    public void set(ItemStack stack) {
        this.unit = ItemResource.of(stack);
        this.amount = stack.getCount();
    }

    public void set(ResourceStack<ItemResource> data) {
        this.unit = data.unit();
        this.amount = data.amount();
    }

    @Override
    public long insert(ItemResource unit, long amount, boolean simulate) {
        if (!isValueValid(unit)) return 0;
        if (this.unit.isBlank()) {
            long inserted = Math.min(amount, unit.getCachedStack().getMaxStackSize());
            if (!simulate) {
                this.unit = unit;
                this.amount = inserted;
            }
            return inserted;
        } else if (this.unit.test(unit)) {
            long inserted = Math.min(amount, getLimit() - this.amount);
            if (!simulate) {
                this.amount += inserted;
            }
            return inserted;
        }
        return 0;
    }

    @Override
    public long extract(ItemResource unit, long amount, boolean simulate) {
        if (this.unit.test(unit)) {
            long extracted = Math.min(amount, this.amount);
            if (!simulate) {
                this.amount -= extracted;
                if (this.amount == 0) {
                    this.unit = ItemResource.BLANK;
                }
            }
            return extracted;
        }
        return 0;
    }

    @Override
    public ResourceStack<ItemResource> createSnapshot() {
        return new ResourceStack<>(unit, amount);
    }

    @Override
    public void readSnapshot(ResourceStack<ItemResource> snapshot) {
        this.unit = snapshot.unit();
        this.amount = snapshot.amount();
    }

    @Override
    public void update() {
        update.run();
    }

    public static class Filtered extends SimpleItemSlot {
        private final Predicate<ItemResource> filter;

        public Filtered(Runnable update, Predicate<ItemResource> filter) {
            super(update);
            this.filter = filter;
        }

        @Override
        public boolean isValueValid(ItemResource unit) {
            return filter.test(unit);
        }
    }
}