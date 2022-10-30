package earth.terrarium.botarium.api.energy;

import com.mojang.datafixers.util.Pair;
import earth.terrarium.botarium.api.item.ItemStackHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.msrandom.extensions.annotations.ImplementedByExtension;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;

public class EnergyHooks {

    /**
     * Gets the energy container of the given {@link ItemStack}.
     * Will throw error if the {@link ItemStack} is not an energy container.
     *
     * @param stack The {@link ItemStack} to get the energy container from.
     * @return The {@link PlatformEnergyManager} of the given {@link ItemStack}.
     * @throws IllegalArgumentException If the item stack is not an energy container.
     */
    @ImplementedByExtension
    public static PlatformItemEnergyManager getItemEnergyManager(ItemStack stack) {
        throw new NotImplementedException("Item Energy Manager not Implemented");
    }

    /**
     * Gets the energy container of the given {@link BlockEntity} and {@link Direction}.
     * Will throw error if the {@link BlockEntity} is not an energy container.
     *
     * @param entity    The {@link BlockEntity} to get the energy container from.
     * @param direction The {@link Direction} to get the energy container from.
     * @return The {@link PlatformEnergyManager} of the given {@link BlockEntity} and {@link Direction}.
     * @throws IllegalArgumentException If the item stack is not an energy container.
     */
    @ImplementedByExtension
    public static PlatformEnergyManager getBlockEnergyManager(BlockEntity entity, @Nullable Direction direction) {
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
     * @param stack     The {@link BlockEntity} to check if it is an energy container.
     * @param direction The {@link Direction} to check for an energy container on the {@link BlockEntity}.
     * @return Whether the given {@link BlockEntity} is an energy container.
     */
    @ImplementedByExtension
    public static boolean isEnergyContainer(BlockEntity stack, @Nullable Direction direction) {
        throw new NotImplementedException("Energy item check not Implemented");
    }

    /**
     * Transfers energy from a {@link PlatformEnergyManager} to another {@link PlatformEnergyManager}.
     *
     * @param from   The {@link PlatformEnergyManager} to extract energy from.
     * @param to     The {@link PlatformEnergyManager} to transfer energy to.
     * @param amount The amount to transfer.
     * @return The amount of energy transferred.
     */
    public static long moveEnergy(PlatformEnergyManager from, PlatformEnergyManager to, long amount) {
        long extracted = from.extract(amount, true);
        long inserted = to.insert(extracted, true);
        from.extract(inserted, false);
        return to.insert(inserted, false);
    }

    /**
     * A safe version of {@link #moveEnergy(PlatformEnergyManager, PlatformEnergyManager, long)} that will not move any energy if the
     * {@link PlatformEnergyManager} is not present.
     * <p>
     * Transfers energy from a {@link PlatformEnergyManager} to another {@link PlatformEnergyManager}.
     *
     * @param from   The {@link PlatformEnergyManager} to extract energy from.
     * @param to     The {@link PlatformEnergyManager} to transfer energy to.
     * @param amount The amount to transfer.
     * @return The amount of energy transferred.
     */
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public static long safeMoveEnergy(Optional<PlatformEnergyManager> from, Optional<PlatformEnergyManager> to, long amount) {
        return from.map(f -> to.map(t -> moveEnergy(f, t, amount)).orElse(0L)).orElse(0L);
    }

    /**
     * Safely gets the {@link PlatformEnergyManager} for a {@link BlockEntity}.
     *
     * @param entity    The {@link BlockEntity} to get the {@link PlatformEnergyManager} from.
     * @param direction The {@link Direction} to get the {@link PlatformEnergyManager} from on the {@link BlockEntity}.
     * @return An optional containing the {@link PlatformEnergyManager} if the {@link BlockEntity} is an energy container, otherwise empty.
     */
    public static Optional<PlatformEnergyManager> safeGetBlockEnergyManager(BlockEntity entity, @Nullable Direction direction) {
        return isEnergyContainer(entity, direction) ? Optional.of(getBlockEnergyManager(entity, direction)) : Optional.empty();
    }

    /**
     * Safely gets the {@link PlatformEnergyManager} for an {@link ItemStack}.
     *
     * @param stack The {@link ItemStack} to get the {@link PlatformEnergyManager} from.
     * @return An optional containing the {@link PlatformEnergyManager} if the {@link ItemStack} is an energy container, otherwise empty.
     */
    public static Optional<PlatformItemEnergyManager> safeGetItemEnergyManager(ItemStack stack) {
        return isEnergyItem(stack) ? Optional.of(getItemEnergyManager(stack)) : Optional.empty();
    }

    //Moves energy from PlatformItemEnergyManager to another PlatformItemEnergyManager into javadoc
    /**
     * Transfers energy from a {@link PlatformItemEnergyManager} to another {@link PlatformItemEnergyManager}.
     *
     * @param from   The {@link PlatformItemEnergyManager} to extract energy from.
     * @param sender The {@link ItemStackHolder} that is sending the energy.
     * @param to     The {@link PlatformItemEnergyManager} to transfer energy to.
     * @param receiver The {@link ItemStackHolder} that is receiving the energy.
     * @param amount The amount to transfer.
     * @return The amount of energy transferred.
     */
    public static long moveItemToItemEnergy(PlatformItemEnergyManager from, ItemStackHolder sender, PlatformItemEnergyManager to, ItemStackHolder receiver, long amount) {
        long extracted = from.extract(sender, amount, true);
        long inserted = to.insert(receiver, extracted, true);
        from.extract(sender, inserted, false);
        return to.insert(receiver, inserted, false);
    }

    /**
     * Transfers energy from a {@link PlatformEnergyManager} to another {@link PlatformItemEnergyManager}.
     *
     * @param from   The {@link PlatformEnergyManager} to extract energy from.
     * @param to     The {@link PlatformItemEnergyManager} to transfer energy to.
     * @param receiver The {@link ItemStackHolder} that is receiving the energy.
     * @param amount The amount to transfer.
     * @return The amount of energy transferred.
     */
    public static long moveStandardToItemEnergy(PlatformEnergyManager from, PlatformItemEnergyManager to, ItemStackHolder receiver, long amount) {
        long extracted = from.extract(amount, true);
        long inserted = to.insert(receiver, extracted, true);
        from.extract(inserted, false);
        return to.insert(receiver, inserted, false);
    }

    /**
     * Transfers energy from a {@link PlatformItemEnergyManager} to a {@link PlatformEnergyManager}.
     *
     * @param from   The {@link PlatformItemEnergyManager} to extract energy from.
     * @param sender The {@link ItemStackHolder} that is sending the energy.
     * @param to     The {@link PlatformEnergyManager} to transfer energy to.
     * @param amount The amount to transfer.
     * @return The amount of energy transferred.
     */
    public static long moveItemToStandardEnergy(PlatformItemEnergyManager from, ItemStackHolder sender, PlatformEnergyManager to, long amount) {
        long extracted = from.extract(sender, amount, true);
        long inserted = to.insert(extracted, true);
        from.extract(sender, inserted, false);
        return to.insert(inserted, false);
    }

    /**
     * Safely transfers energy from a {@link ItemStackHolder} to another {@link ItemStackHolder} if both items
     * are energy containers.
     *
     * @param from The {@link ItemStackHolder} that is sending the energy.
     * @param to The {@link ItemStackHolder} that is receiving the energy.
     * @param amount The amount to transfer.
     * @return The amount of energy transferred.
     */
    public static long safeMoveItemToItemEnergy(ItemStackHolder from, ItemStackHolder to, long amount) {
        return safeGetItemEnergyManager(from.getStack()).map(f -> safeGetItemEnergyManager(to.getStack()).map(t -> moveItemToItemEnergy(f, from, t, to, amount)).orElse(0L)).orElse(0L);
    }

    /**
     * Safely transfers energy from a {@link ItemStackHolder} to a {@link BlockEntity} if both are energy containers.
     *
     * @param from The {@link ItemStackHolder} that is sending the energy.
     * @param to The {@link BlockEntity} that is receiving the energy.
     * @param direction The {@link Direction} to get the {@link PlatformEnergyManager} from on the {@link BlockEntity}.
     * @param amount The amount to transfer.
     * @return The amount of energy transferred.
     */
    public static long safeMoveItemToBlockEnergy(ItemStackHolder from, BlockEntity to, @Nullable Direction direction, long amount) {
        return safeGetItemEnergyManager(from.getStack()).map(f -> safeGetBlockEnergyManager(to, direction).map(t -> moveItemToStandardEnergy(f, from, t, amount)).orElse(0L)).orElse(0L);
    }

    /**
     * Safely transfers energy from a {@link BlockEntity} to a {@link ItemStackHolder} if both are energy containers.
     *
     * @param from The {@link BlockEntity} that is sending the energy.
     * @param direction The {@link Direction} to get the {@link PlatformEnergyManager} from on the {@link BlockEntity}.
     * @param to The {@link ItemStackHolder} that is receiving the energy.
     * @param amount The amount to transfer.
     * @return The amount of energy transferred.
     */
    public static long safeMoveBlockToItemEnergy(BlockEntity from, @Nullable Direction direction, ItemStackHolder to, long amount) {
        return safeGetBlockEnergyManager(from, direction).map(f -> safeGetItemEnergyManager(to.getStack()).map(t -> moveStandardToItemEnergy(f, t, to, amount)).orElse(0L)).orElse(0L);
    }

    /**
     * Transfers energy from an {@link BlockEntity} to an {@link BlockEntity}.
     *
     * @param from          The {@link BlockEntity} to extract energy from.
     * @param fromDirection The {@link Direction} to extract energy from on the {@link BlockEntity}.
     * @param to            The {@link BlockEntity} to transfer energy to.
     * @param toDirection   The {@link Direction} to insert energy into on the {@link BlockEntity}.
     * @param amount        The amount of energy to transfer.
     * @return The amount of energy transferred.
     */
    public static long moveBlockToBlockEnergy(BlockEntity from, @Nullable Direction fromDirection, BlockEntity to, @Nullable Direction toDirection, long amount) {
        return safeMoveEnergy(safeGetBlockEnergyManager(from, fromDirection), safeGetBlockEnergyManager(to, toDirection), amount);
    }

    /**
     * Transfers energy from an {@link BlockEntity} to an {@link BlockEntity}.
     *
     * @param from   The {@link BlockEntity} to extract energy from.
     * @param to     The {@link BlockEntity} to transfer energy to.
     * @param amount The amount of energy to transfer.
     * @return The amount of energy transferred.
     */
    public static long moveBlockToBlockEnergy(BlockEntity from, BlockEntity to, long amount) {
        return safeMoveEnergy(safeGetBlockEnergyManager(from, null), safeGetBlockEnergyManager(to, null), amount);
    }

    /**
     * Automatically transfers energy from an energy block to surrounding blocks
     * @param energyBlock A block entity that is an instance of {@link EnergyBlock}
     * @param amount The amount it will transfer to each side (if possible)
     */
    public static <T extends BlockEntity & EnergyBlock> void distributeEnergyNearby(T energyBlock, long amount) {
        BlockPos blockPos = energyBlock.getBlockPos();
        Level level = energyBlock.getLevel();
        if (level == null) return;
        Direction.stream()
            .map(direction -> Pair.of(direction, level.getBlockEntity(blockPos.relative(direction))))
            .filter(pair -> pair.getSecond() != null)
            .map(pair -> Pair.of(safeGetBlockEnergyManager(pair.getSecond(), pair.getFirst()), pair.getFirst()))
            .filter(pair -> pair.getFirst().isPresent())
            .forEach(pair -> {
                PlatformEnergyManager externalEnergy = pair.getFirst().get();
                safeGetBlockEnergyManager(energyBlock, pair.getSecond().getOpposite())
                    .ifPresent(platformEnergyManager -> moveEnergy(platformEnergyManager, externalEnergy, amount == -1 ? energyBlock.getEnergyStorage().getStoredEnergy() : amount));
            });
    }

    /**
     * Automatically transfers energy from an energy block to surrounding blocks.
     * Will transfer as much energy as it can.
     * @param energyBlock A block entity that is an instance of {@link EnergyBlock}
     */
    public static <T extends BlockEntity & EnergyBlock> void distributeEnergyNearby(T energyBlock) {
        distributeEnergyNearby(energyBlock, -1);
    }
}
