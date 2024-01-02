package earth.terrarium.botarium.fabric.extensions;

import earth.terrarium.botarium.common.fluid.FluidApi;
import earth.terrarium.botarium.common.fluid.base.FluidContainer;
import earth.terrarium.botarium.common.fluid.base.ItemFluidContainer;
import earth.terrarium.botarium.common.item.ItemStackHolder;
import earth.terrarium.botarium.fabric.fluid.holder.ItemStackStorage;
import earth.terrarium.botarium.fabric.fluid.storage.PlatformFluidContainer;
import earth.terrarium.botarium.fabric.fluid.storage.PlatformFluidItemHandler;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.msrandom.extensions.annotations.ClassExtension;
import net.msrandom.extensions.annotations.ImplementsBaseElement;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("ALL")
@ClassExtension(FluidApi.class)
public class FluidApiImpl {
    @ImplementsBaseElement
    public static ItemFluidContainer getItemFluidContainer(ItemStackHolder stack) {
        return isFluidContainingItem(stack.getStack().copy()) ? new PlatformFluidItemHandler(stack) : null;
    }

    @ImplementsBaseElement
    public static FluidContainer getBlockFluidContainer(BlockEntity entity, @Nullable Direction direction) {
        Storage<FluidVariant> storage = FluidStorage.SIDED.find(entity.getLevel(), entity.getBlockPos(), direction);
        return storage == null ? null : new PlatformFluidContainer(storage);
    }

    @ImplementsBaseElement
    public static boolean isFluidContainingBlock(BlockEntity entity, @Nullable Direction direction) {
        return FluidStorage.SIDED.find(entity.getLevel(), entity.getBlockPos(), direction) != null;
    }

    @ImplementsBaseElement
    public static boolean isFluidContainingItem(ItemStack stack) {
        return FluidStorage.ITEM.find(stack, ItemStackStorage.of(stack)) != null;
    }
}
