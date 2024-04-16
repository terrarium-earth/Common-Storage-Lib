package earth.terrarium.botarium.common.item.container;

import earth.terrarium.botarium.common.item.base.ItemContainer;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.fabricmc.fabric.api.transfer.v1.transaction.base.SnapshotParticipant;

public class FabricSingleSlotWrapper<T> extends SnapshotParticipant<T> implements SingleSlotStorage<ItemVariant> {
    private final ItemContainer<T> container;
    private final int slot;

    public FabricSingleSlotWrapper(ItemContainer<T> container, int slot) {
        this.container = container;
        this.slot = slot;
    }

    @Override
    public long insert(ItemVariant resource, long maxAmount, TransactionContext transaction) {
        updateSnapshots(transaction);
        return container.insertIntoSlot(slot, resource.toStack((int) maxAmount), false);
    }

    @Override
    public long extract(ItemVariant resource, long maxAmount, TransactionContext transaction) {
        updateSnapshots(transaction);
        if (resource.matches(container.getValueInSlot(slot))) {
            return container.extractFromSlot(slot, (int) maxAmount, false).getCount();
        } else {
            return 0;
        }
    }

    @Override
    public boolean isResourceBlank() {
        return container.getValueInSlot(slot).isEmpty();
    }

    @Override
    public ItemVariant getResource() {
        return ItemVariant.of(container.getValueInSlot(slot));
    }

    @Override
    public long getAmount() {
        return container.getValueInSlot(slot).getCount();
    }

    @Override
    public long getCapacity() {
        return container.getSlotLimit(slot);
    }

    @Override
    protected T createSnapshot() {
        return container.createSnapshot();
    }

    @Override
    protected void readSnapshot(T snapshot) {
        container.readSnapshot(snapshot);
    }
}
