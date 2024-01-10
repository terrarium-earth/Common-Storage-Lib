package earth.terrarium.botarium.fabric.extensions;

import earth.terrarium.botarium.common.fluid.base.FluidContainer;
import earth.terrarium.botarium.fabric.fluid.storage.PlatformFluidContainer;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.msrandom.extensions.annotations.ImplementsBaseElement;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.Nullable;

public class FluidContainerImpl {
    @Nullable
    @ImplementsBaseElement
    static FluidContainer of(Level level, BlockPos pos, BlockState state, @Nullable BlockEntity entity, @Nullable Direction direction) {
        Storage<FluidVariant> storage = FluidStorage.SIDED.find(level, pos, state, entity, direction);
        return storage == null ? null : new PlatformFluidContainer(storage);
    }

    @ImplementsBaseElement
    static boolean holdsFluid(Level level, BlockPos pos, BlockState state, @Nullable BlockEntity entity, @Nullable Direction direction) {
        return FluidStorage.SIDED.find(level, pos, state, entity, direction) != null;
    }
}
