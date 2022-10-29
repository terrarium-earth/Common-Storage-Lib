package testmod;

import earth.terrarium.botarium.api.fluid.FluidHooks;
import earth.terrarium.botarium.api.fluid.PlatformFluidHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class ExampleN2SPipeBlockEntity extends TestBlockEntity {

    public ExampleN2SPipeBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(TestMod.EXAMPLE_PIPE_ENTITY.get(), blockPos, blockState);
    }

    @Override
    public void tick() {
        if (!this.getLevel().isClientSide) {
            BlockEntity forwardBlock = level.getBlockEntity(getBlockPos().north());
            BlockEntity backBlock = level.getBlockEntity(getBlockPos().south());

            PlatformFluidHandler forwardTank = FluidHooks.safeGetBlockFluidManager(forwardBlock, Direction.NORTH).orElse(null);
            PlatformFluidHandler backTank = FluidHooks.safeGetBlockFluidManager(backBlock, Direction.SOUTH).orElse(null);

            if (forwardTank != null && backTank != null && !forwardTank.getFluidInTank(0).isEmpty()) {
                FluidHooks.moveFluid(forwardTank, backTank, forwardTank.getFluidInTank(0));
            }
        }
    }
}
