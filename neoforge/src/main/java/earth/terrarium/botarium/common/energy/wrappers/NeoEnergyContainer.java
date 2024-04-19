package earth.terrarium.botarium.common.energy.wrappers;

import earth.terrarium.botarium.common.storage.base.LongContainer;
import earth.terrarium.botarium.common.storage.util.UpdateManager;
import net.neoforged.neoforge.energy.IEnergyStorage;

public record NeoEnergyContainer(LongContainer container) implements IEnergyStorage {
    @Override
    public int receiveEnergy(int i, boolean bl) {
        long inserted = container.insert(i, bl);
        if (!bl) UpdateManager.update(container);
        return (int) inserted;
    }

    @Override
    public int extractEnergy(int i, boolean bl) {
        long extracted = container.extract(i, bl);
        if (!bl) UpdateManager.update(container);
        return (int) extracted;
    }

    @Override
    public int getEnergyStored() {
        return (int) container.getStoredAmount();
    }

    @Override
    public int getMaxEnergyStored() {
        return (int) container.getCapacity();
    }

    @Override
    public boolean canExtract() {
        return container.allowsExtraction();
    }

    @Override
    public boolean canReceive() {
        return container.allowsInsertion();
    }
}
