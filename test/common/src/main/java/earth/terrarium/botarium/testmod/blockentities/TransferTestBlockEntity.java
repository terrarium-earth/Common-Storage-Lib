package earth.terrarium.botarium.testmod.blockentities;

import earth.terrarium.botarium.energy.EnergyApi;
import earth.terrarium.botarium.energy.EnergyProvider;
import earth.terrarium.botarium.energy.impl.SimpleValueStorage;
import earth.terrarium.botarium.fluid.FluidApi;
import earth.terrarium.botarium.fluid.impl.SimpleFluidStorage;
import earth.terrarium.botarium.fluid.util.FluidProvider;
import earth.terrarium.botarium.item.ItemApi;
import earth.terrarium.botarium.item.impl.SimpleItemStorage;
import earth.terrarium.botarium.item.util.ItemProvider;
import earth.terrarium.botarium.resources.fluid.FluidResource;
import earth.terrarium.botarium.resources.fluid.util.FluidAmounts;
import earth.terrarium.botarium.resources.item.ItemResource;
import earth.terrarium.botarium.storage.base.CommonStorage;
import earth.terrarium.botarium.storage.base.ValueStorage;
import earth.terrarium.botarium.storage.util.TransferUtil;
import earth.terrarium.botarium.testmod.TestMod;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class TransferTestBlockEntity extends BlockEntity implements EnergyProvider.BlockEntity, FluidProvider.BlockEntity, ItemProvider.BlockEntity {
    private final SimpleValueStorage energy = new SimpleValueStorage(1000, this);
    private final SimpleFluidStorage fluids = new SimpleFluidStorage(1, FluidAmounts.BUCKET, this);
    private final SimpleItemStorage items = new SimpleItemStorage(1, this);

    public TransferTestBlockEntity(BlockPos pos, BlockState blockState) {
        super(TestMod.TRANSFER_BLOCK_ENTITY.get(), pos, blockState);
    }

    public void tick() {
        ValueStorage foundEnergy = EnergyApi.BLOCK.find(level, getBlockPos().above(), Direction.DOWN);
        if (foundEnergy != null) {
            TransferUtil.moveValue(foundEnergy, energy, 50, false);
        }

        CommonStorage<FluidResource> foundFluid = FluidApi.BLOCK.find(level, getBlockPos().above(), Direction.DOWN);
        if (foundFluid != null) {
            TransferUtil.moveAny(foundFluid, fluids, FluidAmounts.BOTTLE, false);
        }

        CommonStorage<ItemResource> foundItem = ItemApi.BLOCK.find(level, getBlockPos().above(), Direction.DOWN);
        if (foundItem != null) {
            TransferUtil.moveAny(foundItem, items, 1, false);
        }

        // look below
        ValueStorage foundEnergyBelow = EnergyApi.BLOCK.find(level, getBlockPos().below(), Direction.UP);
        if (foundEnergyBelow != null) {
            TransferUtil.moveValue(energy, foundEnergyBelow, 50, false);
        }

        CommonStorage<FluidResource> foundFluidBelow = FluidApi.BLOCK.find(level, getBlockPos().below(), Direction.UP);
        if (foundFluidBelow != null) {
            TransferUtil.moveAny(fluids, foundFluidBelow, FluidAmounts.BOTTLE, false);
        }

        CommonStorage<ItemResource> foundItemBelow = ItemApi.BLOCK.find(level, getBlockPos().below(), Direction.UP);
        if (foundItemBelow != null) {
            TransferUtil.moveAny(items, foundItemBelow, 1, false);
        }
    }

    @Override
    public ValueStorage getEnergy(@Nullable Direction direction) {
        return energy;
    }

    @Override
    public CommonStorage<FluidResource> getFluids(@Nullable Direction direction) {
        return fluids;
    }

    @Override
    public CommonStorage<ItemResource> getItems(@Nullable Direction direction) {
        return items;
    }
}
