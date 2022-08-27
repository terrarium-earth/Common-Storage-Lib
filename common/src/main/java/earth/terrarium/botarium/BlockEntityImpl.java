package earth.terrarium.botarium;

import earth.terrarium.botarium.api.EnergyMarker;
import earth.terrarium.botarium.api.EnergyStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityImpl extends BlockEntity implements EnergyMarker<BlockEntityImpl> {
    private final EnergyStorage energyStorage = new EnergyStorage(1000000);

    public BlockEntityImpl(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    @Override
    public EnergyStorage getEnergyStorage(BlockEntityImpl blockEntity) {
        return blockEntity.energyStorage;
    }
}
