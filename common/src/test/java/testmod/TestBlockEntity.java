package testmod;

import earth.terrarium.botarium.common.energy.base.BotariumEnergyBlock;
import earth.terrarium.botarium.common.energy.impl.SimpleEnergyContainer;
import earth.terrarium.botarium.common.energy.impl.WrappedBlockEnergyContainer;
import earth.terrarium.botarium.common.fluid.FluidConstants;
import earth.terrarium.botarium.common.fluid.base.BotariumFluidBlock;
import earth.terrarium.botarium.common.fluid.impl.SimpleFluidContainer;
import earth.terrarium.botarium.common.fluid.impl.WrappedBlockFluidContainer;
import earth.terrarium.botarium.common.fluid.utils.FluidParticleOptions;
import earth.terrarium.botarium.common.item.ItemApi;
import earth.terrarium.botarium.common.item.base.BotariumItemBlock;
import earth.terrarium.botarium.common.item.base.ItemContainer;
import earth.terrarium.botarium.common.item.impl.SimpleItemContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TestBlockEntity extends BlockEntity implements BotariumEnergyBlock<WrappedBlockEnergyContainer>, BotariumFluidBlock<WrappedBlockFluidContainer>, BotariumItemBlock<SimpleItemContainer> {
    private WrappedBlockFluidContainer fluidContainer;
    private WrappedBlockEnergyContainer energyContainer;
    private SimpleItemContainer itemContainer;
    private final ManaContainer manaContainer = new ManaContainer();

    public TestBlockEntity(BlockPos blockPos, BlockState blockState) {
        this(TestMod.EXAMPLE_BLOCK_ENTITY.get(), blockPos, blockState);
    }

    public TestBlockEntity(BlockEntityType<?> entityType, BlockPos blockPos, BlockState blockState) {
        super(entityType, blockPos, blockState);
    }

    @Override
    public final WrappedBlockEnergyContainer getEnergyStorage(Level level, BlockPos pos, BlockState state, @Nullable BlockEntity entity, @Nullable Direction direction) {
        return energyContainer == null ? energyContainer = new WrappedBlockEnergyContainer(entity, new SimpleEnergyContainer(1000000, Integer.MAX_VALUE)) : energyContainer;
    }

    @Override
    public WrappedBlockFluidContainer getFluidContainer(Level level, BlockPos pos, BlockState state, @Nullable BlockEntity entity, @Nullable Direction direction) {
        return fluidContainer == null ? fluidContainer = new WrappedBlockFluidContainer(entity, new SimpleFluidContainer(FluidConstants.fromMillibuckets(2000), 1, (i, fluidHolder) -> true)) : fluidContainer;
    }

    @Override
    public @Nullable SimpleItemContainer getItemContainer(Level level, BlockPos pos, @Nullable BlockState state, @Nullable BlockEntity entity, @Nullable Direction direction) {
        return itemContainer == null ? itemContainer = new SimpleItemContainer(9, level, pos) : itemContainer;
    }

    public WrappedBlockFluidContainer getFluidContainer() {
        return getFluidContainer(level, worldPosition, getBlockState(), this, null);
    }

    public ManaContainer getManaContainer() {
        return manaContainer;
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

            ItemContainer container = ItemApi.SIDED.find(level, getBlockPos().above(), Direction.DOWN);
            ItemContainer thisContainer = ItemApi.SIDED.find(level, getBlockPos(), Direction.UP);
            if (container != null && thisContainer != null && !container.isEmpty()) {
                ItemStack itemStack = container.extractItem(1, true);
                ItemStack inserted = thisContainer.insertItem(itemStack, true);
                if (!itemStack.isEmpty() && itemStack.getCount() == inserted.getCount()) {
                    ItemStack itemStack1 = container.extractItem(1, false);
                    thisContainer.insertItem(itemStack1, false);
                }
            }
        }
    }
}
