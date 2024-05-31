package earth.terrarium.botarium.lookup;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.msrandom.multiplatform.annotations.Expect;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Predicate;

@SuppressWarnings("unchecked")
public interface BlockLookup<T, C> {

    @Expect
    static <T, C> BlockLookup<T, C> create(ResourceLocation name, Class<T> typeClass, Class<C> contextClass);

    static <T> BlockLookup<T, @Nullable Direction> create(ResourceLocation name, Class<T> typeClass) {
        return create(name, typeClass, Direction.class);
    }

    default T find(Level level, BlockPos pos, @Nullable C direction) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        return blockEntity != null ? find(blockEntity, direction) : null;
    }

    @Nullable
    T find(BlockEntity block, C context);

    default boolean isPresent(BlockEntity block, C context) {
        return find(block, context) != null;
    }

    default void registerFallback(BlockEntityGetter<T, C> getter, Predicate<BlockEntityType<?>> entityTypePredicate) {
        onRegister(registrar -> {
            for (BlockEntityType<?> entityType : BuiltInRegistries.BLOCK_ENTITY_TYPE) {
                if (entityTypePredicate.test(entityType)) {
                    registrar.registerBlockEntities(getter, entityType);
                }
            }
        });
    }

    default void registerFallback(BlockEntityGetter<T, C> getter) {
        onRegister(registrar -> {
            for (BlockEntityType<?> entityType : BuiltInRegistries.BLOCK_ENTITY_TYPE) {
                registrar.registerBlockEntities(getter, entityType);
            }
        });
    }

    void onRegister(Consumer<BlockRegistrar<T, C>> registrar);

    interface BlockEntityGetter<T, C> {
        @Nullable
        T getContainer(BlockEntity entity, @Nullable C direction);
    }

    interface BlockRegistrar<T, C> {
        void registerBlockEntities(BlockEntityGetter<T, C> getter, BlockEntityType<?>... blockEntityTypes);
    }
}
