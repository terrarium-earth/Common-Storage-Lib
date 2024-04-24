package earth.terrarium.botarium.common.storage.fabric;

import earth.terrarium.botarium.common.storage.base.LongContainer;
import earth.terrarium.botarium.common.storage.util.UpdateManager;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.fabricmc.fabric.api.transfer.v1.transaction.base.SnapshotParticipant;
import team.reborn.energy.api.EnergyStorage;

public class FabricLongStorage implements EnergyStorage {
    private final LongContainer container;
    private final OptionalSnapshotParticipant<?> snapshotParticipant;

    public FabricLongStorage(LongContainer container) {
        this.container = container;
        if (container instanceof UpdateManager<?> updateManager) {
            this.snapshotParticipant = new OptionalSnapshotParticipant<>(updateManager);
        } else {
            this.snapshotParticipant = null;
        }
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

    public LongContainer getContainer() {
        return container;
    }

    private void updateSnapshots(TransactionContext transaction) {
        if (snapshotParticipant != null) {
            snapshotParticipant.updateSnapshots(transaction);
        }
    }
}
