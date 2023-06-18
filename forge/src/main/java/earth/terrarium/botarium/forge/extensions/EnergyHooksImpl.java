package earth.terrarium.botarium.forge.extensions;

import earth.terrarium.botarium.common.energy.base.PlatformEnergyManager;
import earth.terrarium.botarium.common.energy.base.PlatformItemEnergyManager;
import earth.terrarium.botarium.common.energy.util.EnergyHooks;
import earth.terrarium.botarium.forge.energy.ForgeEnergyManager;
import earth.terrarium.botarium.forge.energy.ForgeItemEnergyManager;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.msrandom.extensions.annotations.ClassExtension;
import net.msrandom.extensions.annotations.ImplementedByExtension;
import net.msrandom.extensions.annotations.ImplementsBaseElement;

@ClassExtension(EnergyHooks.class)
public class EnergyHooksImpl {

    @ImplementsBaseElement
    public static PlatformItemEnergyManager getItemEnergyManager(ItemStack stack) {
        return new ForgeItemEnergyManager(stack);
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
