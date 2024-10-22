package earth.terrarium.common_storage_lib.energy.lookup;

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

public class EnergyBlockLookup implements BlockLookup<ValueStorage, @Nullable Direction> {

    @Override
    public @Nullable ValueStorage find(Level level, BlockPos pos, @Nullable BlockState state, @Nullable BlockEntity entity, @Nullable Direction direction) {
        EnergyStorage storage = EnergyStorage.SIDED.find(level, pos, state, entity, direction);
        if (storage == null) {
            return null;
        }
        if (storage instanceof FabricLongStorage(ValueStorage rootContainer, var ignored)) {
            return rootContainer;
        }
        return new CommonValueStorage(storage);
    }

    @Override
    public void onRegister(Consumer<BlockRegistrar<ValueStorage, @Nullable Direction>> registrar) {
        registrar.accept(new LookupRegistrar());
    }

    public static class LookupRegistrar implements BlockRegistrar<ValueStorage, @Nullable Direction> {
        @Override
        public void registerBlocks(BlockGetter<ValueStorage, @Nullable Direction> getter, Block... blocks) {
            EnergyStorage.SIDED.registerForBlocks((world, pos, state, blockEntity, context) -> {
                ValueStorage container = getter.getContainer(world, pos, state, blockEntity, context);
                return new FabricLongStorage(container);
            }, blocks);
        }

        @Override
        public void registerBlockEntities(BlockEntityGetter<ValueStorage, @Nullable Direction> getter, BlockEntityType<?>... containers) {
            EnergyStorage.SIDED.registerForBlockEntities((entity, context) -> {
                ValueStorage container = getter.getContainer(entity, context);
                return new FabricLongStorage(container);
            }, containers);
        }
    }
}
