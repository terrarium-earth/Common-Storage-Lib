package earth.terrarium.botarium.forge.extensions;

import earth.terrarium.botarium.api.energy.EnergyHooks;
import earth.terrarium.botarium.api.energy.PlatformEnergyManager;
import earth.terrarium.botarium.forge.energy.ForgeEnergyManager;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.msrandom.extensions.annotations.ClassExtension;
import net.msrandom.extensions.annotations.ImplementedByExtension;
import net.msrandom.extensions.annotations.ImplementsBaseElement;

@ClassExtension(EnergyHooks.class)
public class EnergyManagerImpl {

    @ImplementsBaseElement
    public static PlatformEnergyManager getItemEnergyManager(ItemStack stack) {
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

    @ImplementedByExtension
    public static boolean isEnergyContainer(BlockEntity block, Direction direction) {
        return block.getCapability(ForgeCapabilities.ENERGY, direction).isPresent();
    }
}
