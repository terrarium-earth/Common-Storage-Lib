package earth.terrarium.botarium.common.storage.fabric;

import earth.terrarium.botarium.common.storage.base.SingleSlotContainer;
import earth.terrarium.botarium.common.transfer.base.TransferUnit;
import earth.terrarium.botarium.common.transfer.base.UnitHolder;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.storage.TransferVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.fabricmc.fabric.api.transfer.v1.transaction.base.SnapshotParticipant;
import net.minecraft.core.component.DataComponentPatch;

public class FabricWrappedSlot<T, U extends TransferUnit<T>, V extends TransferVariant<T>, H extends UnitHolder<U>, C extends SingleSlotContainer<U, H>> extends SnapshotParticipant<DataComponentPatch> implements SingleSlotStorage<V> {
    private final C container;
    private final FabricWrappedContainer<T, U, V, ?, ?> wrappedContainer;

    public FabricWrappedSlot(C container, FabricWrappedContainer<T, U, V, ?, ?> wrappedContainer) {
        this.container = container;
        this.wrappedContainer = wrappedContainer;
    }

    @Override
    public long insert(V resource, long maxAmount, TransactionContext transaction) {
        U holder = wrappedContainer.fromVariant(resource);
        updateSnapshots(transaction);
        return container.insert(holder, maxAmount, false);
    }

    @Override
    public long extract(V resource, long maxAmount, TransactionContext transaction) {
        U holder = wrappedContainer.fromVariant(resource);
        updateSnapshots(transaction);
        H extracted = container.extract(u -> u.matches(holder), maxAmount, false);
        return extracted.getHeldAmount();
    }

    @Override
    public boolean isResourceBlank() {
        return container.getUnit().isBlank();
    }

    @Override
    public V getResource() {
        return wrappedContainer.toVariant(container.getUnit());
    }

    @Override
    public long getAmount() {
        return container.getHeldAmount();
    }

    @Override
    public long getCapacity() {
        return container.getLimit();
    }

    @Override
    protected DataComponentPatch createSnapshot() {
        return container.createSnapshot();
    }

    @Override
    protected void readSnapshot(DataComponentPatch snapshot) {
        container.readSnapshot(snapshot);
    }

    @Override
    protected void onFinalCommit() {
        container.update();
    }
}
