package earth.terrarium.botarium.api.energy;

import earth.terrarium.botarium.api.BotariumCap;
import net.minecraft.world.item.ItemStack;

public interface EnergyItem extends BotariumCap {

    /**
     * Gets the {@link EnergyContainer} for the given {@link ItemStack}.
     *
     * @param object the {@link ItemStack} to get the {@link EnergyContainer} from.
     * @return the {@link EnergyContainer} for the given {@link ItemStack}.
     */
    EnergyContainer getEnergyStorage(ItemStack object);
}
