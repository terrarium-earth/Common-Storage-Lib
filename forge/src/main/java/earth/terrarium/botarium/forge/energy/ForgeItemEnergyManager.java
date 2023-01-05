package earth.terrarium.botarium.forge.energy;

import earth.terrarium.botarium.common.menu.base.PlatformItemEnergyManager;
import earth.terrarium.botarium.common.item.ItemStackHolder;
import net.minecraftforge.common.capabilities.CapabilityProvider;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.energy.IEnergyStorage;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
@SuppressWarnings("UnstableApiUsage")
public record ForgeItemEnergyManager(IEnergyStorage energy) implements PlatformItemEnergyManager {

    public ForgeItemEnergyManager(CapabilityProvider<?> energyItem) {
        this(energyItem.getCapability(ForgeCapabilities.ENERGY).orElseThrow(IllegalArgumentException::new));
    }

    @Override
    public long getStoredEnergy() {
        return energy.getEnergyStored();
    }

    @Override
    public long getCapacity() {
        return energy.getMaxEnergyStored();
    }

    @Override
    public long extract(ItemStackHolder holder, long amount, boolean simulate) {
        return energy.extractEnergy((int) amount, simulate);
    }

    @Override
    public long insert(ItemStackHolder holder, long amount, boolean simulate) {
        return energy.receiveEnergy((int) amount, simulate);
    }

    @Override
    public boolean supportsInsertion() {
        return energy.canReceive();
    }

    @Override
    public boolean supportsExtraction() {
        return energy.canExtract();
    }
}
