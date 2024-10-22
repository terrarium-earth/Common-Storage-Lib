package earth.terrarium.common_storage_lib.storage.fabric;

import earth.terrarium.common_storage_lib.storage.base.ValueStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import team.reborn.energy.api.EnergyStorage;

public record FabricLongStorage(ValueStorage container,
                                OptionalSnapshotParticipant<?> snapshotParticipant) implements EnergyStorage {

    public FabricLongStorage(ValueStorage container) {
        this(container, OptionalSnapshotParticipant.of(container));
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

    private void updateSnapshots(TransactionContext transaction) {
        if (snapshotParticipant != null) {
            snapshotParticipant.updateSnapshots(transaction);
        }
    }
}
