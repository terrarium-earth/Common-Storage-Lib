package earth.terrarium.botarium.fabric.energy;

import earth.terrarium.botarium.common.menu.base.EnergySnapshot;
import earth.terrarium.botarium.common.menu.base.EnergyContainer;
import earth.terrarium.botarium.util.Updatable;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.fabricmc.fabric.api.transfer.v1.transaction.base.SnapshotParticipant;
import net.minecraft.world.level.block.entity.BlockEntity;
import team.reborn.energy.api.EnergyStorage;

@SuppressWarnings("UnstableApiUsage")
public class FabricBlockEnergyStorage<T extends EnergyContainer & Updatable<BlockEntity>> extends SnapshotParticipant<EnergySnapshot> implements EnergyStorage {
    private final T container;
    private final Updatable<BlockEntity> updatable;
    private final BlockEntity block;

    public FabricBlockEnergyStorage(T container, BlockEntity block) {
        this.container = container;
        this.updatable = container;
        this.block = block;
    }

    @Override
    public long insert(long maxAmount, TransactionContext transaction) {
        if(maxAmount <= 0) return 0;
        this.updateSnapshots(transaction);
        return container.insertEnergy(Math.min(maxAmount, this.container.maxInsert()), false);
    }

    @Override
    public long extract(long maxAmount, TransactionContext transaction) {
        if(maxAmount <= 0) return 0;
        this.updateSnapshots(transaction);
        return container.extractEnergy(Math.min(maxAmount, this.container.maxExtract()), false);
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
        this.container.update(block);
    }
}
