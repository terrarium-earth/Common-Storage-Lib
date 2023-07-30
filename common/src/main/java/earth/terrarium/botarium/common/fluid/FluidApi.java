package earth.terrarium.botarium.common.fluid;

import earth.terrarium.botarium.common.fluid.base.FluidContainer;
import earth.terrarium.botarium.common.fluid.base.ItemFluidContainer;
import earth.terrarium.botarium.common.fluid.base.PlatformFluidHandler;
import earth.terrarium.botarium.common.fluid.impl.WrappedBlockFluidContainer;
import earth.terrarium.botarium.common.fluid.impl.WrappedItemFluidContainer;
import earth.terrarium.botarium.common.item.ItemStackHolder;
import earth.terrarium.botarium.util.Updatable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.msrandom.extensions.annotations.ImplementedByExtension;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class FluidApi {
    public static final Map<BlockEntityType<?>, BlockFluidGetter<?>> BLOCK_ENTITY_LOOKUP_MAP = new HashMap<>();
    public static final Map<Block, BlockFluidGetter<?>> BLOCK_LOOKUP_MAP = new HashMap<>();
    public static final Map<Item, ItemFluidGetter<?>> ITEM_LOOKUP_MAP = new HashMap<>();

    public static void registerFluidBlock(BlockEntityType<?> block, BlockFluidGetter<?> getter) {
        BLOCK_ENTITY_LOOKUP_MAP.put(block, getter);
    }

    public static void registerFluidBlock(BlockFluidGetter<?> getter, BlockEntityType<?>... blocks) {
        for (BlockEntityType<?> block : blocks) {
            BLOCK_ENTITY_LOOKUP_MAP.put(block, getter);
        }
    }

    public static void registerFluidBlock(Block block, BlockFluidGetter<?> getter) {
        BLOCK_LOOKUP_MAP.put(block, getter);
    }

    public static void registerFluidBlock(BlockFluidGetter<?> getter, Block... blocks) {
        for (Block block : blocks) {
            BLOCK_LOOKUP_MAP.put(block, getter);
        }
    }

    public static void registerDefaultFluidBlock(Block block, FluidContainer container) {
        registerFluidBlock(block, (level, pos, state, entity, direction) -> new WrappedBlockFluidContainer(entity, container));
    }

    public static void registerDefaultFluidBlock(FluidContainer container, Block... blocks) {
        registerFluidBlock((level, pos, state, entity, direction) -> new WrappedBlockFluidContainer(entity, container), blocks);
    }

    public static void registerDefaultFluidBlock(BlockEntityType<?> block, FluidContainer container) {
        registerFluidBlock(block, (level, pos, state, entity, direction) -> new WrappedBlockFluidContainer(entity, container));
    }

    public static void registerDefaultFluidBlock(FluidContainer container, BlockEntityType<?>... blocks) {
        registerFluidBlock((level, pos, state, entity, direction) -> new WrappedBlockFluidContainer(entity, container), blocks);
    }

    public static void registerFluidItem(Item item, ItemFluidGetter<?> getter) {
        ITEM_LOOKUP_MAP.put(item, getter);
    }

    public static void registerFluidItem(ItemFluidGetter<?> getter, Item... items) {
        for (Item item : items) {
            ITEM_LOOKUP_MAP.put(item, getter);
        }
    }

    public static void registerDefaultFluidItem(Item item, FluidContainer container) {
        registerFluidItem(item, stack -> new WrappedItemFluidContainer(stack, container));
    }

    public static void registerDefaultFluidItem(FluidContainer container, Item... items) {
        registerFluidItem(stack -> new WrappedItemFluidContainer(stack, container), items);
    }

    /**
     * Gets the {@link PlatformFluidHandler} for an {@link ItemStack}.
     *
     * @param stack The {@link ItemStack} to get the {@link PlatformFluidHandler} from.
     * @return The {@link PlatformFluidHandler} for the {@link ItemStack}.
     * @throws IllegalArgumentException If the {@link ItemStack} does not have a {@link PlatformFluidHandler}.
     */
    @ImplementedByExtension
    public static ItemFluidContainer getItemFluidContainer(ItemStackHolder stack) {
        throw new NotImplementedException("Item Fluid Manager not Implemented");
    }

    /**
     * Gets the {@link PlatformFluidHandler} for a {@link BlockEntity}.
     *
     * @param entity    The {@link BlockEntity} to get the {@link PlatformFluidHandler} from.
     * @param direction The {@link Direction} to get the {@link PlatformFluidHandler} from on the {@link BlockEntity}.
     * @return The {@link PlatformFluidHandler} for the {@link BlockEntity} and {@link Direction}.
     * @throws IllegalArgumentException If the {@link BlockEntity} does not have a {@link PlatformFluidHandler}.
     */
    @ImplementedByExtension
    public static FluidContainer getBlockFluidContainer(BlockEntity entity, @Nullable Direction direction) {
        throw new NotImplementedException("Block Entity Fluid manager not implemented");
    }

    /**
     * @param entity    The {@link BlockEntity} to check if it is a fluid container.
     * @param direction The {@link Direction} to check on the {@link BlockEntity} for a fluid container.
     * @return True if the {@link BlockEntity} is a fluid container.
     */
    @ImplementedByExtension
    public static boolean isFluidContainingBlock(BlockEntity entity, @Nullable Direction direction) {
        throw new NotImplementedException();
    }

    /**
     * @param stack The {@link ItemStack} to check if it is a fluid container.
     * @return True if the {@link ItemStack} is a fluid container.
     */
    @ImplementedByExtension
    public static boolean isFluidContainingItem(ItemStack stack) {
        throw new NotImplementedException();
    }

    @FunctionalInterface
    public interface BlockFluidGetter<T extends FluidContainer & Updatable<BlockEntity>>  {
        T getFluidContainer(Level level, BlockPos pos, BlockState state, BlockEntity entity, Direction direction);
    }

    @FunctionalInterface
    public interface ItemFluidGetter<T extends ItemFluidContainer & Updatable<ItemStack>>  {
        T getFluidContainer(ItemStack stack);
    }
}
