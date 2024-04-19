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

import java.util.function.Consumer;
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
    public void onRegister(Consumer<BlockRegistrar<LongContainer, @Nullable Direction>> registrar) {
        registrar.accept(new LookupRegistrar());
    }

    public static class LookupRegistrar implements BlockRegistrar<LongContainer, @Nullable Direction> {
        @Override
        public void registerBlocks(BlockGetter<LongContainer, @Nullable Direction> getter, Block... blocks) {
            EnergyStorage.SIDED.registerForBlocks((world, pos, state, blockEntity, context) -> {
                LongContainer container = getter.getContainer(world, pos, state, blockEntity, context);
                if (container instanceof UpdateManager<?> updateManager) {
                    return new FabricLongStorage<>(container, updateManager);
                }
                return null;
            }, blocks);
        }

        @Override
        public void registerBlockEntities(BlockEntityGetter<LongContainer, @Nullable Direction> getter, BlockEntityType<?>... containers) {
            EnergyStorage.SIDED.registerForBlockEntities((entity, context) -> {
                LongContainer container = getter.getContainer(entity, context);
                if (container instanceof UpdateManager<?> updateManager) {
                    return new FabricLongStorage<>(container, updateManager);
                }
                return null;
            }, containers);
        }
    }
}
