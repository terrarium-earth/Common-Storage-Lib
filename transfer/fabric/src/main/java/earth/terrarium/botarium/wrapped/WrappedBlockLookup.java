package earth.terrarium.botarium.wrapped;

import earth.terrarium.botarium.fluid.base.FluidUnit;
import earth.terrarium.botarium.item.base.ItemUnit;
import earth.terrarium.botarium.storage.ConversionUtils;
import earth.terrarium.botarium.storage.unit.TransferUnit;
import earth.terrarium.botarium.lookup.BlockLookup;
import earth.terrarium.botarium.storage.base.CommonStorage;
import earth.terrarium.botarium.storage.common.CommonWrappedContainer;
import earth.terrarium.botarium.storage.fabric.FabricWrappedContainer;
import net.fabricmc.fabric.api.lookup.v1.block.BlockApiLookup;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.TransferVariant;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public abstract class WrappedBlockLookup<T, U extends TransferUnit<T, U>, V extends TransferVariant<T>> implements BlockLookup<CommonStorage<U>, @Nullable Direction> {
    private final BlockApiLookup<Storage<V>, Direction> fabricLookup;

    public WrappedBlockLookup(BlockApiLookup<Storage<V>, Direction> fabricLookup) {
        this.fabricLookup = fabricLookup;
    }

    @Override
    public void onRegister(Consumer<BlockLookup.BlockRegistrar<CommonStorage<U>, @Nullable Direction>> registrar) {
        registrar.accept(new LookupRegistrar());
    }

    public class LookupRegistrar implements BlockRegistrar<CommonStorage<U>, @Nullable Direction> {
        @Override
        public void registerBlocks(BlockGetter<CommonStorage<U>, @Nullable Direction> getter, Block... blocks) {
            fabricLookup.registerForBlocks((world, pos, state, blockEntity, context) -> {
                CommonStorage<U> container = getter.getContainer(world, pos, state, blockEntity, context);
                if (container != null) {
                    return wrap(container);
                }
                return null;
            }, blocks);
        }

        @Override
        public void registerBlockEntities(BlockEntityGetter<CommonStorage<U>, @Nullable Direction> getter, BlockEntityType<?>... blockEntities) {
            fabricLookup.registerForBlockEntities((entity, context) -> {
                CommonStorage<U> container = getter.getContainer(entity, context);
                if (container != null) {
                    return wrap(container);
                }
                return null;
            }, blockEntities);
        }
    }

    public abstract FabricWrappedContainer<T, U, V> wrap(CommonStorage<U> container);

    public static class ofFluid extends WrappedBlockLookup<Fluid, FluidUnit, FluidVariant> {
        public ofFluid() {
            super(FluidStorage.SIDED);
        }

        @Override
        public @Nullable CommonStorage<FluidUnit> find(Level level, BlockPos pos, @Nullable BlockState state, @Nullable BlockEntity entity, @Nullable Direction direction) {
            Storage<FluidVariant> storage = FluidStorage.SIDED.find(level, pos, state, entity, direction);
            if (storage != null) {
                if (storage instanceof FabricWrappedContainer.OfFluid wrappedContainer) {
                    return wrappedContainer.container();
                }
                return new CommonWrappedContainer<>(storage, ConversionUtils::toVariant, ConversionUtils::toUnit);
            }
            return null;
        }

        @Override
        public FabricWrappedContainer<Fluid, FluidUnit, FluidVariant> wrap(CommonStorage<FluidUnit> container) {
            return new FabricWrappedContainer.OfFluid(container);
        }
    }

    public static class ofItem extends WrappedBlockLookup<Item, ItemUnit, ItemVariant> {
        public ofItem() {
            super(ItemStorage.SIDED);
        }

        @Override
        public @Nullable CommonStorage<ItemUnit> find(Level level, BlockPos pos, @Nullable BlockState state, @Nullable BlockEntity entity, @Nullable Direction direction) {
            Storage<ItemVariant> storage = ItemStorage.SIDED.find(level, pos, state, entity, direction);
            if (storage != null) {
                if (storage instanceof FabricWrappedContainer.OfItem wrappedContainer) {
                    return wrappedContainer.container();
                }
                return new CommonWrappedContainer<>(storage, ConversionUtils::toVariant, ConversionUtils::toUnit);
            }
            return null;
        }

        @Override
        public FabricWrappedContainer<Item, ItemUnit, ItemVariant> wrap(CommonStorage<ItemUnit> container) {
            return new FabricWrappedContainer.OfItem(container);
        }
    }
}
