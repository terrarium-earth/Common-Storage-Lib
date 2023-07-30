package earth.terrarium.botarium.fabric.energy;

import earth.terrarium.botarium.common.energy.base.EnergyContainer;
import earth.terrarium.botarium.common.energy.base.EnergySnapshot;
import earth.terrarium.botarium.common.energy.impl.SimpleEnergySnapshot;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.ApiStatus;
import team.reborn.energy.api.EnergyStorage;

@ApiStatus.Internal
@SuppressWarnings("UnstableApiUsage")
public record PlatformEnergyManager(EnergyStorage energy) implements EnergyContainer {

    public PlatformEnergyManager(BlockEntity entity, Direction direction) {
        this(EnergyStorage.SIDED.find(entity.getLevel(), entity.getBlockPos(), direction));
    }

    @Override
    public long insertEnergy(long maxAmount, boolean simulate) {
        try (Transaction txn = Transaction.openOuter()) {
            long insert = energy.insert(maxAmount, txn);
            if (simulate) txn.abort();
            else txn.commit();
            return insert;
        }
    }

    @Override
    public long extractEnergy(long maxAmount, boolean simulate) {
        try (Transaction txn = Transaction.openOuter()) {
            long extract = energy.extract(maxAmount, txn);
            if (simulate) txn.abort();
            else txn.commit();
            return extract;
        }
    }

    @Override
    public void setEnergy(long energy) {
        if (energy > this.energy.getAmount()) {
            insertEnergy(energy - this.energy.getAmount(), false);
        } else if (energy < this.energy.getAmount()) {
            extractEnergy(this.energy.getAmount() - energy, false);
        }
    }

    @Override
    public long getStoredEnergy() {
        return energy.getAmount();
    }

    @Override
    public long getMaxCapacity() {
        return energy.getCapacity();
    }

    @Override
    public long maxInsert() {
        return energy.getCapacity();
    }

    @Override
    public long maxExtract() {
        return energy.getCapacity();
    }

    @Override
    public boolean allowsInsertion() {
        return energy.supportsInsertion();
    }

    @Override
    public boolean allowsExtraction() {
        return energy.supportsExtraction();
    }

    @Override
    public EnergySnapshot createSnapshot() {
        return new SimpleEnergySnapshot(this);
    }

    @Override
    public void deserialize(CompoundTag nbt) {

    }

    @Override
    public CompoundTag serialize(CompoundTag nbt) {
        return nbt;
    }

    @Override
    public void clearContent() {
        setEnergy(0);
    }
}
