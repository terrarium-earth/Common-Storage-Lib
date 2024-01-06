package testmod;

import earth.terrarium.botarium.common.fluid.FluidApi;
import earth.terrarium.botarium.common.fluid.base.FluidContainer;
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

            FluidContainer forwardTank = FluidApi.getBlockFluidContainer(forwardBlock, Direction.NORTH);
            FluidContainer backTank = FluidApi.getBlockFluidContainer(backBlock, Direction.SOUTH);

            if (forwardTank != null && backTank != null && !forwardTank.getFluids().get(0).isEmpty()) {
                FluidApi.moveFluid(forwardTank, backTank, forwardTank.getFluids().get(0), false);
            }
        }
    }
}
