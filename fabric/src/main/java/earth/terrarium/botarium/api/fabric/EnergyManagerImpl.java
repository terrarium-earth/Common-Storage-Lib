package earth.terrarium.botarium.api.fabric;

import earth.terrarium.botarium.api.PlatformEnergyManager;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import team.reborn.energy.api.EnergyStorageUtil;

public class EnergyManagerImpl {
    public static PlatformEnergyManager getItemHandler(ItemStack stack) {
        return new FabricEnergyManager(stack);
    }

    public static boolean isEnergyItem(ItemStack stack) {
        return EnergyStorageUtil.isEnergyStorage(stack);
    }

    public static PlatformEnergyManager getBlockEnergyManager(BlockEntity entity, Direction direction) {
        return new FabricEnergyManager(entity, direction);
    }
}
