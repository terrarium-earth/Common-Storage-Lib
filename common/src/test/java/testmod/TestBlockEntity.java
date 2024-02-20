package testmod;

import earth.terrarium.botarium.common.energy.base.BotariumEnergyBlock;
import earth.terrarium.botarium.common.energy.impl.SimpleEnergyContainer;
import earth.terrarium.botarium.common.energy.impl.WrappedBlockEnergyContainer;
import earth.terrarium.botarium.common.fluid.FluidConstants;
import earth.terrarium.botarium.common.fluid.base.BotariumFluidBlock;
import earth.terrarium.botarium.common.fluid.impl.SimpleFluidContainer;
import earth.terrarium.botarium.common.fluid.impl.WrappedBlockFluidContainer;
import earth.terrarium.botarium.common.fluid.utils.FluidParticleOptions;
import earth.terrarium.botarium.common.item.ItemContainerBlock;
import earth.terrarium.botarium.common.item.SerializableContainer;
import earth.terrarium.botarium.common.item.SimpleItemContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TestBlockEntity extends BlockEntity implements BotariumEnergyBlock<WrappedBlockEnergyContainer>, BotariumFluidBlock<WrappedBlockFluidContainer>, ItemContainerBlock {
    public WrappedBlockFluidContainer fluidContainer;
    private SimpleItemContainer itemContainer;
    private WrappedBlockEnergyContainer energyContainer;
    private ManaContainer manaContainer = new ManaContainer();

    public TestBlockEntity(BlockPos blockPos, BlockState blockState) {
        this(TestMod.EXAMPLE_BLOCK_ENTITY.get(), blockPos, blockState);
    }

    public TestBlockEntity(BlockEntityType<?> entityType, BlockPos blockPos, BlockState blockState) {
        super(entityType, blockPos, blockState);
    }

    @Override
    public final WrappedBlockEnergyContainer getEnergyStorage(Level level, BlockPos pos, BlockState state, @Nullable BlockEntity entity, @Nullable Direction direction) {
        return energyContainer == null ? this.energyContainer = new WrappedBlockEnergyContainer(entity, new SimpleEnergyContainer(1000000, Integer.MAX_VALUE)) : this.energyContainer;
    }

    @Override
    public WrappedBlockFluidContainer getFluidContainer(Level level, BlockPos pos, BlockState state, @Nullable BlockEntity entity, @Nullable Direction direction) {
        return fluidContainer == null ? this.fluidContainer = new WrappedBlockFluidContainer(entity, new SimpleFluidContainer(FluidConstants.fromMillibuckets(2000), 1, (i, fluidHolder) -> true)) : this.fluidContainer;
    }

    public WrappedBlockFluidContainer getFluidContainer() {
        return getFluidContainer(level, worldPosition, getBlockState(), this, null);
    }

    @Override
    public SerializableContainer getContainer() {
        return itemContainer == null ? this.itemContainer = new SimpleItemContainer(this, 1) : this.itemContainer;
    }

    @Override
    public @NotNull CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        this.saveAdditional(tag);
        return tag;
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        manaContainer.deserialize(tag);
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        manaContainer.serialize(tag);
    }

    public void tick() {
        if (!getFluidContainer().isEmpty() && level instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(new FluidParticleOptions(getFluidContainer().getFirstFluid()), worldPosition.getX() + 0.5, worldPosition.getY() + 1.5, worldPosition.getZ() + 0.5, 10, 0.25, 0.5, 0.25, 0.05);
        }
    }

    public ManaContainer getManaContainer() {
        return manaContainer;
    }
}
