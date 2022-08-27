package earth.terrarium.botarium.api.forge;

import earth.terrarium.botarium.api.PlatformEnergyManager;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;

public class EnergyManagerImpl {
    public static PlatformEnergyManager getItemHandler(ItemStack stack) {
        return new ForgeEnergyManager(stack);
    }

    public static PlatformEnergyManager getBlockEnergyManager(BlockEntity entity, Direction direction) {
        return new ForgeEnergyManager(entity, direction);
    }

    public static boolean isEnergyItem(ItemStack stack) {
        return stack.getCapability(ForgeCapabilities.ENERGY).isPresent();
    }
}
