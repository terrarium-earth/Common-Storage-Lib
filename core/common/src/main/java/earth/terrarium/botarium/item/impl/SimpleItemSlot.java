package earth.terrarium.botarium.item.impl;

import earth.terrarium.botarium.resources.ResourceStack;
import earth.terrarium.botarium.resources.item.ItemResource;
import earth.terrarium.botarium.storage.util.ModifiableItemSlot;
import earth.terrarium.botarium.storage.base.StorageSlot;
import earth.terrarium.botarium.storage.base.UpdateManager;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.function.Predicate;

public class SimpleItemSlot implements StorageSlot<ItemResource>, ModifiableItemSlot, UpdateManager<ResourceStack<ItemResource>> {
    private final Runnable update;
    private ItemResource resource;
    private long amount;

    public SimpleItemSlot(Runnable update) {
        this.resource = ItemResource.BLANK;
        this.amount = getAmount();
        this.update = update;
    }

    public SimpleItemSlot(ItemStack stack) {
        this.resource = ItemResource.of(stack);
        this.amount = stack.getCount();
        this.update = () -> {};
    }

    @Override
    public long getLimit() {
        return resource.isBlank() ? Item.ABSOLUTE_MAX_STACK_SIZE : resource.getCachedStack().getMaxStackSize();
    }

    @Override
    public boolean isValueValid(ItemResource resource) {
        return true;
    }

    @Override
    public ItemResource getResource() {
        return resource;
    }

    @Override
    public long getAmount() {
        return amount;
    }

    @Override
    public boolean isBlank() {
        return resource.isBlank();
    }

    public void set(ItemResource resource, long amount) {
        this.resource = resource;
        this.amount = amount;
    }

    public void set(ResourceStack<ItemResource> data) {
        this.resource = data.resource();
        this.amount = data.amount();
    }

    @Override
    public long insert(ItemResource resource, long amount, boolean simulate) {
        if (!isValueValid(resource)) return 0;
        if (this.resource.isBlank()) {
            long inserted = Math.min(amount, resource.getCachedStack().getMaxStackSize());
            if (!simulate) {
                this.resource = resource;
                this.amount = inserted;
            }
            return inserted;
        } else if (this.resource.test(resource)) {
            long inserted = Math.min(amount, getLimit() - this.amount);
            if (!simulate) {
                this.amount += inserted;
            }
            return inserted;
        }
        return 0;
    }

    @Override
    public long extract(ItemResource resource, long amount, boolean simulate) {
        if (this.resource.test(resource)) {
            long extracted = Math.min(amount, this.amount);
            if (!simulate) {
                this.amount -= extracted;
                if (this.amount == 0) {
                    this.resource = ItemResource.BLANK;
                }
            }
            return extracted;
        }
        return 0;
    }

    @Override
    public ResourceStack<ItemResource> createSnapshot() {
        return new ResourceStack<>(resource, amount);
    }

    @Override
    public void readSnapshot(ResourceStack<ItemResource> snapshot) {
        this.resource = snapshot.resource();
        this.amount = snapshot.amount();
    }

    @Override
    public void update() {
        update.run();
    }

    @Override
    public void setAmount(long amount) {
        this.amount = amount;
    }

    @Override
    public void setResource(ItemResource resource) {
        this.resource = resource;
    }

    @Override
    public ItemStack toItemStack() {
        return resource.toItemStack((int) amount);
    }

    @Override
    public int getMaxAllowed(ItemResource resource) {
        return resource.getCachedStack().getMaxStackSize();
    }

    @Override
    public boolean isEmpty() {
        return resource.isBlank() || amount == 0;
    }

    public static class Filtered extends SimpleItemSlot {
        private final Predicate<ItemResource> filter;

        public Filtered(Runnable update, Predicate<ItemResource> filter) {
            super(update);
            this.filter = filter;
        }

        @Override
        public boolean isValueValid(ItemResource resource) {
            return filter.test(resource);
        }
    }
}