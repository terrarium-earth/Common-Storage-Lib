package earth.terrarium.botarium.common.fluid.lookup;

import earth.terrarium.botarium.common.fluid.FluidApi;
import earth.terrarium.botarium.common.fluid.base.FluidContainer;
import earth.terrarium.botarium.common.lookup.BlockLookup;
import earth.terrarium.botarium.common.storage.typed.CommonFluidContainer;
import earth.terrarium.botarium.common.storage.typed.FabricFluidContainer;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.function.Supplier;

public class FluidBlockLookup implements BlockLookup<FluidContainer, @Nullable Direction> {

    @Override
    public @Nullable FluidContainer find(Level level, BlockPos pos, @Nullable BlockState state, @Nullable BlockEntity entity, @Nullable Direction direction) {
        Storage<FluidVariant> storage = FluidStorage.SIDED.find(level, pos, state, entity, direction);
        if (storage == null) {
            return null;
        }
        if (storage instanceof FabricFluidContainer fabric) {
            return fabric.getContainer();
        }
        return new CommonFluidContainer(storage);
    }

    @Override
    public void registerBlocks(BlockGetter<FluidContainer, @Nullable Direction> getter, Supplier<Block>... containers) {
        for (Supplier<Block> block : containers) {
            FluidStorage.SIDED.registerForBlocks((world, pos, state, blockEntity, context) -> {
                FluidContainer container = getter.getContainer(world, pos, state, blockEntity, context);
                if (container != null) {
                    return new FabricFluidContainer(container);
                }
                return null;
            }, block.get());
        }
    }

    @Override
    public void registerBlockEntities(BlockGetter<FluidContainer, @Nullable Direction> getter, Supplier<BlockEntityType<?>>... containers) {
        for (Supplier<BlockEntityType<?>> blockEntity : containers) {
            FluidStorage.SIDED.registerForBlockEntities((entity, context) -> {
                FluidContainer container = getter.getContainer(entity.getLevel(), entity.getBlockPos(), entity.getBlockState(), entity, context);
                if (container != null) {
                    return new FabricFluidContainer(container);
                }
                return null;
            }, blockEntity.get());
        }
    }
}
