package earth.terrarium.botarium.common.registry;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.msrandom.multiplatform.annotations.Expect;
import org.jetbrains.annotations.NotNull;

public class RegistryHelpers {

    /**
     * @param factory The factory to create the block entity.
     * @param blocks  The blocks that the block entity can be attached to.
     * @return The created block entity type instance for the given factory.
     */
    @Expect
    public static <E extends BlockEntity> BlockEntityType<E> createBlockEntityType(BlockEntityFactory<E> factory, Block... blocks);

    @FunctionalInterface
    public interface BlockEntityFactory<T extends BlockEntity> {

        /**
         * @param blockPos   The position of the block entity.
         * @param blockState The state of the block the {@link BlockEntity} will be attached to.
         * @return The created block entity.
         */
        @NotNull T create(BlockPos blockPos, BlockState blockState);
    }
}
