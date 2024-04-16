package earth.terrarium.botarium.common.fluid.lookup;

import earth.terrarium.botarium.common.fluid.base.FluidContainer;
import earth.terrarium.botarium.common.lookup.base.BlockContainerLookup;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class EntityFluidContainerLookup implements BlockContainerLookup<FluidContainer, Void> {
    @Override
    public @Nullable FluidContainer find(Level level, BlockPos pos, @Nullable BlockState state, @Nullable BlockEntity entity, @Nullable Void direction) {
        return null;
    }

    @Override
    public void registerBlocks(BlockGetter<FluidContainer, Void> getter, Supplier<Block>... containers) {

    }

    @Override
    public void registerBlockEntities(BlockGetter<FluidContainer, Void> getter, Supplier<BlockEntityType<?>>... containers) {

    }
}
