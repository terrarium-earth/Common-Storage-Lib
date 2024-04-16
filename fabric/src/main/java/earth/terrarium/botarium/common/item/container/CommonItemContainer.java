package earth.terrarium.botarium.common.item.container;

import earth.terrarium.botarium.common.item.base.ItemContainer;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.SlottedStorage;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public class CommonItemContainer implements ItemContainer<Void> {
    private final Storage<ItemVariant> storage;
    private final int slotCount;

    public CommonItemContainer(Storage<ItemVariant> storage) {
        this.storage = storage;
        this.slotCount = initSlotCount();
    }

    public int initSlotCount() {
        if (storage instanceof SlottedStorage<ItemVariant> slottedStorage) {
            return slottedStorage.getSlotCount();
        } else {
            int count = 0;
            for (StorageView<ItemVariant> ignored : storage) {
                count++;
            }
            return count;
        }
    }

    @Override
    public int getSlotCount() {
        return slotCount;
    }

    @Override
    public @NotNull ItemStack getValueInSlot(int slot) {
        if (storage instanceof SlottedStorage<ItemVariant> slottedStorage) {
            SingleSlotStorage<ItemVariant> storageSlot = slottedStorage.getSlot(slot);
            ItemVariant itemVariant = storageSlot.getResource();
            return itemVariant == null ? ItemStack.EMPTY : itemVariant.toStack((int) storageSlot.getAmount());
        } else {
            Iterator<StorageView<ItemVariant>> iterator = storage.iterator();

            for (int index = 0; index < slot && iterator.hasNext(); index++) {
                iterator.next();
            }

            if (iterator.hasNext()) {
                StorageView<ItemVariant> view = iterator.next();
                ItemVariant itemVariant = view.getResource();
                return itemVariant == null ? ItemStack.EMPTY : itemVariant.toStack((int) view.getAmount());
            } else {
                return ItemStack.EMPTY;
            }
        }
    }

    @Override
    public int getSlotLimit(int slot) {
        if (storage instanceof SlottedStorage<ItemVariant> slottedStorage) {
            SingleSlotStorage<ItemVariant> storageSlot = slottedStorage.getSlot(slot);
            return (int) storageSlot.getCapacity();
        } else {
            Iterator<StorageView<ItemVariant>> iterator = storage.iterator();

            for (int index = 0; index < slot && iterator.hasNext(); index++) {
                iterator.next();
            }

            if (iterator.hasNext()) {
                StorageView<ItemVariant> view = iterator.next();
                return (int) view.getCapacity();
            } else {
                return 0;
            }
        }
    }

    @Override
    public boolean isValueValid(int slot, @NotNull ItemStack value) {
        return true;
    }

    @Override
    public long insertIntoSlot(int slot, @NotNull ItemStack value, boolean simulate) {
        if (storage instanceof SlottedStorage<ItemVariant> slottedStorage) {
            SingleSlotStorage<ItemVariant> storageSlot = slottedStorage.getSlot(slot);
            long inserted;
            try (var transaction = Transaction.openOuter()) {
                inserted = storageSlot.insert(ItemVariant.of(value), value.getCount(), transaction);
                if (!simulate) transaction.commit();
            }
            return inserted;
        }
        return insert(value, simulate);
    }

    @Override
    public @NotNull ItemStack extractFromSlot(int slot, long amount, boolean simulate) {
        if (storage instanceof SlottedStorage<ItemVariant> slottedStorage) {
            SingleSlotStorage<ItemVariant> storageSlot = slottedStorage.getSlot(slot);
            if (storageSlot.isResourceBlank() || storageSlot.getAmount() == 0) return ItemStack.EMPTY;
            long extracted;
            ItemVariant itemVariant;
            try (var transaction = Transaction.openOuter()) {
                extracted = storageSlot.extract(ItemVariant.of(ItemStack.EMPTY), amount, transaction);
                itemVariant = storageSlot.getResource();
                if (!simulate) transaction.commit();
            }
            return itemVariant.toStack((int) extracted);
        }
        return extract(amount, simulate);
    }

    @Override
    public long insert(ItemStack value, boolean simulate) {
        long inserted = 0;
        try (var transaction = Transaction.openOuter()) {
            inserted = storage.insert(ItemVariant.of(value), value.getCount(), transaction);
            if (!simulate) transaction.commit();
        }
        return inserted;
    }

    @Override
    public ItemStack extract(long amount, boolean simulate) {
        long extracted = 0;
        ItemVariant itemVariant = null;
        try (var transaction = Transaction.openOuter()) {
            for (StorageView<ItemVariant> view : storage.nonEmptyViews()) {
                if (view.isResourceBlank() || view.getAmount() == 0) continue;
                extracted = view.extract(view.getResource(), amount, transaction);
                itemVariant = view.getResource();
                if (!simulate) transaction.commit();
                break;
            }
        }
        return itemVariant == null ? ItemStack.EMPTY : itemVariant.toStack((int) extracted);
    }

    @Override
    public Void createSnapshot() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void readSnapshot(Void snapshot) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void update() {
        throw new UnsupportedOperationException();
    }
}
