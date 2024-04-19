package earth.terrarium.botarium.common.lookup;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.msrandom.multiplatform.annotations.Expect;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Supplier;

@SuppressWarnings("unchecked")
public interface BlockLookup<T, C> {

    static <T, C> BlockLookup<T, C> create(ResourceLocation name, Class<T> typeClass, Class<C> contextClass) {
        return null;
    }

    static <T> BlockLookup<T, @Nullable Direction> create(ResourceLocation name, Class<T> typeClass) {
        return create(name, typeClass, Direction.class);
    }

    /**
     * @return The {@link T} for the block.
     */
    @Nullable
    T find(Level level, BlockPos pos, @Nullable BlockState state, @Nullable BlockEntity entity, @Nullable C direction);

    @Nullable
    default T find(BlockEntity block, @Nullable C direction) {
        return find(block.getLevel(), block.getBlockPos(), block.getBlockState(), block, direction);
    }

    @Nullable
    default T find(Level level, BlockPos pos, @Nullable C direction) {
        return find(level, pos, null, null, direction);
    }

    void onRegister(Consumer<BlockRegistrar<T, C>> registrar);

    interface BlockGetter<T, C> {
        @Nullable
        T getContainer(Level level, BlockPos pos, BlockState state, @Nullable BlockEntity entity, @Nullable C direction);
    }

    interface BlockRegistrar<T, C> {
        void registerBlocks(BlockGetter<T, C> getter, Block... containers);

        void registerBlockEntities(BlockGetter<T, C> getter, BlockEntityType<?>... containers);
    }
}
