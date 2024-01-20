package earth.terrarium.botarium.impl.extensions;

import earth.terrarium.botarium.common.energy.base.EnergyContainer;
import earth.terrarium.botarium.common.item.ItemStackHolder;
import earth.terrarium.botarium.impl.ItemStackStorage;
import earth.terrarium.botarium.impl.energy.PlatformEnergyManager;
import earth.terrarium.botarium.impl.energy.PlatformItemEnergyManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.msrandom.extensions.annotations.ClassExtension;
import net.msrandom.extensions.annotations.ImplementedByExtension;
import org.jetbrains.annotations.Nullable;
import team.reborn.energy.api.EnergyStorage;

@ClassExtension(EnergyContainer.class)
@SuppressWarnings("unused")
public interface EnergyContainerImpl {
    static EnergyContainer of(Level level, BlockPos pos, BlockState state, @Nullable BlockEntity entity, @Nullable Direction direction) {
        return PlatformEnergyManager.of(level, pos, state, entity, direction);
    }

    static EnergyContainer of(ItemStackHolder holder) {
        return PlatformItemEnergyManager.of(holder);
    }

    @ImplementedByExtension
    static boolean holdsEnergy(ItemStack stack) {
        return EnergyStorage.ITEM.find(stack, ItemStackStorage.of(stack)) != null;
    }

    @ImplementedByExtension
    static boolean holdsEnergy(Level level, BlockPos pos, BlockState state, @Nullable BlockEntity entity, @Nullable Direction direction) {
        return EnergyStorage.SIDED.find(level, pos, state, entity, direction) != null;
    }
}
