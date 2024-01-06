package earth.terrarium.botarium.common.fluid;

import earth.terrarium.botarium.Botarium;
import earth.terrarium.botarium.common.fluid.base.*;
import earth.terrarium.botarium.common.item.ItemStackHolder;
import earth.terrarium.botarium.util.Updatable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.msrandom.extensions.annotations.ImplementedByExtension;
import net.msrandom.extensions.annotations.ImplementsBaseElement;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class FluidApi {

    // region Fluid Container Registration
    private static final Map<Supplier<BlockEntityType<?>>, BotariumFluidBlock<?>> BLOCK_ENTITY_LOOKUP_MAP = new HashMap<>();
    private static final Map<Supplier<Block>, BotariumFluidBlock<?>> BLOCK_LOOKUP_MAP = new HashMap<>();
    private static final Map<Supplier<Item>, BotariumFluidItem<?>> ITEM_LOOKUP_MAP = new HashMap<>();

    public static final Map<BlockEntityType<?>, BotariumFluidBlock<?>> FINALIZED_BLOCK_ENTITY_LOOKUP_MAP = new HashMap<>();
    public static final Map<Block, BotariumFluidBlock<?>> FINALIZED_BLOCK_LOOKUP_MAP = new HashMap<>();
    public static boolean blocksFinalized = false;
    public static final Map<Item, BotariumFluidItem<?>> FINALIZED_ITEM_LOOKUP_MAP = new HashMap<>();
    public static boolean itemsFinalized = false;

    public static void finalizeBlockRegistration() {
        if (!blocksFinalized) {
            Botarium.LOGGER.debug("Finalizing fluid block registration");
            for (Map.Entry<Supplier<BlockEntityType<?>>, BotariumFluidBlock<?>> entry : BLOCK_ENTITY_LOOKUP_MAP.entrySet()) {
                FINALIZED_BLOCK_ENTITY_LOOKUP_MAP.put(entry.getKey().get(), entry.getValue());
            }
            for (Map.Entry<Supplier<Block>, BotariumFluidBlock<?>> entry : BLOCK_LOOKUP_MAP.entrySet()) {
                FINALIZED_BLOCK_LOOKUP_MAP.put(entry.getKey().get(), entry.getValue());
            }
            blocksFinalized = true;
        }
    }

    public static void finalizeItemRegistration() {
        if (!itemsFinalized) {
            Botarium.LOGGER.debug("Finalizing fluid item registration");
            for (Map.Entry<Supplier<Item>, BotariumFluidItem<?>> entry : ITEM_LOOKUP_MAP.entrySet()) {
                FINALIZED_ITEM_LOOKUP_MAP.put(entry.getKey().get(), entry.getValue());
            }
            itemsFinalized = true;
        }
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
     * @param from   The energy container to move energy from
     * @param to     The energy container to move energy to
     * @param fluidHolder The fluidHolder to move
     * @return The amount of fluid that was moved
     */
    public static long moveFluid(ItemStackHolder from, ItemStackHolder to, FluidHolder fluidHolder, boolean simulate) {
        if (!FluidContainer.holdsFluid(from.getStack()) || !FluidContainer.holdsFluid(to.getStack())) return 0;
        FluidContainer fromFluid =  FluidContainer.of(from);
        FluidContainer toFluid =  FluidContainer.of(to);
        return moveFluid(fromFluid, toFluid, fluidHolder, simulate);
    }

    /**
     * Moves energy from one energy container to another
     *
     * @param from   The energy container to move energy from
     * @param to     The energy container to move energy to
     * @param fluidHolder The amount of energy to move
     * @return The amount of energy that was moved
     */
    public static long moveFluid(BlockEntity from, BlockEntity to, FluidHolder fluidHolder, boolean simulate) {
        if (!FluidContainer.holdsFluid(from, null) || !FluidContainer.holdsFluid(to, null)) return 0;
        FluidContainer fromFluid =  FluidContainer.of(from, null);
        FluidContainer toFluid =  FluidContainer.of(to, null);
        return moveFluid(fromFluid, toFluid, fluidHolder, simulate);
    }

    /**
     * Moves energy from one energy container to another
     *
     * @param from   The energy container to move energy from
     * @param to     The energy container to move energy to
     * @param fluidHolder The amount of energy to move
     * @return The amount of energy that was moved
     */
    public static long moveFluid(BlockEntity from, Direction direction, ItemStackHolder to, FluidHolder fluidHolder, boolean simulate) {
        if (!FluidContainer.holdsFluid(from, direction) || !FluidContainer.holdsFluid(to.getStack())) return 0;
        FluidContainer fromFluid = FluidContainer.of(from, direction);
        FluidContainer toFluid =  FluidContainer.of(to);
        return moveFluid(fromFluid, toFluid, fluidHolder, simulate);
    }

    /**
     * Moves energy from one energy container to another
     *
     * @param from   The energy container to move energy from
     * @param to     The energy container to move energy to
     * @param fluidHolder The amount of energy to move
     * @return The amount of energy that was moved
     */
    public static long moveFluid(ItemStackHolder from, BlockEntity to, Direction direction, FluidHolder fluidHolder, boolean simulate) {
        if (!FluidContainer.holdsFluid(from.getStack()) || !FluidContainer.holdsFluid(to, direction)) return 0;
        FluidContainer fromFluid =  FluidContainer.of(from);
        FluidContainer toFluid =  FluidContainer.of(to, direction);
        return moveFluid(fromFluid, toFluid, fluidHolder, simulate);
    }
}
