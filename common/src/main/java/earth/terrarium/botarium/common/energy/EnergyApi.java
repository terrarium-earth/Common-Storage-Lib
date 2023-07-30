package earth.terrarium.botarium.common.energy;

import com.mojang.datafixers.util.Pair;
import earth.terrarium.botarium.common.energy.base.BotariumEnergyBlock;
import earth.terrarium.botarium.common.energy.base.EnergyContainer;
import earth.terrarium.botarium.common.energy.impl.WrappedBlockEnergyContainer;
import earth.terrarium.botarium.common.energy.impl.WrappedItemEnergyContainer;
import earth.terrarium.botarium.common.fluid.FluidApi;
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
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        throw new NotImplementedException("Block Entity Energy manager not implemented");
    }

    /**
     * @param stack The {@link ItemStack} to check if it is an energy container.
     * @return Whether the given {@link ItemStack} is an energy container.
     */
    @Contract(pure = true)
    @ImplementedByExtension
    public static boolean isEnergyItem(ItemStack stack) {
        throw new NotImplementedException("Energy item check not Implemented");
    }

    /**
     * @param stack     The {@link BlockEntity} to check if it is an energy container.
     * @param direction The {@link Direction} to check for an energy container on the {@link BlockEntity}.
     * @return Whether the given {@link BlockEntity} is an energy container.
     */
    @Contract(pure = true)
    @ImplementedByExtension
    public static boolean isEnergyBlock(BlockEntity stack, @Nullable Direction direction) {
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
                .filter(pair -> isEnergyBlock(pair.getSecond(), pair.getFirst().getOpposite()))
                .map(pair -> getBlockEnergyContainer(pair.getSecond(), pair.getFirst()))
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

    @FunctionalInterface
    public interface BlockEnergyGetter<T extends EnergyContainer & Updatable<BlockEntity>>  {
        T getEnergyContainer(Level level, BlockPos pos, BlockState state, BlockEntity entity, Direction direction);
    }

    @FunctionalInterface
    public interface ItemEnergyGetter<T extends EnergyContainer & Updatable<ItemStack>>  {
        T getEnergyContainer(ItemStack stack);
    }
}
