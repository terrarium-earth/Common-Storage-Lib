package earth.terrarium.botarium.fabric.extensions;

import earth.terrarium.botarium.api.energy.EnergyManager;
import earth.terrarium.botarium.api.energy.PlatformEnergyManager;
import earth.terrarium.botarium.fabric.energy.FabricEnergyManager;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.msrandom.extensions.annotations.ClassExtension;
import net.msrandom.extensions.annotations.ImplementsBaseElement;
import team.reborn.energy.api.EnergyStorageUtil;

@ClassExtension(EnergyManager.class)
public class EnergyManagerImpl {

    @ImplementsBaseElement
    public static PlatformEnergyManager getItemHandler(ItemStack stack) {
        return new FabricEnergyManager(stack);
    }

    @ImplementsBaseElement
    public static boolean isEnergyItem(ItemStack stack) {
        return EnergyStorageUtil.isEnergyStorage(stack);
    }

    @ImplementsBaseElement
    public static PlatformEnergyManager getBlockEnergyManager(BlockEntity entity, Direction direction) {
        return new FabricEnergyManager(entity, direction);
    }
}
