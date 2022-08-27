package earth.terrarium.botarium.api.fabric;

import earth.terrarium.botarium.api.ItemEnergyHandler;
import net.minecraft.world.item.ItemStack;
import team.reborn.energy.api.EnergyStorageUtil;

public class EnergyHandlerImpl {
    public static ItemEnergyHandler getItemHandler(ItemStack stack) {
        return new FabricEnergyItemHandler(stack);
    }

    public static boolean isEnergyItem(ItemStack stack) {
        return EnergyStorageUtil.isEnergyStorage(stack);
    }
}
