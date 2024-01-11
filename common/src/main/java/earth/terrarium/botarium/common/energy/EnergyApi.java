package earth.terrarium.botarium.common.energy;

import com.mojang.datafixers.util.Pair;
import earth.terrarium.botarium.common.energy.base.BotariumEnergyBlock;
import earth.terrarium.botarium.common.energy.base.EnergyContainer;
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
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Supplier;

public class EnergyApi {
    private static final Map<Supplier<BlockEntityType<?>>, BlockEnergyGetter<?>> BLOCK_ENTITY_LOOKUP_MAP = new HashMap<>();
    private static final Map<Supplier<Block>, BlockEnergyGetter<?>> BLOCK_LOOKUP_MAP = new HashMap<>();
    private static final Map<Supplier<Item>, ItemEnergyGetter<?>> ITEM_LOOKUP_MAP = new HashMap<>();

    public static final Map<BlockEntityType<?>, BlockEnergyGetter<?>> FINALIZED_BLOCK_ENTITY_LOOKUP_MAP = new HashMap<>();
    public static final Map<Block, BlockEnergyGetter<?>> FINALIZED_BLOCK_LOOKUP_MAP = new HashMap<>();
    public static boolean blocksFinalized = false;
    public static final Map<Item, ItemEnergyGetter<?>> FINALIZED_ITEM_LOOKUP_MAP = new HashMap<>();
    public static boolean itemsFinalized = false;

    public static void finalizeBlockRegistration() {
        if (!blocksFinalized) {
            System.out.println("Finalizing energy block registration");
            for (Map.Entry<Supplier<BlockEntityType<?>>, BlockEnergyGetter<?>> entry : BLOCK_ENTITY_LOOKUP_MAP.entrySet()) {
                FINALIZED_BLOCK_ENTITY_LOOKUP_MAP.put(entry.getKey().get(), entry.getValue());
            }

            for (Map.Entry<Supplier<Block>, BlockEnergyGetter<?>> entry : BLOCK_LOOKUP_MAP.entrySet()) {
                FINALIZED_BLOCK_LOOKUP_MAP.put(entry.getKey().get(), entry.getValue());
            }

            blocksFinalized = true;
        }
    }

    public static void finalizeItemRegistration() {
        if (!itemsFinalized) {
            System.out.println("Finalizing energy item registration");
            for (Map.Entry<Supplier<Item>, ItemEnergyGetter<?>> entry : ITEM_LOOKUP_MAP.entrySet()) {
                FINALIZED_ITEM_LOOKUP_MAP.put(entry.getKey().get(), entry.getValue());
            }

            itemsFinalized = true;
        }
    }

    public static void registerEnergyBlockEntity(Supplier<BlockEntityType<?>> block, BlockEnergyGetter<?> getter) {
        BLOCK_ENTITY_LOOKUP_MAP.put(block, getter);
    }

    @SafeVarargs
    public static void registerEnergyBlockEntity(BlockEnergyGetter<?> getter, Supplier<BlockEntityType<?>>... blocks) {
        for (Supplier<BlockEntityType<?>> block : blocks) {
            BLOCK_ENTITY_LOOKUP_MAP.put(block, getter);
        }
    }

    public static void registerEnergyBlock(Supplier<Block> block, BlockEnergyGetter<?> getter) {
        BLOCK_LOOKUP_MAP.put(block, getter);
    }

    @SafeVarargs
    public static void registerEnergyBlock(BlockEnergyGetter<?> getter, Supplier<Block>... blocks) {
        for (Supplier<Block> block : blocks) {
            BLOCK_LOOKUP_MAP.put(block, getter);
        }
    }

    public static void registerEnergyItem(Supplier<Item> item, ItemEnergyGetter<?> getter) {
        ITEM_LOOKUP_MAP.put(item, getter);
    }

    @SafeVarargs
    public static void registerEnergyItem(ItemEnergyGetter<?> getter, Supplier<Item>... items) {
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
     * @deprecated Use {@link EnergyContainer#of(ItemStackHolder)} instead.
     */
    @Nullable
    @Deprecated
    @ImplementedByExtension
    @ApiStatus.ScheduledForRemoval(inVersion = "1.20.4")
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
     * @deprecated Use {@link EnergyContainer#of(Level, BlockPos, Direction)} or {@link EnergyContainer#of(BlockEntity, Direction)} instead.
     */
    @Nullable
    @Deprecated
    @ImplementedByExtension
    @ApiStatus.ScheduledForRemoval(inVersion = "1.20.4")
    public static EnergyContainer getBlockEnergyContainer(BlockEntity entity, @Nullable Direction direction) {
        throw new NotImplementedException("Block Entity Energy manager not implemented");
    }

    /**
     * @param stack The {@link ItemStack} to check if it is an energy container.
     * @return Whether the given {@link ItemStack} is an energy container.
     * @deprecated Use {@link EnergyContainer#holdsEnergy(ItemStack)} instead.
     */
    @Deprecated
    @ImplementedByExtension
    @ApiStatus.ScheduledForRemoval(inVersion = "1.20.4")
    public static boolean isEnergyItem(ItemStack stack) {
        throw new NotImplementedException("Energy item check not Implemented");
    }

    /**
     * @param stack     The {@link BlockEntity} to check if it is an energy container.
     * @param direction The {@link Direction} to check for an energy container on the {@link BlockEntity}.
     * @return Whether the given {@link BlockEntity} is an energy container.
     * @deprecated Use {@link EnergyContainer#holdsEnergy(Level, BlockPos, Direction)} or {@link EnergyContainer#holdsEnergy(BlockEntity, Direction)} instead.
     */
    @Deprecated
    @Contract(pure = true)
    @ImplementedByExtension
    @ApiStatus.ScheduledForRemoval(inVersion = "1.20.4")
    public static boolean isEnergyBlock(BlockEntity blockEntity, @Nullable Direction direction) {
        throw new NotImplementedException("Energy item check not Implemented");
    }

    /**
     * Automatically transfers energy from an energy block to surrounding blocks
     *
     * @param energyBlock A block entity that is an instance of {@link BotariumEnergyBlock}
     * @param extractDirection The direction to extract energy from the energy block
     * @param amount      The total amount that will be distributed as equally it can be. If one block cannot receive all the energy, it will be distributed evenly to the other blocks.
     * @return The amount of energy that was distributed
     */
    public static long distributeEnergyNearby(BlockEntity energyBlock, @Nullable Direction extractDirection, long amount) {
        return distributeEnergyNearby(energyBlock.getLevel(), energyBlock.getBlockPos(), extractDirection, amount);
    }

    /**
     * Automatically transfers energy from an energy block to surrounding blocks
     *
     * @param energyBlock A block entity that is an instance of {@link BotariumEnergyBlock}
     * @param amount      The total amount that will be distributed as equally it can be. If one block cannot receive all the energy, it will be distributed evenly to the other blocks.
     * @return The amount of energy that was distributed
     */
    public static long distributeEnergyNearby(BlockEntity energyBlock, long amount) {
        return distributeEnergyNearby(energyBlock.getLevel(), energyBlock.getBlockPos(), null, amount);
    }

    /**
     * Automatically transfers energy from an energy block to surrounding blocks
     *
     * @param level         The level of the energy block
     * @param energyPos     The position of the energy block
     * @param amount        The total amount that will be distributed as equally it can be. If one block cannot receive all the energy, it will be distributed evenly to the other blocks.
     * @param extractDirection The direction to extract energy from the energy block
     * @return The amount of energy that was distributed
     */
    public static long distributeEnergyNearby(Level level, BlockPos energyPos, @Nullable Direction extractDirection, long amount) {
        EnergyContainer internalEnergy = EnergyContainer.of(level, energyPos, extractDirection);
        long amountToDistribute = internalEnergy.extractEnergy(amount, true);
        if (amountToDistribute == 0) return 0;
        List<EnergyContainer> list = Direction.stream()
                .map(direction -> EnergyContainer.of(level, energyPos.relative(direction), direction.getOpposite()))
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
     * @param level The level of the energy container to move energy from
     * @param fromPos The position of the energy container to move energy from
     * @param fromDirection The direction of the energy container to move energy from
     * @param toPos The position of the energy container to move energy to
     * @param toDirection The direction of the energy container to move energy to
     * @param amount The amount of energy to move
     * @return The amount of energy that was moved
     */
    public static long moveEnergy(Level level, BlockPos fromPos, @Nullable Direction fromDirection, BlockPos toPos, @Nullable Direction toDirection, long amount, boolean simulate) {
        if(!EnergyContainer.holdsEnergy(level, fromPos, fromDirection) || !EnergyContainer.holdsEnergy(level, toPos, toDirection)) return 0;
        EnergyContainer fromEnergy = EnergyContainer.of(level, fromPos, fromDirection);
        EnergyContainer toEnergy = EnergyContainer.of(level, toPos, toDirection);
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

    @FunctionalInterface
    public interface BlockEnergyGetter<T extends EnergyContainer & Updatable<BlockEntity>>  {
        T getEnergyContainer(Level level, BlockPos pos, BlockState state, BlockEntity entity, Direction direction);
    }

    @FunctionalInterface
    public interface ItemEnergyGetter<T extends EnergyContainer & Updatable<ItemStack>>  {
        T getEnergyContainer(ItemStack stack);
    }
}
