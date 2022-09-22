package earth.terrarium.botarium.api.fluid;

import earth.terrarium.botarium.api.energy.PlatformEnergyManager;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.material.Fluid;
import net.msrandom.extensions.annotations.ImplementedByExtension;
import net.msrandom.extensions.annotations.ImplementsBaseElement;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class FluidHooks {
    public static final long BUCKET = 81000;
    public static final long BOTTLE = 27000;
    public static final long BLOCK = 81000;
    public static final long INGOT = 9000;
    public static final long NUGGET = 1000;

    @ImplementedByExtension
    public static FluidHolder newFluidHolder(Fluid fluid, long amount, @Nullable CompoundTag tag) {
        throw new NotImplementedException();
    }

    @ImplementedByExtension
    public static FluidHolder fluidFromCompound(CompoundTag compoundTag) {
        throw new NotImplementedException();
    }

    @ImplementedByExtension
    public static FluidHolder emptyFluid() {
        throw new NotImplementedException();
    }

    @ImplementedByExtension
    public static long buckets(int buckets) {
        throw new NotImplementedException();
    }

    @ImplementedByExtension
    public static PlatformFluidHandler getItemFluidManager(ItemStack stack) {
        throw new NotImplementedException("Item Fluid Manager not Implemented");
    }

    @ImplementedByExtension
    public static PlatformFluidHandler getBlockFluidManager(BlockEntity entity, @Nullable Direction direction) {
        throw new NotImplementedException("Block Entity Fluid manager not implemented");
    }

    @ImplementedByExtension
    public static boolean isFluidContainingBlock(BlockEntity entity, @Nullable Direction direction) {
        throw new NotImplementedException();
    }

    @ImplementedByExtension
    public static boolean isFluidContainingItem(ItemStack stack) {
        throw new NotImplementedException();
    }

    //optional platform fluid handler getters

    public static Optional<PlatformFluidHandler> safeGetBlockFluidManager(BlockEntity entity, @Nullable Direction direction) {
        return isFluidContainingBlock(entity, direction) ? Optional.of(getBlockFluidManager(entity, direction)) : Optional.empty();
    }

    public static Optional<PlatformFluidHandler> safeGetItemFluidManager(ItemStack stack) {
        return isFluidContainingItem(stack) ? Optional.of(getItemFluidManager(stack)) : Optional.empty();
    }

    public static long moveFluid(PlatformFluidHandler from, PlatformFluidHandler to, FluidHolder fluid) {
        FluidHolder extracted = from.extractFluid(fluid, true);
        long inserted = to.insertFluid(extracted, true);
        from.extractFluid(newFluidHolder(fluid.getFluid(), inserted, fluid.getCompound()), false);
        return to.insertFluid(extracted, false);
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public static long safeMoveFluid(Optional<PlatformFluidHandler> from, Optional<PlatformFluidHandler> to, FluidHolder fluid) {
        return from.flatMap(f -> to.map(t -> moveFluid(f, t, fluid))).orElse(0L);
    }

    public static long moveItemToBlockFluid(ItemStack from, BlockEntity to, @Nullable Direction direction, FluidHolder fluid) {
        return safeMoveFluid(safeGetItemFluidManager(from), safeGetBlockFluidManager(to, direction), fluid);
    }

    public static long moveBlockToItemFluid(BlockEntity from, @Nullable Direction direction, ItemStack to, FluidHolder fluid) {
        return safeMoveFluid(safeGetBlockFluidManager(from, direction), safeGetItemFluidManager(to), fluid);
    }

    public static long moveBlockToBlockFluid(BlockEntity from, @Nullable Direction fromDirection, BlockEntity to, @Nullable Direction toDirection, FluidHolder fluid) {
        return safeMoveFluid(safeGetBlockFluidManager(from, fromDirection), safeGetBlockFluidManager(to, toDirection), fluid);
    }

    public static long moveItemToItemFluid(ItemStack from, ItemStack to, FluidHolder fluid) {
        return safeMoveFluid(safeGetItemFluidManager(from), safeGetItemFluidManager(to), fluid);
    }

    @ImplementedByExtension
    public static long toMillibuckets(long amount) {
        throw new NotImplementedException();
    }

    @ImplementedByExtension
    private static long getBucketAmount() {
        throw new NotImplementedException();
    }

    @ImplementedByExtension
    private static long getBottleAmount() {
        throw new NotImplementedException();
    }

    @ImplementedByExtension
    private static long getBlockAmount() {
        throw new NotImplementedException();
    }

    @ImplementedByExtension
    private static long getIngotAmount() {
        throw new NotImplementedException();
    }

    @ImplementedByExtension
    private static long getNuggetAmount() {
        throw new NotImplementedException();
    }
}
