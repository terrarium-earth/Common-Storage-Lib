package earth.terrarium.botarium.forge.energy;

import earth.terrarium.botarium.common.energy.base.EnergyContainer;
import earth.terrarium.botarium.util.Updatable;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record ForgeItemEnergyContainer<T extends EnergyContainer & Updatable<ItemStack>>(T container, ItemStack entity) implements IEnergyStorage, ICapabilityProvider {

    @Override
    @NotNull
    public  <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction arg) {
        LazyOptional<IEnergyStorage> of = LazyOptional.of(container.getContainer(arg) != null ? () -> this : null);
        return capability.orEmpty(CapabilityEnergy.ENERGY, of.cast()).cast();
    }

    @Override
    public int receiveEnergy(int maxAmount, boolean bl) {
        if(maxAmount <= 0) return 0;
        int inserted = (int) container.insertEnergy(Math.min(maxAmount, container.maxInsert()), bl);
        container.update(entity);
        return inserted;
    }

    @Override
    public int extractEnergy(int maxAmount, boolean bl) {
        if(maxAmount <= 0) return 0;
        int extracted = (int) container.extractEnergy(Math.min(maxAmount, container.maxExtract()), bl);
        container.update(entity);
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
}
