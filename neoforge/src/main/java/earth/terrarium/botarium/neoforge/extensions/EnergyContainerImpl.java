package earth.terrarium.botarium.neoforge.extensions;

import earth.terrarium.botarium.common.energy.base.EnergyContainer;
import earth.terrarium.botarium.common.item.ItemStackHolder;
import earth.terrarium.botarium.neoforge.energy.PlatformEnergyManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.msrandom.extensions.annotations.ClassExtension;
import net.msrandom.extensions.annotations.ImplementedByExtension;
import net.neoforged.neoforge.capabilities.Capabilities;
import org.jetbrains.annotations.Nullable;

@ClassExtension(EnergyContainer.class)
public interface EnergyContainerImpl {
    static EnergyContainer of(Level level, BlockPos pos, BlockState state, @Nullable BlockEntity entity, @Nullable Direction direction) {
        return PlatformEnergyManager.of(level, pos, state, entity, direction);
    }

    static EnergyContainer of(ItemStackHolder holder) {
        return PlatformEnergyManager.of(holder);
    }

    @ImplementedByExtension
    static boolean holdsEnergy(ItemStack stack) {
        return stack.getCapability(Capabilities.EnergyStorage.ITEM) != null;
    }

    @ImplementedByExtension
    static boolean holdsEnergy(Level level, BlockPos pos, BlockState state, @Nullable BlockEntity entity, @Nullable Direction direction) {
        return level.getCapability(Capabilities.EnergyStorage.BLOCK, pos, state, entity, direction) != null;
    }
}
