package earth.terrarium.common_storage_lib.energy.lookup;

import earth.terrarium.common_storage_lib.energy.impl.AutoUpdatingValueStorage;
import earth.terrarium.common_storage_lib.lookup.BlockLookup;
import earth.terrarium.common_storage_lib.storage.base.ValueStorage;
import earth.terrarium.common_storage_lib.storage.common.CommonValueStorage;
import earth.terrarium.common_storage_lib.storage.fabric.FabricLongStorage;
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
import java.util.function.Predicate;

public class EnergyBlockLookup implements BlockLookup<ValueStorage, @Nullable Direction> {

    @Override
    public @Nullable ValueStorage find(Level level, BlockPos pos, @Nullable BlockState state, @Nullable BlockEntity entity, @Nullable Direction direction) {
        EnergyStorage storage = EnergyStorage.SIDED.find(level, pos, state, entity, direction);
        if (storage == null) return null;
        if (storage instanceof FabricLongStorage(ValueStorage rootContainer, var ignored)) {
            return new AutoUpdatingValueStorage(rootContainer);
        }
        return new CommonValueStorage(storage);
    }

    @Override
    public void onRegister(Consumer<BlockRegistrar<ValueStorage, @Nullable Direction>> registrar) {
        registrar.accept(new LookupRegistrar());
    }

    @Override
    public void registerFallback(BlockGetter<ValueStorage, @Nullable Direction> getter, Predicate<Block> blockPredicate) {
        registerFallback(getter);
    }

    @Override
    public void registerFallback(BlockGetter<ValueStorage, @Nullable Direction> getter) {
        EnergyStorage.SIDED.registerFallback((level, pos, state, entity, dir) -> {
            ValueStorage storage = getter.getContainer(level, pos, state, entity, dir);
            return storage == null ? null : new FabricLongStorage(storage);
        });
    }

    @Override
    public void registerFallback(BlockEntityGetter<ValueStorage, @Nullable Direction> getter, Predicate<BlockEntityType<?>> entityTypePredicate) {
        registerFallback(getter);
    }

    @Override
    public void registerFallback(BlockEntityGetter<ValueStorage, @Nullable Direction> getter) {
        EnergyStorage.SIDED.registerFallback((level, blockPos, blockState, blockEntity, direction) -> {
            var be = blockEntity;
            if (be == null) be = level.getBlockEntity(blockPos);
            if (be == null) return null;
            ValueStorage storage = getter.getContainer(be, direction);
            return storage == null ? null : new FabricLongStorage(storage);
        });
    }

    public static class LookupRegistrar implements BlockRegistrar<ValueStorage, @Nullable Direction> {
        @Override
        public void registerBlocks(BlockGetter<ValueStorage, @Nullable Direction> getter, Block... blocks) {
            EnergyStorage.SIDED.registerForBlocks((world, pos, state, blockEntity, context) -> {
                ValueStorage storage = getter.getContainer(world, pos, state, blockEntity, context);
                return storage == null ? null : new FabricLongStorage(storage);
            }, blocks);
        }

        @Override
        public void registerBlockEntities(BlockEntityGetter<ValueStorage, @Nullable Direction> getter, BlockEntityType<?>... containers) {
            EnergyStorage.SIDED.registerForBlockEntities((entity, context) -> {
                ValueStorage storage = getter.getContainer(entity, context);
                return storage == null ? null : new FabricLongStorage(storage);
            }, containers);
        }
    }
}
