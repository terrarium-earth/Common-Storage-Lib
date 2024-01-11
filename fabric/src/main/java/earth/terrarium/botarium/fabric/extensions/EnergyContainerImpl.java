package earth.terrarium.botarium.fabric.extensions;

import earth.terrarium.botarium.common.energy.base.EnergyContainer;
import earth.terrarium.botarium.common.item.ItemStackHolder;
import earth.terrarium.botarium.fabric.energy.PlatformEnergyManager;
import earth.terrarium.botarium.fabric.energy.PlatformItemEnergyManager;
import earth.terrarium.botarium.fabric.fluid.holder.ItemStackStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.msrandom.extensions.annotations.ClassExtension;
import net.msrandom.extensions.annotations.ImplementedByExtension;
import net.msrandom.extensions.annotations.ImplementsBaseElement;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.Nullable;
import team.reborn.energy.api.EnergyStorage;

@SuppressWarnings("UnstableApiUsage")
@ClassExtension(EnergyContainer.class)
public interface EnergyContainerImpl {

    @ImplementsBaseElement
    static EnergyContainer of(Level level, BlockPos pos, BlockState state, @Nullable BlockEntity entity, @Nullable Direction direction) {
        EnergyStorage energyStorage = EnergyStorage.SIDED.find(level, pos, state, entity, direction);
        return energyStorage != null ? new PlatformEnergyManager(energyStorage) : null;
    }

    @ImplementsBaseElement
    static EnergyContainer of(ItemStackHolder holder) {
        return EnergyContainer.holdsEnergy(holder.getStack()) ? new PlatformItemEnergyManager(holder) : null;
    }

    @ImplementedByExtension
    static boolean holdsEnergy(Level level, BlockPos pos, BlockState state, @Nullable BlockEntity entity, @Nullable Direction direction) {
        return EnergyStorage.SIDED.find(level, pos, state, entity, direction) != null;
    }

    @ImplementsBaseElement
    static boolean holdsEnergy(ItemStack stack) {
        return EnergyStorage.ITEM.find(stack, ItemStackStorage.of(stack)) != null;
    }
}
