package earth.terrarium.botarium.common.fluid;

import earth.terrarium.botarium.Botarium;
import earth.terrarium.botarium.common.fluid.base.BotariumFluidBlock;
import earth.terrarium.botarium.common.fluid.base.BotariumFluidItem;
import earth.terrarium.botarium.common.fluid.base.FluidContainer;
import earth.terrarium.botarium.common.fluid.base.FluidHolder;
import earth.terrarium.botarium.common.item.ItemStackHolder;
import earth.terrarium.botarium.util.Updatable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class FluidApi {

    // region Fluid Container Registration
    private static final Map<Supplier<BlockEntityType<?>>, BotariumFluidBlock<?>> BLOCK_ENTITY_LOOKUP_MAP = new HashMap<>();
    private static final Map<Supplier<Block>, BotariumFluidBlock<?>> BLOCK_LOOKUP_MAP = new HashMap<>();
    private static final Map<Supplier<Item>, BotariumFluidItem<?>> ITEM_LOOKUP_MAP = new HashMap<>();

    private static Map<BlockEntityType<?>, BotariumFluidBlock<?>> FINALIZED_BLOCK_ENTITY_LOOKUP_MAP = null;
    private static Map<Block, BotariumFluidBlock<?>> FINALIZED_BLOCK_LOOKUP_MAP = null;
    private static Map<Item, BotariumFluidItem<?>> FINALIZED_ITEM_LOOKUP_MAP = null;

    public static Map<BlockEntityType<?>, BotariumFluidBlock<?>> getBlockEntityRegistry() {
        return FINALIZED_BLOCK_ENTITY_LOOKUP_MAP = Botarium.finalizeRegistration(BLOCK_ENTITY_LOOKUP_MAP, FINALIZED_BLOCK_ENTITY_LOOKUP_MAP, "fluid containing block entity");
    }

    public static Map<Block, BotariumFluidBlock<?>> getBlockRegistry() {
        return FINALIZED_BLOCK_LOOKUP_MAP = Botarium.finalizeRegistration(BLOCK_LOOKUP_MAP, FINALIZED_BLOCK_LOOKUP_MAP, "fluid containing block");
    }

    public static Map<Item, BotariumFluidItem<?>> getItemRegistry() {
        return FINALIZED_ITEM_LOOKUP_MAP = Botarium.finalizeRegistration(ITEM_LOOKUP_MAP, FINALIZED_ITEM_LOOKUP_MAP, "fluid containing item");
    }

    public static BotariumFluidBlock<?> getFluidBlock(Block block) {
        return getBlockRegistry().get(block);
    }

    public static BotariumFluidBlock<?> getFluidBlock(BlockEntityType<?> blockEntity) {
        return getBlockEntityRegistry().get(blockEntity);
    }

    public static BotariumFluidItem<?> getFluidItem(Item item) {
        return getItemRegistry().get(item);
    }

    /**
     * Retrieves the Botarium specific FluidContainer object from a block entity or block. This method is used internally
     * by the Botarium API and should not be used by other mods.
     *
     * @param <T>       the type of FluidContainer
     * @param level     the game level
     * @param pos       the position of the block
     * @param state     the block state
     * @param entity    the block entity (can be null)
     * @param direction the direction (can be null)
     * @return the API FluidContainer object
     */
    public static <T extends FluidContainer & Updatable> T getAPIFluidContainer(Level level, BlockPos pos, BlockState state, @Nullable BlockEntity entity, @Nullable Direction direction) {
        BotariumFluidBlock<?> getter = getFluidBlock(state.getBlock());
        if (getter == null && entity != null) {
            getter = getFluidBlock(entity.getType());

            if (getter == null && entity instanceof BotariumFluidBlock<?> fluidGetter) {
                getter = fluidGetter;
            }
        }
        if (getter == null) return null;
        return (T) getter.getFluidContainer(level, pos, state, entity, direction);
    }

    public static void registerFluidBlockEntity(Supplier<BlockEntityType<?>> block, BotariumFluidBlock<?> getter) {
        BLOCK_ENTITY_LOOKUP_MAP.put(block, getter);
    }

    @SafeVarargs
    public static void registerFluidBlockEntity(BotariumFluidBlock<?> getter, Supplier<BlockEntityType<?>>... blocks) {
        for (Supplier<BlockEntityType<?>> block : blocks) {
            BLOCK_ENTITY_LOOKUP_MAP.put(block, getter);
        }
    }

    public static void registerFluidBlock(Supplier<Block> block, BotariumFluidBlock<?> getter) {
        BLOCK_LOOKUP_MAP.put(block, getter);
    }

    @SafeVarargs
    public static void registerFluidBlock(BotariumFluidBlock<?> getter, Supplier<Block>... blocks) {
        for (Supplier<Block> block : blocks) {
            BLOCK_LOOKUP_MAP.put(block, getter);
        }
    }

    public static void registerFluidItem(Supplier<Item> item, BotariumFluidItem<?> getter) {
        ITEM_LOOKUP_MAP.put(item, getter);
    }

    @SafeVarargs
    public static void registerFluidItem(BotariumFluidItem<?> getter, Supplier<Item>... items) {
        for (Supplier<Item> item : items) {
            ITEM_LOOKUP_MAP.put(item, getter);
        }
    }

    /**
     * Moves energy from one energy container to another
     *
     * @param from   The energy container to move energy from
     * @param to     The energy container to move energy to
     * @param amount The amount of energy to move
     * @return The amount of energy that was moved
     */
    public static long moveFluid(FluidContainer from, FluidContainer to, FluidHolder amount, boolean simulate) {
        FluidHolder extracted = from.extractFluid(amount, true);
        long inserted = to.insertFluid(extracted, true);
        FluidHolder toInsert = amount.copyWithAmount(inserted);
        FluidHolder simulatedExtraction = from.extractFluid(toInsert, true);
        if (!simulate && inserted > 0 && simulatedExtraction.getFluidAmount() == inserted) {
            from.extractFluid(toInsert, false);
            to.insertFluid(toInsert, false);
        }
        return Math.max(0, inserted);
    }

    /**
     * Moves energy from one energy container to another
     *
     * @param from        The energy container to move energy from
     * @param to          The energy container to move energy to
     * @param fluidHolder The fluidHolder to move
     * @return The amount of fluid that was moved
     */
    public static long moveFluid(ItemStackHolder from, ItemStackHolder to, FluidHolder fluidHolder, boolean simulate) {
        FluidContainer fromFluid = FluidContainer.of(from);
        FluidContainer toFluid = FluidContainer.of(to);
        if (fromFluid == null || toFluid == null) return 0;
        return moveFluid(fromFluid, toFluid, fluidHolder, simulate);
    }

    /**
     * Moves energy from one energy container to another
     *
     * @param from        The energy container to move energy from
     * @param to          The energy container to move energy to
     * @param fluidHolder The amount of energy to move
     * @return The amount of energy that was moved
     */
    public static long moveFluid(BlockEntity from, BlockEntity to, FluidHolder fluidHolder, boolean simulate) {
        FluidContainer fromFluid = FluidContainer.of(from, null);
        FluidContainer toFluid = FluidContainer.of(to, null);
        if (fromFluid == null || toFluid == null) return 0;
        return moveFluid(fromFluid, toFluid, fluidHolder, simulate);
    }

    public static long moveFluid(Level level, BlockPos fromPos, @Nullable Direction fromDirection, BlockPos toPos, @Nullable Direction toDirection, FluidHolder fluidHolder, boolean simulate) {
        FluidContainer fromFluid = FluidContainer.of(level, fromPos, fromDirection);
        FluidContainer toFluid = FluidContainer.of(level, toPos, toDirection);
        if (fromFluid == null || toFluid == null) return 0;
        return moveFluid(fromFluid, toFluid, fluidHolder, simulate);
    }

    /**
     * Moves energy from one energy container to another
     *
     * @param from        The energy container to move energy from
     * @param to          The energy container to move energy to
     * @param fluidHolder The amount of energy to move
     * @return The amount of energy that was moved
     */
    public static long moveFluid(BlockEntity from, Direction direction, ItemStackHolder to, FluidHolder fluidHolder, boolean simulate) {
        FluidContainer fromFluid = FluidContainer.of(from, direction);
        FluidContainer toFluid = FluidContainer.of(to);
        if (fromFluid == null || toFluid == null) return 0;
        return moveFluid(fromFluid, toFluid, fluidHolder, simulate);
    }

    /**
     * Moves energy from one energy container to another
     *
     * @param from        The energy container to move energy from
     * @param to          The energy container to move energy to
     * @param fluidHolder The amount of energy to move
     * @return The amount of energy that was moved
     */
    public static long moveFluid(ItemStackHolder from, BlockEntity to, Direction direction, FluidHolder fluidHolder, boolean simulate) {
        FluidContainer fromFluid = FluidContainer.of(from);
        FluidContainer toFluid = FluidContainer.of(to, direction);
        if (fromFluid == null || toFluid == null) return 0;
        return moveFluid(fromFluid, toFluid, fluidHolder, simulate);
    }
}
