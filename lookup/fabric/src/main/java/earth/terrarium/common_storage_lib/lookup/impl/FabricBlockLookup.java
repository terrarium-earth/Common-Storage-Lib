package earth.terrarium.common_storage_lib.lookup.impl;

import earth.terrarium.common_storage_lib.lookup.BlockLookup;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.lookup.v1.block.BlockApiLookup;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class FabricBlockLookup<T, C> implements BlockLookup<T, C> {
    private final BlockApiLookup<T, C> lookup;

    public FabricBlockLookup(BlockApiLookup<T, C> lookup) {
        this.lookup = lookup;
    }

    public FabricBlockLookup(ResourceLocation id, Class<T> type, Class<C> contextType) {
        this(BlockApiLookup.get(id, type, contextType));
    }

    @Override
    public @Nullable T find(Level level, BlockPos pos, @Nullable BlockState state, @Nullable BlockEntity entity, @Nullable C direction) {
        return lookup.find(level, pos, state, entity, direction);
    }

    @Override
    public void onRegister(Consumer<BlockRegistrar<T, C>> registrar) {
        registrar.accept(new LookupRegistrar());
    }

    @Override
    public void registerSelf(BlockGetter<T, C> getter, Block... blocks) {
        lookup.registerForBlocks(getter::getContainer, blocks);
    }

    @Override
    public void registerFallback(BlockGetter<T, C> getter, Predicate<Block> blockPredicate) {
        ServerLifecycleEvents.SERVER_STARTED.register(server -> BlockLookup.super.registerFallback(getter, blockPredicate));
    }

    @Override
    public void registerFallback(BlockGetter<T, C> getter) {
        lookup.registerFallback(getter::getContainer);
    }

    @Override
    public void registerFallback(BlockEntityGetter<T, C> getter, Predicate<BlockEntityType<?>> entityTypePredicate) {
        ServerLifecycleEvents.SERVER_STARTED.register(server -> BlockLookup.super.registerFallback(getter, entityTypePredicate));
    }

    @Override
    public void registerFallback(BlockEntityGetter<T, C> getter) {
        lookup.registerFallback((level, blockPos, blockState, blockEntity, c) -> getter.getContainer(blockEntity, c));
    }

    public class LookupRegistrar implements BlockRegistrar<T, C> {
        @Override
        public void registerBlocks(BlockGetter<T, C> getter, Block... blocks) {
            lookup.registerForBlocks(getter::getContainer, blocks);
        }

        @Override
        public void registerBlockEntities(BlockEntityGetter<T, C> getter, BlockEntityType<?>... containers) {
            lookup.registerForBlockEntities(getter::getContainer, containers);
        }
    }
}
