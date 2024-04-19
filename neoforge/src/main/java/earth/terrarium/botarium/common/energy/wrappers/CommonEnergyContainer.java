package earth.terrarium.botarium.common.energy.wrappers;

import earth.terrarium.botarium.common.storage.base.LongContainer;
import net.neoforged.neoforge.energy.IEnergyStorage;

public record CommonEnergyContainer(IEnergyStorage storage) implements LongContainer {
    @Override
    public long getStoredAmount() {
        return storage.getEnergyStored();
    }

    @Override
    public long getCapacity() {
        return storage.getMaxEnergyStored();
    }

    @Override
    public boolean allowsInsertion() {
        return storage.canReceive();
    }

    @Override
    public boolean allowsExtraction() {
        return storage.canExtract();
    }

    @Override
    public long insert(long amount, boolean simulate) {
        return storage.receiveEnergy((int) amount, simulate);
    }

    @Override
    public long extract(long amount, boolean simulate) {
        return storage.extractEnergy((int) amount, simulate);
    }
}
