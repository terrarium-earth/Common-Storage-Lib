package earth.terrarium.botarium.api.forge;

import earth.terrarium.botarium.api.ItemEnergyHandler;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ForgeCapabilities;

public class EnergyHandlerImpl {
    public static ItemEnergyHandler getItemHandler(ItemStack stack) {
        return new ForgeEnergyItemHandler(stack);
    }

    public static boolean isEnergyItem(ItemStack stack) {
        return stack.getCapability(ForgeCapabilities.ENERGY).isPresent();
    }
}
