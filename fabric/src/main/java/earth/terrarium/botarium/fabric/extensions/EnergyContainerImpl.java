package earth.terrarium.botarium.fabric.extensions;

import earth.terrarium.botarium.common.energy.base.EnergyContainer;
import earth.terrarium.botarium.fabric.energy.PlatformEnergyManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.msrandom.extensions.annotations.ClassExtension;
import net.msrandom.extensions.annotations.ImplementedByExtension;
import net.msrandom.extensions.annotations.ImplementsBaseElement;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.Nullable;
import team.reborn.energy.api.EnergyStorage;

@ClassExtension(EnergyContainer.class)
public interface EnergyContainerImpl {

    @ImplementsBaseElement
    static EnergyContainer of(Level level, BlockPos pos, BlockState state, @Nullable BlockEntity entity, @Nullable Direction direction) {
        EnergyStorage energyStorage = EnergyStorage.SIDED.find(level, pos, state, entity, direction);
        return energyStorage != null ? new PlatformEnergyManager(energyStorage) : null;
    }

    @ImplementedByExtension
    static boolean holdsEnergy(Level level, BlockPos pos, BlockState state, @Nullable BlockEntity entity, @Nullable Direction direction) {
        return EnergyStorage.SIDED.find(level, pos, state, entity, direction) != null;
    }
}
