package earth.terrarium.botarium.common.storage.typed;

import earth.terrarium.botarium.common.storage.base.ValueContainer;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.fabricmc.fabric.api.transfer.v1.transaction.base.SnapshotParticipant;
import net.minecraft.core.component.DataComponentPatch;
import team.reborn.energy.api.EnergyStorage;

public class FabricEnergyStorage extends SnapshotParticipant<DataComponentPatch> implements EnergyStorage {
    private final ValueContainer container;

    public FabricEnergyStorage(ValueContainer container) {
        this.container = container;
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
        container.update();
    }

    @Override
    protected DataComponentPatch createSnapshot() {
        return container.createSnapshot();
    }

    @Override
    protected void readSnapshot(DataComponentPatch snapshot) {
        container.readSnapshot(snapshot);
    }
}
