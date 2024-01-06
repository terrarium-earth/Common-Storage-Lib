package earth.terrarium.botarium.neoforge.energy;

import earth.terrarium.botarium.common.energy.base.EnergyContainer;
import earth.terrarium.botarium.common.energy.base.EnergySnapshot;
import earth.terrarium.botarium.common.energy.impl.SimpleEnergySnapshot;
import earth.terrarium.botarium.common.item.ItemStackHolder;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

@ApiStatus.Internal
@SuppressWarnings("UnstableApiUsage")
public record PlatformItemEnergyManager(IEnergyStorage energy) implements EnergyContainer {

    @Nullable
    public static EnergyContainer of(ItemStackHolder stack) {
        var energy = stack.getStack().getCapability(Capabilities.EnergyStorage.ITEM);
        return energy != null ? new PlatformItemEnergyManager(energy) : null;
    }

    @Override
    public long insertEnergy(long maxAmount, boolean simulate) {
        return energy.receiveEnergy((int) maxAmount, simulate);
    }

    @Override
    public long extractEnergy(long maxAmount, boolean simulate) {
        return energy.extractEnergy((int) maxAmount, simulate);
    }

    @Override
    public void setEnergy(long energy) {
        if (energy > this.energy.getEnergyStored()) {
            this.energy.receiveEnergy((int) (energy - this.energy.getEnergyStored()), false);
        } else if (energy < this.energy.getEnergyStored()) {
            this.energy.extractEnergy((int) (this.energy.getEnergyStored() - energy), false);
        }
    }

    @Override
    public long getStoredEnergy() {
        return energy.getEnergyStored();
    }

    @Override
    public long getMaxCapacity() {
        return energy.getMaxEnergyStored();
    }

    @Override
    public long maxInsert() {
        return Integer.MAX_VALUE;
    }

    @Override
    public long maxExtract() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean allowsInsertion() {
        return energy.canReceive();
    }

    @Override
    public boolean allowsExtraction() {
        return energy.canExtract();
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
