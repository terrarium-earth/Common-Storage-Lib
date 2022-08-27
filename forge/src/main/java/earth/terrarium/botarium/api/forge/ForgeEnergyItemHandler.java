package earth.terrarium.botarium.api.forge;

import earth.terrarium.botarium.api.ItemEnergyHandler;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.energy.IEnergyStorage;

public class ForgeEnergyItemHandler implements ItemEnergyHandler {
    private final IEnergyStorage energy;

    public ForgeEnergyItemHandler(ItemStack energyItem) {
        this.energy = energyItem.getCapability(ForgeCapabilities.ENERGY).orElseThrow(IllegalArgumentException::new) ;
    }

    @Override
    public long getCapacity() {
        return energy.getEnergyStored();
    }

    @Override
    public long maxCapacity() {
        return energy.getMaxEnergyStored();
    }

    @Override
    public long extract(int amount, boolean simulate) {
        return energy.extractEnergy(amount, simulate);
    }

    @Override
    public long insert(int amount, boolean simulate) {
        return energy.receiveEnergy(amount, simulate);
    }
}
