package earth.terrarium.botarium.common.registry;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.msrandom.extensions.annotations.ImplementedByExtension;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.NotNull;

public class RegistryHelpers {

    /**
     * @param factory The factory to create the block entity.
     * @param blocks  The blocks that the block entity can be attached to.
     * @return The created block entity type instance for the given factory.
     */
    @ImplementedByExtension
    public static <E extends BlockEntity> BlockEntityType<E> createBlockEntityType(BlockEntityFactory<E> factory, Block... blocks) {
        throw new AssertionError();
    }

    /**
     * @param factory The factory to create the menu.
     * @return The created menu type instance for the given factory.
     */
    @ImplementedByExtension
    public static <T extends AbstractContainerMenu> MenuType<T> createMenuType(MenuFactory<T> factory) {
        throw new NotImplementedException();
    }

    @FunctionalInterface
    public interface BlockEntityFactory<T extends BlockEntity> {

        /**
         * @param blockPos   The position of the block entity.
         * @param blockState The state of the block the {@link BlockEntity} will be attached to.
         * @return The created block entity.
         */
        @NotNull T create(BlockPos blockPos, BlockState blockState);
    }

    @FunctionalInterface
    public interface MenuFactory<T extends AbstractContainerMenu> {

        /**
         * @param syncId    The internal id for the menu.
         * @param inventory The inventory of the player.
         * @param byteBuf   The extra packet data for the menu.
         * @return The created menu instance.
         */
        T create(int syncId, Inventory inventory, FriendlyByteBuf byteBuf);
    }
}
