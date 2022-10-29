package earth.terrarium.botarium.fabric.energy;

import earth.terrarium.botarium.api.energy.EnergySnapshot;
import earth.terrarium.botarium.api.energy.StatefulEnergyContainer;
import net.fabricmc.fabric.api.transfer.v1.storage.StoragePreconditions;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.fabricmc.fabric.api.transfer.v1.transaction.base.SnapshotParticipant;
import team.reborn.energy.api.EnergyStorage;

@SuppressWarnings("UnstableApiUsage")
public class FabricBlockEnergyStorage extends SnapshotParticipant<EnergySnapshot> implements EnergyStorage {
    private final StatefulEnergyContainer container;

    public FabricBlockEnergyStorage(StatefulEnergyContainer container) {
        this.container = container;
    }

    @Override
    public long insert(long maxAmount, TransactionContext transaction) {
        this.updateSnapshots(transaction);
        return container.insertEnergy(Math.max(maxAmount, this.container.maxInsert()), false);
    }

    @Override
    public long extract(long maxAmount, TransactionContext transaction) {
        this.updateSnapshots(transaction);
        return container.extractEnergy(Math.max(maxAmount, this.container.maxExtract()), false);
    }

    @Override
    public long getAmount() {
        return container.getStoredEnergy();
    }

    @Override
    public long getCapacity() {
        return container.getMaxCapacity();
    }

    @Override
    public boolean supportsInsertion() {
        return container.allowsInsertion();
    }

    @Override
    public boolean supportsExtraction() {
        return container.allowsExtraction();
    }

    @Override
    protected EnergySnapshot createSnapshot() {
        return container.createSnapshot();
    }

    @Override
    protected void readSnapshot(EnergySnapshot snapshot) {
        container.readSnapshot(snapshot);
    }

    @Override
    protected void onFinalCommit() {
        this.container.update();
    }
}
