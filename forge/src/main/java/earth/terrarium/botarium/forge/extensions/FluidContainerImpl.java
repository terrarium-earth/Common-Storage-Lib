package earth.terrarium.botarium.forge.extensions;

import earth.terrarium.botarium.common.fluid.FluidApi;
import earth.terrarium.botarium.common.fluid.base.FluidContainer;
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
import net.msrandom.extensions.annotations.ImplementedByExtension;
import org.jetbrains.annotations.Nullable;

@ClassExtension(FluidContainer.class)
@SuppressWarnings("unused")
public interface FluidContainerImpl {

    @Nullable
    @ImplementedByExtension
    static FluidContainer of(Level level, BlockPos pos, BlockState state, @Nullable BlockEntity entity, @Nullable Direction direction) {
        PlatformBlockFluidHandler platformBlockFluidHandler = PlatformBlockFluidHandler.of(level, pos, state, entity, direction);
        return platformBlockFluidHandler == null ? FluidApi.getAPIFluidContainer(level, pos, state, entity, direction) : platformBlockFluidHandler;
    }

    @Nullable
    @ImplementedByExtension
    static FluidContainer of(ItemStackHolder holder) {
        return PlatformFluidItemHandler.of(holder);
    }

    @ImplementedByExtension
    static boolean holdsFluid(ItemStack stack) {
        return stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).isPresent();
    }

    @ImplementedByExtension
    static boolean holdsFluid(Level level, BlockPos pos, BlockState state, @Nullable BlockEntity entity, @Nullable Direction direction) {
        return (entity != null && entity.getCapability(ForgeCapabilities.FLUID_HANDLER, direction).isPresent()) || FluidApi.getAPIFluidContainer(level, pos, state, entity, direction) != null;
    }
}
