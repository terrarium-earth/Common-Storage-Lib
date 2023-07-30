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
import java.util.function.Supplier;

public class FluidApi {
    private static final Map<Supplier<BlockEntityType<?>>, BlockFluidGetter<?>> BLOCK_ENTITY_LOOKUP_MAP = new HashMap<>();
    private static final Map<Supplier<Block>, BlockFluidGetter<?>> BLOCK_LOOKUP_MAP = new HashMap<>();
    private static final Map<Supplier<Item>, ItemFluidGetter<?>> ITEM_LOOKUP_MAP = new HashMap<>();

    public static final Map<BlockEntityType<?>, BlockFluidGetter<?>> FINALIZED_BLOCK_ENTITY_LOOKUP_MAP = new HashMap<>();
    public static final Map<Block, BlockFluidGetter<?>> FINALIZED_BLOCK_LOOKUP_MAP = new HashMap<>();
    public static boolean blocksFinalized = false;
    public static final Map<Item, ItemFluidGetter<?>> FINALIZED_ITEM_LOOKUP_MAP = new HashMap<>();
    public static boolean itemsFinalized = false;

    public static void finalizeBlockRegistration() {
        if (!blocksFinalized) {
            System.out.println("Finalizing fluid block registration");
            for (Map.Entry<Supplier<BlockEntityType<?>>, BlockFluidGetter<?>> entry : BLOCK_ENTITY_LOOKUP_MAP.entrySet()) {
                FINALIZED_BLOCK_ENTITY_LOOKUP_MAP.put(entry.getKey().get(), entry.getValue());
            }
            for (Map.Entry<Supplier<Block>, BlockFluidGetter<?>> entry : BLOCK_LOOKUP_MAP.entrySet()) {
                FINALIZED_BLOCK_LOOKUP_MAP.put(entry.getKey().get(), entry.getValue());
            }
            blocksFinalized = true;
        }
    }

    public static void finalizeItemRegistration() {
        if (!itemsFinalized) {
            System.out.println("Finalizing fluid item registration");
            for (Map.Entry<Supplier<Item>, ItemFluidGetter<?>> entry : ITEM_LOOKUP_MAP.entrySet()) {
                FINALIZED_ITEM_LOOKUP_MAP.put(entry.getKey().get(), entry.getValue());
            }
            itemsFinalized = true;
        }
    }

    public static void registerFluidBlockEntity(Supplier<BlockEntityType<?>> block, BlockFluidGetter<?> getter) {
        BLOCK_ENTITY_LOOKUP_MAP.put(block, getter);
    }

    @SafeVarargs
    public static void registerFluidBlockEntity(BlockFluidGetter<?> getter, Supplier<BlockEntityType<?>>... blocks) {
        for (Supplier<BlockEntityType<?>> block : blocks) {
            BLOCK_ENTITY_LOOKUP_MAP.put(block, getter);
        }
    }

    public static void registerFluidBlock(Supplier<Block> block, BlockFluidGetter<?> getter) {
        BLOCK_LOOKUP_MAP.put(block, getter);
    }

    @SafeVarargs
    public static void registerFluidBlock(BlockFluidGetter<?> getter, Supplier<Block>... blocks) {
        for (Supplier<Block> block : blocks) {
            BLOCK_LOOKUP_MAP.put(block, getter);
        }
    }

    public static void registerFluidItem(Supplier<Item> item, ItemFluidGetter<?> getter) {
        ITEM_LOOKUP_MAP.put(item, getter);
    }

    @SafeVarargs
    public static void registerFluidItem(ItemFluidGetter<?> getter, Supplier<Item>... items) {
        for (Supplier<Item> item : items) {
            ITEM_LOOKUP_MAP.put(item, getter);
        }
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
