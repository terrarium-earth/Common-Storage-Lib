package earth.terrarium.botarium.fabric.extensions;

import earth.terrarium.botarium.api.fluid.FluidHolder;
import earth.terrarium.botarium.api.fluid.FluidHooks;
import earth.terrarium.botarium.api.fluid.PlatformFluidHandler;
import earth.terrarium.botarium.fabric.fluid.FabricFluidHandler;
import earth.terrarium.botarium.fabric.fluid.FabricFluidHolder;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.material.Fluid;
import net.msrandom.extensions.annotations.ClassExtension;
import net.msrandom.extensions.annotations.ImplementsBaseElement;
import org.jetbrains.annotations.Nullable;

@ClassExtension(FluidHooks.class)
@SuppressWarnings("UnstableApiUsage")
public class FluidHooksImpl {

    @ImplementsBaseElement
    public static FluidHolder newFluidHolder(Fluid fluid, long amount, CompoundTag tag) {
        return FabricFluidHolder.of(fluid, tag, amount);
    }

    @ImplementsBaseElement
    public static FluidHolder fluidFromCompound(CompoundTag compoundTag) {
        FabricFluidHolder fluid = FabricFluidHolder.of(null, 0);
        fluid.deserialize(compoundTag);
        return fluid;
    }

    @ImplementsBaseElement
    public static FluidHolder emptyFluid() {
        return FabricFluidHolder.EMPTY;
    }

    @ImplementsBaseElement
    public static long buckets(int buckets) {
        return FluidConstants.BUCKET * buckets;
    }

    @ImplementsBaseElement
    public static PlatformFluidHandler getItemFluidManager(ItemStack stack) {
        return new FabricFluidHandler(FluidStorage.ITEM.find(stack, ContainerItemContext.withInitial(stack)));
    }

    @ImplementsBaseElement
    public static PlatformFluidHandler getBlockFluidManager(BlockEntity entity, @Nullable Direction direction) {
        return new FabricFluidHandler(FluidStorage.SIDED.find(entity.getLevel(), entity.getBlockPos(), direction));
    }

    @ImplementsBaseElement
    public static boolean isFluidContainingBlock(BlockEntity entity, @Nullable Direction direction) {
        return FluidStorage.SIDED.find(entity.getLevel(), entity.getBlockPos(), direction) != null;
    }

    @ImplementsBaseElement
    public static boolean isFluidContainingItem(ItemStack stack) {
        return FluidStorage.ITEM.find(stack, ContainerItemContext.withInitial(stack)) != null;
    }

    @ImplementsBaseElement
    public static long toMillibuckets(long amount) {
        return amount / 81;
    }

    @ImplementsBaseElement
    public static long getBucketAmount() {
        return FluidConstants.BUCKET;
    }

    @ImplementsBaseElement
    public static long getBottleAmount() {
        return FluidConstants.BOTTLE;
    }

    @ImplementsBaseElement
    public static long getBlockAmount() {
        return FluidConstants.BLOCK;
    }

    @ImplementsBaseElement
    public static long getIngotAmount() {
        return FluidConstants.INGOT;
    }

    @ImplementsBaseElement
    public static long getNuggetAmount() {
        return FluidConstants.NUGGET;
    }
}
