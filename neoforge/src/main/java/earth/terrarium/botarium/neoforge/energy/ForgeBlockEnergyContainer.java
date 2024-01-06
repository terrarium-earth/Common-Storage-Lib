package earth.terrarium.botarium.neoforge.energy;

import earth.terrarium.botarium.common.energy.base.EnergyContainer;
import earth.terrarium.botarium.neoforge.AutoSerializable;
import earth.terrarium.botarium.util.Serializable;
import earth.terrarium.botarium.util.Updatable;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import net.neoforged.neoforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@APiSt
public record ForgeBlockEnergyContainer<T extends EnergyContainer & Updatable>(T container) implements IEnergyStorage, AutoSerializable, ICapabilityProvider<Block, Direction, IEnergyStorage> {
    @Override
    public int receiveEnergy(int maxAmount, boolean bl) {
        if (maxAmount <= 0) return 0;
        return (int) container.insertEnergy(Math.min(maxAmount, container.maxInsert()), bl);
    }

    @Override
    public int extractEnergy(int maxAmount, boolean bl) {
        if (maxAmount <= 0) return 0;
        return (int) container.extractEnergy(Math.min(maxAmount, container.maxExtract()), bl);
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
    public @NotNull IEnergyStorage getCapability(Block object, Direction direction) {
        return this;
    }
}
