package earth.terrarium.botarium.wrapped;

import earth.terrarium.botarium.resources.fluid.FluidResource;
import earth.terrarium.botarium.resources.item.ItemResource;
import earth.terrarium.botarium.storage.ConversionUtils;
import earth.terrarium.botarium.resources.Resource;
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

public abstract class WrappedBlockLookup<U extends Resource, V extends TransferVariant<?>> implements BlockLookup<CommonStorage<U>, @Nullable Direction> {
    private final BlockApiLookup<Storage<V>, Direction> fabricLookup;

    public WrappedBlockLookup(BlockApiLookup<Storage<V>, Direction> fabricLookup) {
        this.fabricLookup = fabricLookup;
    }

    @Override
    public void onRegister(Consumer<BlockLookup.BlockRegistrar<CommonStorage<U>, @Nullable Direction>> registrar) {
        registrar.accept((getter, blockEntityTypes) -> fabricLookup.registerForBlockEntities((entity, context) -> {
            CommonStorage<U> container = getter.getContainer(entity, context);
            if (container != null) {
                return wrap(container);
            }
            return null;
        }, blockEntityTypes));
    }

    public abstract FabricWrappedContainer<U, V> wrap(CommonStorage<U> container);

    public static class ofFluid extends WrappedBlockLookup<FluidResource, FluidVariant> {
        public ofFluid() {
            super(FluidStorage.SIDED);
        }


        @Override
        public FabricWrappedContainer<FluidResource, FluidVariant> wrap(CommonStorage<FluidResource> container) {
            return new FabricWrappedContainer.OfFluid(container);
        }

        @Override
        public @Nullable CommonStorage<FluidResource> find(BlockEntity block, @Nullable Direction context) {
            Storage<FluidVariant> storage = FluidStorage.SIDED.find(block.getLevel(), block.getBlockPos(), context);
            if (storage != null) {
                if (storage instanceof FabricWrappedContainer.OfFluid wrappedContainer) {
                    return wrappedContainer.container();
                }
                return new CommonWrappedContainer<>(storage, ConversionUtils::toVariant, ConversionUtils::toResource);
            }
            return null;
        }
    }

    public static class ofItem extends WrappedBlockLookup<ItemResource, ItemVariant> {
        public ofItem() {
            super(ItemStorage.SIDED);
        }



        @Override
        public FabricWrappedContainer<ItemResource, ItemVariant> wrap(CommonStorage<ItemResource> container) {
            return new FabricWrappedContainer.OfItem(container);
        }

        @Override
        public @Nullable CommonStorage<ItemResource> find(BlockEntity block, @Nullable Direction context) {
            Storage<ItemVariant> storage = ItemStorage.SIDED.find(block.getLevel(), block.getBlockPos(), context);
            if (storage != null) {
                if (storage instanceof FabricWrappedContainer.OfItem wrappedContainer) {
                    return wrappedContainer.container();
                }
                return new CommonWrappedContainer<>(storage, ConversionUtils::toVariant, ConversionUtils::toResource);
            }
            return null;
        }
    }
}
