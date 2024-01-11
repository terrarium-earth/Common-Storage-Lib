package earth.terrarium.botarium.forge.extensions;

import earth.terrarium.botarium.common.fluid.base.FluidContainer;
import earth.terrarium.botarium.common.fluid.base.ItemFluidContainer;
import earth.terrarium.botarium.common.item.ItemStackHolder;
import earth.terrarium.botarium.forge.fluid.PlatformBlockFluidHandler;
import earth.terrarium.botarium.forge.fluid.PlatformFluidItemHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.msrandom.extensions.annotations.ClassExtension;
import net.msrandom.extensions.annotations.ImplementsBaseElement;
import org.jetbrains.annotations.Nullable;

@ClassExtension(FluidContainer.class)
public interface FluidContainerImpl {
    @Nullable
    @ImplementsBaseElement
    static FluidContainer of(Level level, BlockPos pos, BlockState state, @Nullable BlockEntity entity, @Nullable Direction direction) {
        var blockEntity = (entity == null ? level.getBlockEntity(pos) : entity);
        if (blockEntity != null) {
            return blockEntity.getCapability(ForgeCapabilities.FLUID_HANDLER, direction).map(PlatformBlockFluidHandler::new).orElse(null);
        }
        return null;
    }

    @ImplementsBaseElement
    static ItemFluidContainer of(ItemStackHolder holder) {
        return holder.getStack().getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).map(handler -> new PlatformFluidItemHandler(holder, handler)).orElse(null);
    }

    @ImplementsBaseElement
    static boolean holdsFluid(Level level, BlockPos pos, BlockState state, @Nullable BlockEntity entity, @Nullable Direction direction) {
        var blockEntity = (entity == null ? level.getBlockEntity(pos) : entity);
        if (blockEntity != null) {
            return blockEntity.getCapability(ForgeCapabilities.FLUID_HANDLER, direction).isPresent();
        }
        return false;
    }

    @ImplementsBaseElement
    static boolean holdsFluid(ItemStack stack) {
        return stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).isPresent();
    }
}
