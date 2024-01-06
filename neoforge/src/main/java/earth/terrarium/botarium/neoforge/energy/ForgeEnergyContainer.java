package earth.terrarium.botarium.neoforge.energy;

import earth.terrarium.botarium.common.energy.base.EnergyContainer;
import earth.terrarium.botarium.neoforge.AutoSerializable;
import earth.terrarium.botarium.util.Serializable;
import earth.terrarium.botarium.util.Updatable;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import net.neoforged.neoforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record ForgeEnergyContainer<T extends EnergyContainer & Updatable>(T container) implements IEnergyStorage, AutoSerializable, ICapabilityProvider<BlockEntity, Direction, IEnergyStorage> {
    @Override
    public int receiveEnergy(int maxAmount, boolean bl) {
        if (maxAmount <= 0) return 0;
        int inserted = (int) container.insertEnergy(Math.min(maxAmount, container.maxInsert()), bl);
        container.update();
        return inserted;
    }

    @Override
    public int extractEnergy(int maxAmount, boolean bl) {
        if (maxAmount <= 0) return 0;
        int extracted = (int) container.extractEnergy(Math.min(maxAmount, container.maxExtract()), bl);
        container.update();
        return extracted;
    }

    @Override
    public int getEnergyStored() {
        return (int) container.getStoredEnergy();
    }

    @Override
    public int getMaxEnergyStored() {
        return (int) container.getMaxCapacity();
    }

    @Override
    public boolean canExtract() {
        return container.allowsExtraction();
    }

    @Override
    public boolean canReceive() {
        return container.allowsInsertion();
    }

    @Override
    public Serializable getSerializable() {
        return container;
    }

    @Override
    public @NotNull IEnergyStorage getCapability(BlockEntity object, Direction direction) {
        return this;
    }
}
