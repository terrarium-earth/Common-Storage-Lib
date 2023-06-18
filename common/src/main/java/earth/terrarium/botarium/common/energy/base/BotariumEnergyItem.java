package earth.terrarium.botarium.common.energy.base;

import earth.terrarium.botarium.util.Updatable;
import net.minecraft.world.item.ItemStack;

public interface BotariumEnergyItem<T extends EnergyContainer & Updatable<ItemStack>> {
    T getEnergyStorage(ItemStack holder);
}
