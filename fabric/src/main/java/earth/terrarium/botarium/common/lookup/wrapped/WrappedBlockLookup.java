package earth.terrarium.botarium.common.lookup.wrapped;

import earth.terrarium.botarium.common.lookup.BlockLookup;
import earth.terrarium.botarium.common.storage.base.UnitContainer;
import earth.terrarium.botarium.common.storage.common.CommonWrappedContainer;
import earth.terrarium.botarium.common.storage.fabric.FabricWrappedContainer;
import earth.terrarium.botarium.common.storage.util.UpdateManager;
import earth.terrarium.botarium.common.transfer.base.TransferUnit;
import net.fabricmc.fabric.api.lookup.v1.block.BlockApiLookup;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.TransferVariant;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Function;

public class WrappedBlockLookup<T, U extends TransferUnit<T>, V extends TransferVariant<T>> implements BlockLookup<UnitContainer<U>, @Nullable Direction> {
    private final BlockApiLookup<Storage<V>, Direction> fabricLookup;
    private final Function<U, V> toVariant;
    private final Function<V, U> toUnit;

    public WrappedBlockLookup(BlockApiLookup<Storage<V>, Direction> fabricLookup, Function<U, V> toVariant, Function<V, U> toUnit) {
        this.fabricLookup = fabricLookup;
        this.toVariant = toVariant;
        this.toUnit = toUnit;
    }

    @Override
    public @Nullable UnitContainer<U> find(Level level, BlockPos pos, @Nullable BlockState state, @Nullable BlockEntity entity, @Nullable Direction direction) {
        Storage<V> storage = fabricLookup.find(level, pos, state, entity, direction);
        if (storage == null) {
            return null;
        }
        if (storage instanceof FabricWrappedContainer<T, U, V> fabric) {
            return fabric.getContainer();
        }
        return new CommonWrappedContainer<>(storage, toVariant, toUnit);
    }

    @Override
    public void onRegister(Consumer<BlockRegistrar<UnitContainer<U>, @Nullable Direction>> registrar) {
        registrar.accept(new LookupRegistrar());
    }

    public class LookupRegistrar implements BlockRegistrar<UnitContainer<U>, @Nullable Direction> {
        @Override
        public void registerBlocks(BlockGetter<UnitContainer<U>, @Nullable Direction> getter, Block... blocks) {
            fabricLookup.registerForBlocks((world, pos, state, blockEntity, context) -> {
                UnitContainer<U> container = getter.getContainer(world, pos, state, blockEntity, context);
                if (container != null) {
                    return new FabricWrappedContainer<>(container, toVariant, toUnit);
                }
                return null;
            }, blocks);
        }

        @Override
        public void registerBlockEntities(BlockEntityGetter<UnitContainer<U>, @Nullable Direction> getter, BlockEntityType<?>... blockEntities) {
            fabricLookup.registerForBlockEntities((entity, context) -> {
                UnitContainer<U> container = getter.getContainer(entity, context);
                if (container != null) {
                    return new FabricWrappedContainer<>(container, toVariant, toUnit);
                }
                return null;
            }, blockEntities);
        }
    }
}
