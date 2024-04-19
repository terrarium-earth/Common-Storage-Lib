package earth.terrarium.botarium.common.storage.fabric;

import earth.terrarium.botarium.common.storage.base.LongContainer;
import earth.terrarium.botarium.common.storage.util.UpdateManager;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.fabricmc.fabric.api.transfer.v1.transaction.base.SnapshotParticipant;
import team.reborn.energy.api.EnergyStorage;

public class FabricLongStorage<S> extends SnapshotParticipant<S> implements EnergyStorage {
    private final LongContainer container;
    private final UpdateManager<S> updateManager;

    public FabricLongStorage(LongContainer container, UpdateManager<S> updateManager) {
        this.container = container;
        this.updateManager = updateManager;
    }

    @Override
    public long insert(long maxAmount, TransactionContext transaction) {
        updateSnapshots(transaction);
        return container.insert(maxAmount, false);
    }

    @Override
    public long extract(long maxAmount, TransactionContext transaction) {
        updateSnapshots(transaction);
        return container.extract(maxAmount, false);
    }

    @Override
    public long getAmount() {
        return container.getStoredAmount();
    }

    @Override
    public long getCapacity() {
        return container.getCapacity();
    }

    @Override
    protected void onFinalCommit() {
        updateManager.update();
    }

    @Override
    protected S createSnapshot() {
        return updateManager.createSnapshot();
    }

    @Override
    protected void readSnapshot(S snapshot) {
        updateManager.readSnapshot(snapshot);
    }

    public LongContainer getContainer() {
        return container;
    }
}
