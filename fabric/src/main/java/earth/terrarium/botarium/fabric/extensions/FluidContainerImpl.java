package earth.terrarium.botarium.fabric.extensions;

import earth.terrarium.botarium.common.fluid.base.FluidContainer;
import earth.terrarium.botarium.common.fluid.base.ItemFluidContainer;
import earth.terrarium.botarium.common.item.ItemStackHolder;
import earth.terrarium.botarium.fabric.fluid.holder.ItemStackStorage;
import earth.terrarium.botarium.fabric.fluid.storage.PlatformFluidContainer;
import earth.terrarium.botarium.fabric.fluid.storage.PlatformFluidItemHandler;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.msrandom.extensions.annotations.ClassExtension;
import net.msrandom.extensions.annotations.ImplementsBaseElement;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("UnstableApiUsage")
@ClassExtension(FluidContainer.class)
public interface FluidContainerImpl {
    @Nullable
    @ImplementsBaseElement
    static FluidContainer of(Level level, BlockPos pos, BlockState state, @Nullable BlockEntity entity, @Nullable Direction direction) {
        Storage<FluidVariant> storage = FluidStorage.SIDED.find(level, pos, state, entity, direction);
        return storage == null ? null : new PlatformFluidContainer(storage);
    }

    @ImplementsBaseElement
    static ItemFluidContainer of(ItemStackHolder holder) {
        return FluidContainer.holdsFluid(holder.getStack().copy()) ? new PlatformFluidItemHandler(holder) : null;
    }

    @ImplementsBaseElement
    static boolean holdsFluid(Level level, BlockPos pos, BlockState state, @Nullable BlockEntity entity, @Nullable Direction direction) {
        return FluidStorage.SIDED.find(level, pos, state, entity, direction) != null;
    }

    @ImplementsBaseElement
    static boolean holdsFluid(ItemStack stack) {
        return FluidStorage.ITEM.find(stack, ItemStackStorage.of(stack)) != null;
    }
}
