package earth.terrarium.botarium.forge.extensions;

import earth.terrarium.botarium.api.fluid.FluidHolder;
import earth.terrarium.botarium.api.fluid.FluidHooks;
import earth.terrarium.botarium.api.fluid.PlatformFluidHandler;
import earth.terrarium.botarium.forge.fluid.ForgeFluidHandler;
import earth.terrarium.botarium.forge.fluid.ForgeFluidHolder;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidType;
import net.msrandom.extensions.annotations.ClassExtension;
import net.msrandom.extensions.annotations.ImplementsBaseElement;
import org.jetbrains.annotations.Nullable;

@ClassExtension(FluidHooks.class)
public class FluidManagerImpl {
    @ImplementsBaseElement
    public static FluidHolder newFluidHolder(Fluid fluid, long amount, CompoundTag tag) {
        return new ForgeFluidHolder(fluid, (int) amount, tag);
    }

    @ImplementsBaseElement
    public static FluidHolder fluidFromCompound(CompoundTag compoundTag) {
        return ForgeFluidHolder.fromCompound(compoundTag);
    }

    @ImplementsBaseElement
    public static PlatformFluidHandler getItemFluidManager(ItemStack stack) {
        return new ForgeFluidHandler(stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).orElseThrow(IllegalArgumentException::new));
    }

    @ImplementsBaseElement
    public static PlatformFluidHandler getBlockFluidManager(BlockEntity entity, @Nullable Direction direction) {
        return new ForgeFluidHandler(entity.getCapability(ForgeCapabilities.FLUID_HANDLER, direction).orElseThrow(IllegalArgumentException::new));
    }

    @ImplementsBaseElement
    public static boolean isFluidContainingBlock(BlockEntity entity, @Nullable Direction direction) {
        return entity.getCapability(ForgeCapabilities.FLUID_HANDLER, direction).isPresent();
    }

    @ImplementsBaseElement
    public static boolean isFluidContainingItem(ItemStack stack) {
        return stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).isPresent();
    }

    @ImplementsBaseElement
    public static FluidHolder emptyFluid() {
        return ForgeFluidHolder.EMPTY;
    }

    @ImplementsBaseElement
    public static long buckets(int buckets) {
        return (long) FluidType.BUCKET_VOLUME * buckets;
    }

    @ImplementsBaseElement
    public static long toMillibuckets(long amount) {
        return amount;
    }

    @ImplementsBaseElement
    private static long getBucketAmount() {
        return FluidType.BUCKET_VOLUME;
    }

    @ImplementsBaseElement
    private static long getBottleAmount() {
        return FluidType.BUCKET_VOLUME / 4;
    }

    @ImplementsBaseElement
    private static long getBlockAmount() {
        return FluidType.BUCKET_VOLUME;
    }

    @ImplementsBaseElement
    private static long getIngotAmount() {
        return 90;
    }

    @ImplementsBaseElement
    private static long getNuggetAmount() {
        return 10;
    }
}
