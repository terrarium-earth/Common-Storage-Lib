package earth.terrarium.botarium.api.energy;

import net.minecraft.world.item.ItemStack;

public interface EnergyItem {
    EnergyContainer getEnergyStorage(ItemStack object);
}
