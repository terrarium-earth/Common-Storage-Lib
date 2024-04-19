package earth.terrarium.botarium.common.energy.lookup;

import earth.terrarium.botarium.common.lookup.BlockLookup;
import earth.terrarium.botarium.common.storage.base.LongContainer;
import earth.terrarium.botarium.common.storage.common.CommonLongContainer;
import earth.terrarium.botarium.common.storage.fabric.FabricLongStorage;
import earth.terrarium.botarium.common.storage.util.UpdateManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import team.reborn.energy.api.EnergyStorage;

import java.util.function.Supplier;

public class EnergyBlockLookup implements BlockLookup<LongContainer, @Nullable Direction> {

    @Override
    public @Nullable LongContainer find(Level level, BlockPos pos, @Nullable BlockState state, @Nullable BlockEntity entity, @Nullable Direction direction) {
        EnergyStorage storage = EnergyStorage.SIDED.find(level, pos, state, entity, direction);
        if (storage == null) {
            return null;
        }
        if (storage instanceof FabricLongStorage<?> fabric) {
            return fabric.getContainer();
        }
        return new CommonLongContainer(storage);
    }

    @Override
    public void registerBlocks(BlockGetter<LongContainer, @Nullable Direction> getter, Supplier<Block>... containers) {
        for (Supplier<Block> block : containers) {
            EnergyStorage.SIDED.registerForBlocks((world, pos, state, blockEntity, context) -> {
                LongContainer container = getter.getContainer(world, pos, state, blockEntity, context);
                if (container instanceof UpdateManager<?> updateManager) {
                    return new FabricLongStorage<>(container, updateManager);
                }
                return null;
            }, block.get());
        }
    }

    @Override
    public void registerBlockEntities(BlockGetter<LongContainer, @Nullable Direction> getter, Supplier<BlockEntityType<?>>... containers) {
        for (Supplier<BlockEntityType<?>> blockEntity : containers) {
            EnergyStorage.SIDED.registerForBlockEntities((entity, context) -> {
                LongContainer container = getter.getContainer(entity.getLevel(), entity.getBlockPos(), entity.getBlockState(), entity, context);
                if (container instanceof UpdateManager<?> updateManager) {
                    return new FabricLongStorage<>(container, updateManager);
                }
                return null;
            }, blockEntity.get());
        }
    }
}
