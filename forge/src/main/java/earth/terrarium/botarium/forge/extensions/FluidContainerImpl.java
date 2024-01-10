package earth.terrarium.botarium.forge.extensions;

import earth.terrarium.botarium.common.fluid.base.FluidContainer;
import earth.terrarium.botarium.forge.fluid.PlatformBlockFluidHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.msrandom.extensions.annotations.ClassExtension;
import net.msrandom.extensions.annotations.ImplementsBaseElement;
import org.jetbrains.annotations.Nullable;

@ClassExtension(FluidContainer.class)
public class FluidContainerImpl {
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
    static boolean holdsFluid(Level level, BlockPos pos, BlockState state, @Nullable BlockEntity entity, @Nullable Direction direction) {
        var blockEntity = (entity == null ? level.getBlockEntity(pos) : entity);
        if (blockEntity != null) {
            return blockEntity.getCapability(ForgeCapabilities.FLUID_HANDLER, direction).isPresent();
        }
        return false;
    }
}
