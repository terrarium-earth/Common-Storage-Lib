package earth.terrarium.botarium.api.fluid;

import earth.terrarium.botarium.api.item.ItemStackHolder;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.material.Fluid;
import net.msrandom.extensions.annotations.ImplementedByExtension;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class FluidHooks {
    /**
     * @param fluid The fluid to create the holder for.
     * @param amount The amount of fluid in the holder.
     * @param tag The tag of the holder.
     * @return A {@link FluidHolder} for the given fluid, amount, and tag.
     */
    @ImplementedByExtension
    public static FluidHolder newFluidHolder(Fluid fluid, long amount, @Nullable CompoundTag tag) {
        throw new NotImplementedException();
    }

    /**
     * @param compoundTag The compound tag to read from.
     * @return The {@link FluidHolder} read from the compound tag.
     */
    @ImplementedByExtension
    public static FluidHolder fluidFromCompound(CompoundTag compoundTag) {
        throw new NotImplementedException();
    }

    /**
     * @return An empty fluid holder.
     */
    @ImplementedByExtension
    public static FluidHolder emptyFluid() {
        throw new NotImplementedException();
    }

    /**
     * @param buckets The number of buckets.
     * @return The number of fluid values in the given number of buckets.
     */
    @ImplementedByExtension
    public static long buckets(double buckets) {
        return (long) (buckets * getBucketAmount());
    }

    @Deprecated
    public static long buckets(int buckets) {
        return buckets((double) buckets);
    }

    /**
     * Gets the {@link PlatformFluidHandler} for an {@link ItemStack}.
     * @param stack The {@link ItemStack} to get the {@link PlatformFluidHandler} from.
     * @return The {@link PlatformFluidHandler} for the {@link ItemStack}.
     * @throws IllegalArgumentException If the {@link ItemStack} does not have a {@link PlatformFluidHandler}.
     */
    @ImplementedByExtension
    public static PlatformFluidItemHandler getItemFluidManager(ItemStack stack) {
        throw new NotImplementedException("Item Fluid Manager not Implemented");
    }

    /**
     * Gets the {@link PlatformFluidHandler} for a {@link BlockEntity}.
     * @param entity The {@link BlockEntity} to get the {@link PlatformFluidHandler} from.
     * @param direction The {@link Direction} to get the {@link PlatformFluidHandler} from on the {@link BlockEntity}.
     * @return The {@link PlatformFluidHandler} for the {@link BlockEntity} and {@link Direction}.
     * @throws IllegalArgumentException If the {@link BlockEntity} does not have a {@link PlatformFluidHandler}.
     */
    @ImplementedByExtension
    public static PlatformFluidHandler getBlockFluidManager(BlockEntity entity, @Nullable Direction direction) {
        throw new NotImplementedException("Block Entity Fluid manager not implemented");
    }

    /**
     * @param entity The {@link BlockEntity} to check if it is a fluid container.
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

    //optional platform fluid handler getters

    /**
     * Safely gets the {@link PlatformFluidHandler} for a given {@link BlockEntity}.
     * @param entity The {@link BlockEntity} to get the {@link PlatformFluidHandler} from.
     * @param direction The {@link Direction} to get the {@link PlatformFluidHandler} from on the {@link BlockEntity}.
     * @return An optional containing the {@link PlatformFluidHandler} if the {@link BlockEntity} is a fluid container, otherwise empty.
     */
    public static Optional<PlatformFluidHandler> safeGetBlockFluidManager(BlockEntity entity, @Nullable Direction direction) {
        if (entity == null) return Optional.empty();
        return isFluidContainingBlock(entity, direction) ? Optional.of(getBlockFluidManager(entity, direction)) : Optional.empty();
    }

    /**
     * Safely gets the {@link PlatformFluidHandler} for a given {@link ItemStack}.
     * @param stack The {@link ItemStack} to get the {@link PlatformFluidHandler} from.
     * @return An optional containing the {@link PlatformFluidHandler} if the item is a fluid container, otherwise empty.
     */
    public static Optional<PlatformFluidItemHandler> safeGetItemFluidManager(ItemStack stack) {
        return isFluidContainingItem(stack) ? Optional.of(getItemFluidManager(stack)) : Optional.empty();
    }

    /**
     * Transfers fluid from a {@link PlatformFluidHandler} to another {@link PlatformFluidHandler}.
     * @param from The {@link PlatformFluidHandler} to extract fluid from.
     * @param to The {@link PlatformFluidHandler} to transfer fluid to.
     * @param fluid The {@link FluidHolder} to transfer.
     * @return The amount of fluid transferred.
     */
    public static long moveFluid(PlatformFluidHandler from, PlatformFluidHandler to, FluidHolder fluid) {
        FluidHolder extracted = from.extractFluid(fluid, true);
        long inserted = to.insertFluid(extracted, true);
        from.extractFluid(newFluidHolder(fluid.getFluid(), inserted, fluid.getCompound()), false);
        return to.insertFluid(extracted, false);
    }

    /**
     * Transfers fluid from a {@link PlatformFluidHandler} to another {@link PlatformFluidHandler}.
     * @param from The {@link PlatformFluidHandler} to extract fluid from.
     * @param to The {@link PlatformFluidHandler} to transfer fluid to.
     * @param fluid The {@link FluidHolder} to transfer.
     * @return The amount of fluid transferred.
     */
    public static long moveItemToStandardFluid(PlatformFluidItemHandler from, ItemStackHolder sender, PlatformFluidHandler to, FluidHolder fluid) {
        FluidHolder extracted = from.extractFluid(sender.copy(), fluid, true);
        long inserted = to.insertFluid(extracted, true);
        from.extractFluid(sender, newFluidHolder(fluid.getFluid(), inserted, fluid.getCompound()), false);
        return to.insertFluid(extracted, false);
    }

    /**
     * Transfers fluid from a {@link PlatformFluidHandler} to another {@link PlatformFluidHandler}.
     * @param from The {@link PlatformFluidHandler} to extract fluid from.
     * @param to The {@link PlatformFluidHandler} to transfer fluid to.
     * @param fluid The {@link FluidHolder} to transfer.
     * @return The amount of fluid transferred.
     */
    public static long moveStandardToItemFluid(PlatformFluidHandler from, PlatformFluidItemHandler to, ItemStackHolder receiver, FluidHolder fluid) {
        FluidHolder extracted = from.extractFluid(fluid, true);
        long inserted = to.insertFluid(receiver, extracted, true);
        from.extractFluid(newFluidHolder(fluid.getFluid(), inserted, fluid.getCompound()), false);
        return to.insertFluid(receiver, extracted, false);
    }

    /**
     * Transfers fluid from a {@link PlatformFluidHandler} to another {@link PlatformFluidHandler}.
     * @param from The {@link PlatformFluidHandler} to extract fluid from.
     * @param to The {@link PlatformFluidHandler} to transfer fluid to.
     * @param fluid The {@link FluidHolder} to transfer.
     * @return The amount of fluid transferred.
     */
    public static long moveItemToItemFluid(PlatformFluidItemHandler from, ItemStackHolder sender, PlatformFluidItemHandler to, ItemStackHolder receiver, FluidHolder fluid) {
        FluidHolder extracted = from.extractFluid(sender.copy(), fluid, true);
        long inserted = to.insertFluid(receiver, extracted, true);
        from.extractFluid(sender, newFluidHolder(fluid.getFluid(), inserted, fluid.getCompound()), false);
        return to.insertFluid(receiver, extracted, false);
    }

    /**
     * A safe version of {@link #moveFluid(PlatformFluidHandler, PlatformFluidHandler, FluidHolder)} that will not move any fluid if the
     * {@link PlatformFluidHandler} is not present.
     * <p>
     * Transfers fluid from a {@link PlatformFluidHandler} to another {@link PlatformFluidHandler}.
     * @param from The {@link PlatformFluidHandler} to extract fluid from.
     * @param to The {@link PlatformFluidHandler} to transfer fluid to.
     * @param fluid The {@link FluidHolder} to transfer.
     * @return The amount of fluid transferred.
     */
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public static long safeMoveFluid(Optional<PlatformFluidHandler> from, Optional<PlatformFluidHandler> to, FluidHolder fluid) {
        return from.flatMap(f -> to.map(t -> moveFluid(f, t, fluid))).orElse(0L);
    }

    /**
     * A safe version of {@link #moveItemToStandardFluid(PlatformFluidItemHandler, ItemStackHolder, PlatformFluidHandler, FluidHolder)} that will not move any fluid if the
     * {@link PlatformFluidHandler} is not present.
     * <p>
     * Transfers fluid from a {@link PlatformFluidItemHandler} to another {@link PlatformFluidHandler}.
     * @param from The {@link PlatformFluidItemHandler} to extract fluid from.
     * @param sender The {@link ItemStackHolder} that is sending the fluid.
     * @param to The {@link PlatformFluidHandler} to transfer fluid to.
     * @param fluid The {@link FluidHolder} to transfer.
     * @return The amount of fluid transferred.
     */
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public static long safeMoveItemToStandard(Optional<PlatformFluidItemHandler> from, ItemStackHolder sender, Optional<PlatformFluidHandler> to, FluidHolder fluid) {
        return from.flatMap(f -> to.map(t -> moveItemToStandardFluid(f, sender, t, fluid))).orElse(0L);
    }

    /**
     * A safe version of {@link #moveStandardToItemFluid(PlatformFluidHandler, PlatformFluidItemHandler, ItemStackHolder, FluidHolder)} (PlatformFluidItemHandler, PlatformFluidItemHandler, ItemStackHolder, FluidHolder)} that will not move any fluid if the
     * {@link PlatformFluidHandler} is not present.
     * <p>
     * Transfers fluid from a {@link PlatformFluidHandler} to another {@link PlatformFluidItemHandler}.
     * @param from The {@link PlatformFluidHandler} to extract fluid from.
     * @param to The {@link PlatformFluidItemHandler} to transfer fluid to.
     * @param receiver The {@link ItemStackHolder} to receive the fluid.
     * @param fluid The {@link FluidHolder} to transfer.
     * @return The amount of fluid transferred.
     */
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public static long safeMoveStandardToItem(Optional<PlatformFluidHandler> from, Optional<PlatformFluidItemHandler> to, ItemStackHolder receiver, FluidHolder fluid) {
        return from.flatMap(f -> to.map(t -> moveStandardToItemFluid(f, t, receiver, fluid))).orElse(0L);
    }

    /**
     * A safe version of {@link #moveItemToItemFluid(PlatformFluidItemHandler, ItemStackHolder, PlatformFluidItemHandler, ItemStackHolder, FluidHolder)} that will not move any fluid if the
     * {@link PlatformFluidHandler} is not present.
     * <p>
     * Transfers fluid from a {@link PlatformFluidHandler} to another {@link PlatformFluidHandler}.
     * @param from The {@link PlatformFluidItemHandler} to extract fluid from.
     * @param sender The {@link ItemStackHolder} that is sending the fluid.
     * @param to The {@link PlatformFluidItemHandler} to transfer fluid to.
     * @param receiver The {@link ItemStackHolder} to receive the fluid.
     * @param fluid The {@link FluidHolder} to transfer.
     * @return The amount of fluid transferred.
     */
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public static long safeMoveItemToItem(Optional<PlatformFluidItemHandler> from, ItemStackHolder sender, Optional<PlatformFluidItemHandler> to, ItemStackHolder receiver, FluidHolder fluid) {
        return from.flatMap(f -> to.map(t -> moveItemToItemFluid(f, sender, t, receiver, fluid))).orElse(0L);
    }


    /**
     * Transfers fluid from an {@link BlockEntity} to another {@link BlockEntity}.
     * @param from The {@link BlockEntity} to extract fluid from.
     * @param fromDirection The {@link Direction} that the fluid is extracted from.
     * @param to The {@link BlockEntity} to transfer fluid to.
     * @param toDirection The {@link Direction} that the fluid is inserted into.
     * @param fluid The {@link FluidHolder} to transfer.
     * @return The amount of fluid transferred.
     */
    public static long moveBlockToBlockFluid(BlockEntity from, @Nullable Direction fromDirection, BlockEntity to, @Nullable Direction toDirection, FluidHolder fluid) {
        return safeMoveFluid(safeGetBlockFluidManager(from, fromDirection), safeGetBlockFluidManager(to, toDirection), fluid);
    }

    /**
     * Transfers fluid from an {@link BlockEntity} to an {@link ItemStackHolder}.
     * @param from The {@link BlockEntity} to extract fluid from.
     * @param fromDirection The {@link Direction} that the fluid is extracted from.
     * @param to The {@link ItemStackHolder} to transfer fluid to.
     * @param fluid The {@link FluidHolder} to transfer.
     * @return The amount of fluid transferred.
     */
    public static long moveBlockToItemFluid(BlockEntity from, @Nullable Direction fromDirection, ItemStackHolder to, FluidHolder fluid) {
        return safeMoveStandardToItem(safeGetBlockFluidManager(from, fromDirection), safeGetItemFluidManager(to.getStack()), to, fluid);
    }

    /**
     * Transfers fluid from an {@link ItemStackHolder} to an {@link BlockEntity}.
     * @param from The {@link ItemStackHolder} to extract fluid from.
     * @param to The {@link BlockEntity} to transfer fluid to.
     * @param toDirection The {@link Direction} that the fluid is inserted into.
     * @param fluid The {@link FluidHolder} to transfer.
     * @return The amount of fluid transferred.
     */
    public static long moveItemToBlockFluid(ItemStackHolder from, BlockEntity to, @Nullable Direction toDirection, FluidHolder fluid) {
        return safeMoveItemToStandard(safeGetItemFluidManager(from.getStack()), from, safeGetBlockFluidManager(to, toDirection), fluid);
    }

    /**
     * Transfers fluid from an {@link ItemStackHolder} to another {@link ItemStackHolder}.
     * @param from The {@link ItemStackHolder} to extract fluid from.
     * @param to The {@link ItemStackHolder} to transfer fluid to.
     * @param fluid The {@link FluidHolder} to transfer.
     * @return The amount of fluid transferred.
     */
    public static long moveItemToItemFluid(ItemStackHolder from, ItemStackHolder to, FluidHolder fluid) {
        return safeMoveItemToItem(safeGetItemFluidManager(from.getStack()), from, safeGetItemFluidManager(to.getStack()), to, fluid);
    }

    /**
     * @param amount amount of fluid to be converted
     * @return amount of fluid in millibuckets
     */
    @ImplementedByExtension
    public static long toMillibuckets(long amount) {
        throw new NotImplementedException();
    }

    /**
     * @return The amount of fluid a bucket is for the platform.
     */
    @ImplementedByExtension
    public static long getBucketAmount() {
        throw new NotImplementedException();
    }

    /**
     * @return The amount of fluid a bottle is for the platform.
     */
    @ImplementedByExtension
    public static long getBottleAmount() {
        throw new NotImplementedException();
    }

    /**
     * @return The amount of fluid a block is for the platform.
     */
    @ImplementedByExtension
    public static long getBlockAmount() {
        throw new NotImplementedException();
    }

    /**
     * @return The amount of fluid an ingot is for the platform.
     */
    @ImplementedByExtension
    public static long getIngotAmount() {
        throw new NotImplementedException();
    }

    /**
     * @return The amount of fluid a nugget is for the platform.
     */
    @ImplementedByExtension
    public static long getNuggetAmount() {
        throw new NotImplementedException();
    }
}
