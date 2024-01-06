package earth.terrarium.botarium.common.energy;

import com.mojang.datafixers.util.Pair;
import earth.terrarium.botarium.Botarium;
import earth.terrarium.botarium.common.energy.base.BotariumEnergyBlock;
import earth.terrarium.botarium.common.energy.base.BotariumEnergyItem;
import earth.terrarium.botarium.common.energy.base.EnergyContainer;
import earth.terrarium.botarium.common.item.ItemStackHolder;
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
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@SuppressWarnings("DataFlowIssue")
public class EnergyApi {
    private static final Map<Supplier<BlockEntityType<?>>, BotariumEnergyBlock<?>> BLOCK_ENTITY_LOOKUP_MAP = new HashMap<>();
    private static final Map<Supplier<Block>, BotariumEnergyBlock<?>> BLOCK_LOOKUP_MAP = new HashMap<>();
    private static final Map<Supplier<Item>, BotariumEnergyItem<?>> ITEM_LOOKUP_MAP = new HashMap<>();

    private static Map<BlockEntityType<?>, BotariumEnergyBlock<?>> FINALIZED_BLOCK_ENTITY_LOOKUP_MAP = null;
    private static Map<Block, BotariumEnergyBlock<?>> FINALIZED_BLOCK_LOOKUP_MAP = null;
    private static Map<Item, BotariumEnergyItem<?>> FINALIZED_ITEM_LOOKUP_MAP = null;

    private static <T, U> Map<T, U> finalizeRegistration(Map<Supplier<T>, U> unfinalized, @Nullable Map<T, U> finalized, String type) {
        if (finalized == null) {
            Botarium.LOGGER.debug("Finalizing energy {} registration", type);
            Map<T, U> collected = unfinalized.entrySet().stream().map(entry -> Pair.of(entry.getKey().get(), entry.getValue())).collect(Collectors.toUnmodifiableMap(Pair::getFirst, Pair::getSecond));
            unfinalized.clear();
            return collected;
        }

        return finalized;
    }

    public static Map<BlockEntityType<?>, BotariumEnergyBlock<?>> getBlockEntityRegistry() {
        return FINALIZED_BLOCK_ENTITY_LOOKUP_MAP = finalizeRegistration(BLOCK_ENTITY_LOOKUP_MAP, FINALIZED_BLOCK_ENTITY_LOOKUP_MAP, "block entity");
    }

    public static Map<Block, BotariumEnergyBlock<?>> getBlockRegistry() {
        return FINALIZED_BLOCK_LOOKUP_MAP = finalizeRegistration(BLOCK_LOOKUP_MAP, FINALIZED_BLOCK_LOOKUP_MAP, "block");
    }

    public static Map<Item, BotariumEnergyItem<?>> getItemRegistry() {
        return FINALIZED_ITEM_LOOKUP_MAP = finalizeRegistration(ITEM_LOOKUP_MAP, FINALIZED_ITEM_LOOKUP_MAP, "item");
    }

    public static BotariumEnergyBlock<?> getEnergyBlock(Block block) {
        return getBlockRegistry().get(block);
    }

    public static BotariumEnergyBlock<?> getEnergyBlock(BlockEntityType<?> blockEntity) {
        return getBlockEntityRegistry().get(blockEntity);
    }

    public static BotariumEnergyItem<?> getEnergyItem(Item item) {
        return getItemRegistry().get(item);
    }

    public static void registerEnergyBlockEntity(Supplier<BlockEntityType<?>> block, BotariumEnergyBlock<?> getter) {
        BLOCK_ENTITY_LOOKUP_MAP.put(block, getter);
    }

    @SafeVarargs
    public static void registerEnergyBlockEntity(BotariumEnergyBlock<?> getter, Supplier<BlockEntityType<?>>... blocks) {
        for (Supplier<BlockEntityType<?>> block : blocks) {
            BLOCK_ENTITY_LOOKUP_MAP.put(block, getter);
        }
    }

    public static void registerEnergyBlock(Supplier<Block> block, BotariumEnergyBlock<?> getter) {
        BLOCK_LOOKUP_MAP.put(block, getter);
    }

    @SafeVarargs
    public static void registerEnergyBlock(BotariumEnergyBlock<?> getter, Supplier<Block>... blocks) {
        for (Supplier<Block> block : blocks) {
            BLOCK_LOOKUP_MAP.put(block, getter);
        }
    }

    public static void registerEnergyItem(Supplier<Item> item, BotariumEnergyItem<?> getter) {
        ITEM_LOOKUP_MAP.put(item, getter);
    }

    @SafeVarargs
    public static void registerEnergyItem(BotariumEnergyItem<?> getter, Supplier<Item>... items) {
        for (Supplier<Item> item : items) {
            ITEM_LOOKUP_MAP.put(item, getter);
        }
    }

    /**
     * Gets the energy container of the given {@link ItemStack}.
     * Will throw error if the {@link ItemStack} is not an energy container.
     *
     * @param stack The {@link ItemStack} to get the energy container from.
     * @return The {@link EnergyContainer} of the given {@link ItemStack}.
     */
    @ImplementedByExtension
    @Nullable
    public static EnergyContainer getItemEnergyContainer(ItemStackHolder stack) {
        throw new NotImplementedException("Item Energy Manager not Implemented");
    }

    /**
     * Gets the energy container of the given {@link BlockEntity} and {@link Direction}.
     * Will throw error if the {@link BlockEntity} is not an energy container.
     *
     * @param entity    The {@link BlockEntity} to get the energy container from.
     * @param direction The {@link Direction} to get the energy container from.
     * @return The {@link EnergyContainer} of the given {@link BlockEntity} and {@link Direction}.
     */
    @ImplementedByExtension
    @Nullable
    public static EnergyContainer getBlockEnergyContainer(BlockEntity entity, @Nullable Direction direction) {
        return getBlockEnergyContainer(entity.getLevel(), entity.getBlockPos(), entity.getBlockState(), entity, direction);
    }

    @ImplementedByExtension
    @Nullable
    public static EnergyContainer getBlockEnergyContainer(Level level, BlockPos pos, BlockState state, @Nullable BlockEntity entity, @Nullable Direction direction) {
        throw new NotImplementedException("Block Entity Energy manager not implemented");
    }

    /**
     * @param stack The {@link ItemStack} to check if it is an energy container.
     * @return Whether the given {@link ItemStack} is an energy container.
     */
    @ImplementedByExtension
    public static boolean isEnergyItem(ItemStack stack) {
        throw new NotImplementedException("Energy item check not Implemented");
    }

    /**
     * @param blockEntity     The {@link BlockEntity} to check if it is an energy container.
     * @param direction The {@link Direction} to check for an energy container on the {@link BlockEntity}.
     * @return Whether the given {@link BlockEntity} is an energy container.
     */
    @ImplementedByExtension
    public static boolean isEnergyBlock(BlockEntity blockEntity, @Nullable Direction direction) {
        return isEnergyBlock(blockEntity.getLevel(), blockEntity.getBlockPos(), blockEntity.getBlockState(), blockEntity, direction);
    }

    /**
     *
     */
    public static boolean isEnergyBlock(Level level, BlockPos pos, BlockState state, @Nullable BlockEntity entity, @Nullable Direction direction) {
        throw new NotImplementedException("Energy item check not Implemented");
    }

    /**
     * Automatically transfers energy from an energy block to surrounding blocks
     *
     * @param energyBlock A block entity that is an instance of {@link BotariumEnergyBlock}
     * @param amount      The total amount that will be distributed as equally it can be. If one block cannot receive all the energy, it will be distributed evenly to the other blocks.
     * @return The amount of energy that was distributed
     */
    public static long distributeEnergyNearby(BlockEntity energyBlock, long amount) {
        BlockPos blockPos = energyBlock.getBlockPos();
        Level level = energyBlock.getLevel();
        if (level == null) return 0;
        EnergyContainer internalEnergy = getBlockEnergyContainer(energyBlock, null);
        long amountToDistribute = internalEnergy.extractEnergy(amount, true);
        if (amountToDistribute == 0) return 0;
        List<EnergyContainer> list = Direction.stream()
                .map(direction -> Pair.of(direction, level.getBlockEntity(blockPos.relative(direction))))
                .filter(pair -> pair.getSecond() != null)
                .map(pair -> getBlockEnergyContainer(pair.getSecond(), pair.getFirst()))
                .filter(Objects::nonNull)
                .sorted(Comparator.comparingLong(energy -> energy.insertEnergy(amount, true)))
                .toList();
        int receiverCount = list.size();
        for (EnergyContainer energy : list) {
            if (energy == null) continue;
            long inserted = moveEnergy(internalEnergy, energy, amountToDistribute / receiverCount, false);
            amountToDistribute -= inserted;
            receiverCount--;
        }
        return amount - amountToDistribute;
    }

    /**
     * Moves energy from one energy container to another
     *
     * @param from   The energy container to move energy from
     * @param to     The energy container to move energy to
     * @param amount The amount of energy to move
     * @return The amount of energy that was moved
     */
    public static long moveEnergy(EnergyContainer from, EnergyContainer to, long amount, boolean simulate) {
        long extracted = from.extractEnergy(amount, true);
        long inserted = to.insertEnergy(extracted, true);
        long simulatedExtraction = from.extractEnergy(inserted, true);
        if (!simulate && inserted > 0 && simulatedExtraction == inserted) {
            from.extractEnergy(inserted, false);
            to.insertEnergy(inserted, false);
        }
        return Math.max(0, inserted);
    }

    /**
     * Moves energy from one energy container to another
     *
     * @param from   The energy container to move energy from
     * @param to     The energy container to move energy to
     * @param amount The amount of energy to move
     * @return The amount of energy that was moved
     */
    public static long moveEnergy(ItemStackHolder from, ItemStackHolder to, long amount, boolean simulate) {
        if (!isEnergyItem(from.getStack()) || !isEnergyItem(to.getStack())) return 0;
        EnergyContainer fromEnergy = getItemEnergyContainer(from);
        EnergyContainer toEnergy = getItemEnergyContainer(to);
        return moveEnergy(fromEnergy, toEnergy, amount, simulate);
    }

    /**
     * Moves energy from one energy container to another
     *
     * @param from   The energy container to move energy from
     * @param to     The energy container to move energy to
     * @param amount The amount of energy to move
     * @return The amount of energy that was moved
     */
    public static long moveEnergy(BlockEntity from, BlockEntity to, long amount, boolean simulate) {
        if (!isEnergyBlock(from, null) || !isEnergyBlock(to, null)) return 0;
        EnergyContainer fromEnergy = getBlockEnergyContainer(from, null);
        EnergyContainer toEnergy = getBlockEnergyContainer(to, null);
        return moveEnergy(fromEnergy, toEnergy, amount, simulate);
    }

    /**
     * Moves energy from one energy container to another
     *
     * @param from   The energy container to move energy from
     * @param to     The energy container to move energy to
     * @param amount The amount of energy to move
     * @return The amount of energy that was moved
     */
    public static long moveEnergy(BlockEntity from, Direction direction, ItemStackHolder to, long amount, boolean simulate) {
        if (!isEnergyBlock(from, direction) || !isEnergyItem(to.getStack())) return 0;
        EnergyContainer fromEnergy = getBlockEnergyContainer(from, direction);
        EnergyContainer toEnergy = getItemEnergyContainer(to);
        return moveEnergy(fromEnergy, toEnergy, amount, simulate);
    }

    /**
     * Moves energy from one energy container to another
     *
     * @param from   The energy container to move energy from
     * @param to     The energy container to move energy to
     * @param amount The amount of energy to move
     * @return The amount of energy that was moved
     */
    public static long moveEnergy(ItemStackHolder from, BlockEntity to, Direction direction, long amount, boolean simulate) {
        if (!isEnergyItem(from.getStack()) || !isEnergyBlock(to, direction)) return 0;
        EnergyContainer fromEnergy = getItemEnergyContainer(from);
        EnergyContainer toEnergy = getBlockEnergyContainer(to, direction);
        return moveEnergy(fromEnergy, toEnergy, amount, simulate);
    }
}
