package earth.terrarium.botarium.forge.extensions;

import earth.terrarium.botarium.common.fluid.FluidApi;
import earth.terrarium.botarium.common.fluid.base.FluidContainer;
import earth.terrarium.botarium.common.fluid.base.FluidHolder;
import earth.terrarium.botarium.common.fluid.base.ItemFluidContainer;
import earth.terrarium.botarium.common.item.ItemStackHolder;
import earth.terrarium.botarium.forge.fluid.ForgeFluidHolder;
import earth.terrarium.botarium.forge.fluid.PlatformBlockFluidHandler;
import earth.terrarium.botarium.forge.fluid.PlatformFluidItemHandler;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.msrandom.extensions.annotations.ClassExtension;
import net.msrandom.extensions.annotations.ImplementsBaseElement;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("ALL")
@ClassExtension(FluidApi.class)
public class FluidApiImpl {
    @ImplementsBaseElement
    public static ItemFluidContainer getItemFluidContainer(ItemStackHolder stack) {
        return isFluidContainingItem(stack.getStack()) ? new PlatformFluidItemHandler(stack) : null;
    }

    @ImplementsBaseElement
    public static FluidContainer getBlockFluidContainer(BlockEntity entity, @Nullable Direction direction) {
        LazyOptional<IFluidHandler> capability = entity.getCapability(ForgeCapabilities.FLUID_HANDLER, direction);
        return capability.isPresent() ? new PlatformBlockFluidHandler(capability.orElseThrow(IllegalArgumentException::new)) : null;
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
    public static FluidHolder fluidFromCompound(CompoundTag compoundTag) {
        return ForgeFluidHolder.fromCompound(compoundTag);
    }


    @ImplementsBaseElement
    public static long toMillibuckets(long amount) {
        return amount;
    }

    @ImplementsBaseElement
    public static long fromMillibuckets(long amount) {
        return amount;
    }

    @ImplementsBaseElement
    public static long getBucketAmount() {
        return FluidType.BUCKET_VOLUME;
    }

    @ImplementsBaseElement
    public static long getBottleAmount() {
        return FluidType.BUCKET_VOLUME / 4;
    }

    @ImplementsBaseElement
    public static long getBlockAmount() {
        return FluidType.BUCKET_VOLUME;
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
