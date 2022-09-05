package earth.terrarium.botarium.fabric.energy;

import earth.terrarium.botarium.api.energy.EnergyContainer;
import earth.terrarium.botarium.api.energy.UpdatingEnergyContainer;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.fabricmc.fabric.api.transfer.v1.transaction.base.SnapshotParticipant;
import net.minecraft.nbt.CompoundTag;
import team.reborn.energy.api.EnergyStorage;

@SuppressWarnings("UnstableApiUsage")
public class FabricBlockEnergyStorage extends SnapshotParticipant<CompoundTag> implements EnergyStorage {
    private final UpdatingEnergyContainer container;

    public FabricBlockEnergyStorage(UpdatingEnergyContainer container) {
        this.container = container;
    }

    @Override
    public long insert(long maxAmount, TransactionContext transaction) {
        this.updateSnapshots(transaction);
        return container.insertEnergy(maxAmount, false);
    }

    @Override
    public long extract(long maxAmount, TransactionContext transaction) {
        this.updateSnapshots(transaction);
        return container.insertEnergy(maxAmount, false);
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
    protected CompoundTag createSnapshot() {
        return container.serialize(new CompoundTag());
    }

    @Override
    protected void readSnapshot(CompoundTag snapshot) {
        container.deseralize(snapshot);
    }

    @Override
    protected void onFinalCommit() {
        this.container.update();
    }
}
