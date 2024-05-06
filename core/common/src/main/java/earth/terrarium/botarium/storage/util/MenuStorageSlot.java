package earth.terrarium.botarium.storage.util;

import earth.terrarium.botarium.resources.item.ItemResource;
import earth.terrarium.botarium.storage.base.CommonStorage;
import earth.terrarium.botarium.storage.base.StorageSlot;
import earth.terrarium.botarium.storage.base.UpdateManager;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class MenuStorageSlot extends Slot {
    public static final Container EMPTY = new SimpleContainer(0);
    private final StorageSlot<ItemResource> storageSlot;
    private final ModifiableItemSlot modifiable;

    public MenuStorageSlot(CommonStorage<ItemResource> storage, int slotIndex, int x, int y) {
        super(EMPTY, slotIndex, x, y);
        this.storageSlot = storage.getSlot(slotIndex);
        if (!(storageSlot instanceof ModifiableItemSlot slot)) {
            throw new UnsupportedOperationException("Cannot create MenuStorageSlot from non-modifiable slot");
        } else {
            this.modifiable = slot;
        }
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        if (stack.isEmpty())
            return false;
        return storageSlot.isValueValid(ItemResource.of(stack));
    }

    @Override
    public @NotNull ItemStack getItem() {
        return modifiable.toItemStack();
    }

    @Override
    public void set(ItemStack stack) {
        modifiable.set(stack);
    }

    @Override
    public void onQuickCraft(ItemStack oldStackIn, ItemStack newStackIn) {}

    @Override
    public int getMaxStackSize() {
        return (int) this.storageSlot.getLimit();
    }

    @Override
    public int getMaxStackSize(ItemStack stack) {
        return this.modifiable.getMaxAllowed(ItemResource.of(stack));
    }

    @Override
    public boolean mayPickup(Player playerIn) {
        return !this.modifiable.isEmpty();
    }

    @Override
    public @NotNull ItemStack remove(int amount) {
        ItemResource resource = storageSlot.getResource();
        long extract = this.storageSlot.extract(resource, amount, false);
        return extract > 0 ? resource.toItemStack((int) extract) : ItemStack.EMPTY;
    }

    @Override
    public void setChanged() {
        UpdateManager.batch(storageSlot);
    }
}
