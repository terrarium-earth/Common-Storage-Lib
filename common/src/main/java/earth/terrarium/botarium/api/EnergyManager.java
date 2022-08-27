package earth.terrarium.botarium.api;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.apache.commons.lang3.NotImplementedException;

public class EnergyManager {

    @ExpectPlatform
    public static PlatformEnergyManager getItemHandler(ItemStack stack) {
        throw new NotImplementedException("Item Energy Manager not Implemented");
    }

    @ExpectPlatform
    public static PlatformEnergyManager getBlockEnergyManager(BlockEntity entity, Direction direction) {
        throw new NotImplementedException("Block Entity Energy manager not implemented");
    }

    @ExpectPlatform
    public static boolean isEnergyItem(ItemStack stack) {
        throw new NotImplementedException("Energy item check not Implemented");
    }
}
