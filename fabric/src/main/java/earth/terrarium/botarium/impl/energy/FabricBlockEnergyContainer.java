package earth.terrarium.botarium.impl.energy;

import earth.terrarium.botarium.common.energy.base.EnergyContainer;
import earth.terrarium.botarium.common.energy.base.EnergySnapshot;
import earth.terrarium.botarium.util.Updatable;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.fabricmc.fabric.api.transfer.v1.transaction.base.SnapshotParticipant;
import team.reborn.energy.api.EnergyStorage;

public class FabricBlockEnergyContainer<T extends EnergyContainer & Updatable> extends SnapshotParticipant<EnergySnapshot> implements EnergyStorage {
    private final T container;

    public FabricBlockEnergyContainer(T container) {
        this.container = container;
    }

    @Override
    public long insert(long maxAmount, TransactionContext transaction) {
        if (maxAmount <= 0) return 0;
        this.updateSnapshots(transaction);
        return container.insertEnergy(maxAmount, false);
    }

    @Override
    public long extract(long maxAmount, TransactionContext transaction) {
        if (maxAmount <= 0) return 0;
        this.updateSnapshots(transaction);
        return container.extractEnergy(maxAmount, false);
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
        container.update();
    }
}
