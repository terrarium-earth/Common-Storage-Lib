package earth.terrarium.botarium.neoforge.extensions;

import earth.terrarium.botarium.common.fluid.base.FluidContainer;
import earth.terrarium.botarium.common.item.ItemStackHolder;
import earth.terrarium.botarium.neoforge.fluid.PlatformBlockFluidHandler;
import earth.terrarium.botarium.neoforge.fluid.PlatformFluidItemHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.msrandom.extensions.annotations.ClassExtension;
import net.msrandom.extensions.annotations.ImplementedByExtension;
import net.neoforged.neoforge.capabilities.Capabilities;
import org.jetbrains.annotations.Nullable;

@ClassExtension(FluidContainer.class)
@SuppressWarnings("unused")
public interface FluidContainerImpl {

    @Nullable
    @ImplementedByExtension
    static FluidContainer of(Level level, BlockPos pos, BlockState state, @Nullable BlockEntity entity, @Nullable Direction direction) {
        return PlatformBlockFluidHandler.of(level, pos, state, entity, direction);
    }

    @Nullable
    @ImplementedByExtension
    static FluidContainer of(ItemStackHolder holder) {
        return PlatformFluidItemHandler.of(holder);
    }

    @ImplementedByExtension
    static boolean holdsFluid(ItemStack stack) {
        return stack.getCapability(Capabilities.EnergyStorage.ITEM) != null;
    }

    @ImplementedByExtension
    static boolean holdsFluid(Level level, BlockPos pos, BlockState state, @Nullable BlockEntity entity, @Nullable Direction direction) {
        return level.getCapability(Capabilities.EnergyStorage.BLOCK, pos, state, entity, direction) != null;
    }
}
