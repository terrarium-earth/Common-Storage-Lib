package earth.terrarium.botarium.api.energy;

import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.msrandom.extensions.annotations.ImplementedByExtension;
import org.apache.commons.lang3.NotImplementedException;

public class EnergyManager {

    @ImplementedByExtension
    public static PlatformEnergyManager getItemHandler(ItemStack stack) {
        throw new NotImplementedException("Item Energy Manager not Implemented");
    }

    @ImplementedByExtension
    public static PlatformEnergyManager getBlockEnergyManager(BlockEntity entity, Direction direction) {
        throw new NotImplementedException("Block Entity Energy manager not implemented");
    }

    @ImplementedByExtension
    public static boolean isEnergyItem(ItemStack stack) {
        throw new NotImplementedException("Energy item check not Implemented");
    }
}
