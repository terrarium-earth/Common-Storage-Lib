package earth.terrarium.botarium.forge.extensions;

import earth.terrarium.botarium.common.fluid.base.FluidHolder;
import earth.terrarium.botarium.common.fluid.utils.FluidHooks;
import earth.terrarium.botarium.common.fluid.base.PlatformFluidHandler;
import earth.terrarium.botarium.common.fluid.base.PlatformFluidItemHandler;
import earth.terrarium.botarium.forge.fluid.ForgeFluidHandler;
import earth.terrarium.botarium.forge.fluid.ForgeFluidHolder;
import earth.terrarium.botarium.forge.fluid.ForgeFluidItemHandler;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.msrandom.extensions.annotations.ClassExtension;
import net.msrandom.extensions.annotations.ImplementsBaseElement;
import org.jetbrains.annotations.Nullable;

@ClassExtension(FluidHooks.class)
public class FluidHooksImpl {
    @ImplementsBaseElement
    public static FluidHolder newFluidHolder(Fluid fluid, long amount, CompoundTag tag) {
        return new ForgeFluidHolder(fluid, (int) amount, tag);
    }

    @ImplementsBaseElement
    public static FluidHolder fluidFromCompound(CompoundTag compoundTag) {
        return ForgeFluidHolder.fromCompound(compoundTag);
    }

    @ImplementsBaseElement
    public static PlatformFluidItemHandler getItemFluidManager(ItemStack stack) {
        return new ForgeFluidItemHandler(stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).orElseThrow(IllegalArgumentException::new));
    }

    @ImplementsBaseElement
    public static PlatformFluidHandler getBlockFluidManager(BlockEntity entity, @Nullable Direction direction) {
        return new ForgeFluidHandler(entity.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, direction).orElseThrow(IllegalArgumentException::new));
    }

    @ImplementsBaseElement
    public static boolean isFluidContainingBlock(BlockEntity entity, @Nullable Direction direction) {
        return entity.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, direction).isPresent();
    }

    @ImplementsBaseElement
    public static boolean isFluidContainingItem(ItemStack stack) {
        return stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).isPresent();
    }

    @ImplementsBaseElement
    public static FluidHolder emptyFluid() {
        return ForgeFluidHolder.empty();
    }

    @ImplementsBaseElement
    public static long toMillibuckets(long amount) {
        return amount;
    }

    @ImplementsBaseElement
    public static long getBucketAmount() {
        return FluidAttributes.BUCKET_VOLUME;
    }

    @ImplementsBaseElement
    public static long getBottleAmount() {
        return FluidAttributes.BUCKET_VOLUME / 4;
    }

    @ImplementsBaseElement
    public static long getBlockAmount() {
        return FluidAttributes.BUCKET_VOLUME;
    }

    @ImplementsBaseElement
    public static long getIngotAmount() {
        return 90;
    }

    @ImplementsBaseElement
    public static long getNuggetAmount() {
        return 10;
    }
}
