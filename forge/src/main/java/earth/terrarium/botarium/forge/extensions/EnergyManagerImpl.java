package earth.terrarium.botarium.forge.extensions;

import earth.terrarium.botarium.api.energy.EnergyManager;
import earth.terrarium.botarium.api.energy.PlatformEnergyManager;
import earth.terrarium.botarium.forge.ForgeEnergyManager;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.msrandom.extensions.annotations.ClassExtension;
import net.msrandom.extensions.annotations.ImplementsBaseElement;

@ClassExtension(EnergyManager.class)
public class EnergyManagerImpl {

    @ImplementsBaseElement
    public static PlatformEnergyManager getItemHandler(ItemStack stack) {
        return new ForgeEnergyManager(stack);
    }

    @ImplementsBaseElement
    public static PlatformEnergyManager getBlockEnergyManager(BlockEntity entity, Direction direction) {
        return new ForgeEnergyManager(entity, direction);
    }

    @ImplementsBaseElement
    public static boolean isEnergyItem(ItemStack stack) {
        return stack.getCapability(ForgeCapabilities.ENERGY).isPresent();
    }
}
