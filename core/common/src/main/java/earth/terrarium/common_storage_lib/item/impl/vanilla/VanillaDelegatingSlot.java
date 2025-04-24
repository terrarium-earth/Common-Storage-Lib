package earth.terrarium.common_storage_lib.item.impl.vanilla;

import earth.terrarium.common_storage_lib.resources.ResourceStack;
import earth.terrarium.common_storage_lib.resources.item.ItemResource;
import earth.terrarium.common_storage_lib.storage.base.StorageSlot;
import earth.terrarium.common_storage_lib.storage.base.UpdateManager;
import earth.terrarium.common_storage_lib.storage.util.ModifiableItemSlot;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;

public class VanillaDelegatingSlot implements StorageSlot<ItemResource>, ModifiableItemSlot, UpdateManager<ItemStack> {
    private final int slot;
    private final Container container;
    private final Runnable update;

    public VanillaDelegatingSlot(AbstractVanillaContainer container, int slot) {
        this.container = container.container;
        this.update = container instanceof UpdateManager<?> manager ? manager::update : () -> {};
        this.slot = slot;
    }

    @Override
    public long getLimit(ItemResource resource) {
        return container.getMaxStackSize(resource.getCachedStack());
    }

    @Override
    public boolean isResourceValid(ItemResource resource) {
        return container.canPlaceItem(slot, resource.toStack());
    }

    @Override
    public ItemResource getResource() {
        return ItemResource.of(container.getItem(slot));
    }

    @Override
    public long getAmount() {
        return container.getItem(slot).getCount();
    }

    public void set(ItemResource resource, long amount) {
        container.setItem(slot, resource.toStack((int) amount));
    }

    public void set(ResourceStack<ItemResource> data) {
        set(data.resource(), data.amount());
    }

    @Override
    public long insert(ItemResource resource, long amount, boolean simulate) {
        ItemStack stack = container.getItem(slot);
        if (resource.test(stack) || stack.isEmpty()) {
            if (stack.isEmpty()) {
                ItemStack inserted = resource.toStack(Math.min((int) amount, (int) getLimit(resource)));
                if (!simulate) {
                    container.setItem(slot, inserted);
                }
                return inserted.getCount();
            } else {
                ItemStack inserted = resource.toStack(Math.min((int) amount + stack.getCount(), (int) getLimit(resource)));
                if (!simulate) {
                    container.setItem(slot, inserted);
                }
                return inserted.getCount() - stack.getCount();
            }
        }
        return 0;
    }

    @Override
    public long extract(ItemResource resource, long amount, boolean simulate) {
        ItemStack stack = container.getItem(slot).copy();
        if (resource.test(stack)) {
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

    @Override
    public void setAmount(long amount) {
        var item = container.getItem(slot);
        item.setCount((int) amount);
    }

    @Override
    public void setResource(ItemResource resource) {
        container.setItem(slot, resource.toStack((int) getAmount()));
    }

    @Override
    public ItemStack toItemStack() {
        return container.getItem(slot);
    }

    @Override
    public int getMaxAllowed(ItemResource resource) {
        return container.getMaxStackSize(resource.getCachedStack());
    }

    @Override
    public boolean isEmpty() {
        return container.getItem(slot).isEmpty();
    }
}