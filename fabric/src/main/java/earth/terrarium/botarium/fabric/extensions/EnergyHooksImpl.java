package earth.terrarium.botarium.fabric.extensions;

import earth.terrarium.botarium.api.energy.EnergyHooks;
import earth.terrarium.botarium.api.energy.PlatformEnergyManager;
import earth.terrarium.botarium.api.energy.PlatformItemEnergyManager;
import earth.terrarium.botarium.fabric.energy.FabricEnergyManager;
import earth.terrarium.botarium.fabric.energy.FabricItemEnergyManager;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.msrandom.extensions.annotations.ClassExtension;
import net.msrandom.extensions.annotations.ImplementedByExtension;
import net.msrandom.extensions.annotations.ImplementsBaseElement;
import team.reborn.energy.api.EnergyStorage;
import team.reborn.energy.api.EnergyStorageUtil;

@ClassExtension(EnergyHooks.class)
public class EnergyHooksImpl {

    @ImplementsBaseElement
    public static PlatformItemEnergyManager getItemEnergyManager(ItemStack stack) {
        return new FabricItemEnergyManager(stack);
    }

    @ImplementsBaseElement
    public static boolean isEnergyItem(ItemStack stack) {
        return EnergyStorageUtil.isEnergyStorage(stack);
    }

    @ImplementsBaseElement
    public static PlatformEnergyManager getBlockEnergyManager(BlockEntity entity, Direction direction) {
        return new FabricEnergyManager(entity, direction);
    }

    @ImplementedByExtension
    public static boolean isEnergyContainer(BlockEntity block, Direction direction) {
        return EnergyStorage.SIDED.find(block.getLevel(), block.getBlockPos(), direction) != null;
    }
}
