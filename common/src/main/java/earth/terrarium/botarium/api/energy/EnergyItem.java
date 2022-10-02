package earth.terrarium.botarium.api.energy;

import net.minecraft.world.item.ItemStack;

public interface EnergyItem {

    /**
     * Gets the {@link EnergyContainer} for the given {@link ItemStack}.
     *
     * @param object the {@link ItemStack} to get the {@link EnergyContainer} from.
     * @return the {@link EnergyContainer} for the given {@link ItemStack}.
     */
    EnergyContainer getEnergyStorage(ItemStack object);
}
