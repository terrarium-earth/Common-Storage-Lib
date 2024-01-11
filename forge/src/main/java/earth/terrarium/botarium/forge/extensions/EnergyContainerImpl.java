package earth.terrarium.botarium.forge.extensions;

import earth.terrarium.botarium.common.energy.base.EnergyContainer;
import earth.terrarium.botarium.common.item.ItemStackHolder;
import earth.terrarium.botarium.forge.energy.PlatformBlockEnergyManager;
import earth.terrarium.botarium.forge.energy.PlatformItemEnergyManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.msrandom.extensions.annotations.ClassExtension;
import net.msrandom.extensions.annotations.ImplementedByExtension;
import net.msrandom.extensions.annotations.ImplementsBaseElement;
import org.jetbrains.annotations.Nullable;

@ClassExtension(EnergyContainer.class)
public interface EnergyContainerImpl {

    @ImplementsBaseElement
    static EnergyContainer of(Level level, BlockPos pos, BlockState state, @Nullable BlockEntity entity, @Nullable Direction direction) {
        var blockEntity = (entity == null ? level.getBlockEntity(pos) : entity);
        if (blockEntity != null) {
            return blockEntity.getCapability(ForgeCapabilities.ENERGY, direction).map(PlatformBlockEnergyManager::new).orElse(null);
        }
        return null;
    }

    @ImplementsBaseElement
    static EnergyContainer of(ItemStackHolder holder) {
        return EnergyContainer.holdsEnergy(holder.getStack()) ? new PlatformItemEnergyManager(holder) : null;
    }

    @ImplementedByExtension
    static boolean holdsEnergy(Level level, BlockPos pos, BlockState state, @Nullable BlockEntity entity, @Nullable Direction direction) {
        var blockEntity = (entity == null ? level.getBlockEntity(pos) : entity);
        if (blockEntity != null) {
            return blockEntity.getCapability(ForgeCapabilities.ENERGY, direction).isPresent();
        }
        return false;
    }

    @ImplementsBaseElement
    static boolean holdsEnergy(ItemStack stack) {
        return stack.getCapability(ForgeCapabilities.ENERGY).isPresent();
    }
}
