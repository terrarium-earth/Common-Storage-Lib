package testmod;

import earth.terrarium.botarium.api.EnergyBlock;
import earth.terrarium.botarium.api.BlockEnergyContainer;
import earth.terrarium.botarium.api.EnergyContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class ExampleBlockEntity extends BlockEntity implements EnergyBlock {
    public EnergyContainer container;
    public ExampleBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(TestMod.EXAMPLE_BLOCK_ENTITY.get(), blockPos, blockState);
    }

    @Override
    public EnergyContainer getEnergyStorage() {
        if(container == null) {
            this.container = new BlockEnergyContainer(1000000);
        }
        return container;
    }
}
