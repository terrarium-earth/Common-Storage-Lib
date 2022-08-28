package earth.terrarium.botarium.api;

import net.minecraft.world.item.ItemStack;

public interface EnergyItem {
    EnergyContainer getEnergyStorage(ItemStack object);
}
