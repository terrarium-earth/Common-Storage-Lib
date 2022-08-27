package earth.terrarium.botarium;

import earth.terrarium.botarium.api.EnergyCapable;
import earth.terrarium.botarium.api.BlockEnergyContainer;
import earth.terrarium.botarium.api.EnergyContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityImpl extends BlockEntity implements EnergyCapable<BlockEntityImpl> {
    private final BlockEnergyContainer blockEnergyContainer = new BlockEnergyContainer(1000000);

    public BlockEntityImpl(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    @Override
    public EnergyContainer getEnergyStorage(BlockEntityImpl blockEntity) {
        return blockEntity.blockEnergyContainer;
    }
}
