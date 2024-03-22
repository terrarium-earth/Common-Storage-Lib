package testmod;

import earth.terrarium.botarium.common.energy.base.BotariumEnergyBlock;
import earth.terrarium.botarium.common.energy.impl.SimpleEnergyContainer;
import earth.terrarium.botarium.common.energy.impl.WrappedBlockEnergyContainer;
import earth.terrarium.botarium.common.fluid.base.BotariumFluidBlock;
import earth.terrarium.botarium.common.fluid.impl.SimpleFluidContainer;
import earth.terrarium.botarium.common.fluid.impl.WrappedBlockFluidContainer;
import earth.terrarium.botarium.common.fluid.utils.FluidHooks;
import earth.terrarium.botarium.common.item.base.BotariumItemBlock;
import earth.terrarium.botarium.common.item.base.ItemContainer;
import earth.terrarium.botarium.common.item.impl.SimpleItemContainer;
import earth.terrarium.botarium.common.item.impl.WrappedBlockItemContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class TestBlockEntity extends BlockEntity implements BotariumEnergyBlock<WrappedBlockEnergyContainer>, BotariumFluidBlock<WrappedBlockFluidContainer>, BotariumItemBlock<WrappedBlockItemContainer<SimpleItemContainer>> {
    public WrappedBlockFluidContainer fluidContainer;
    private WrappedBlockItemContainer<SimpleItemContainer> container;
    private WrappedBlockEnergyContainer energyContainer;

    public TestBlockEntity(BlockPos blockPos, BlockState blockState) {
        this(TestMod.EXAMPLE_BLOCK_ENTITY.get(), blockPos, blockState);
    }

    public TestBlockEntity(BlockEntityType<?> entityType, BlockPos blockPos, BlockState blockState) {
        super(entityType, blockPos, blockState);
    }

    @Override
    public final WrappedBlockEnergyContainer getEnergyStorage() {
        return energyContainer == null ? this.energyContainer = new WrappedBlockEnergyContainer(this, new SimpleEnergyContainer(1000000)) : this.energyContainer;
    }

    @Override
    public WrappedBlockFluidContainer getFluidContainer() {
        return fluidContainer == null ? this.fluidContainer = new WrappedBlockFluidContainer(this, new SimpleFluidContainer(FluidHooks.buckets(2), 1, (i, fluidHolder) -> true)) : this.fluidContainer;
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        this.saveAdditional(tag);
        return tag;
    }

    public void tick() {
        ItemContainer insertContainer = ItemContainer.of(level, getBlockPos().below(), null);
        if (insertContainer != null) {
            WrappedBlockItemContainer<SimpleItemContainer> itemContainer = getItemContainer();
            if (itemContainer != null) {
                ItemStack extracted = itemContainer.extractItem(1, true);
                if (!extracted.isEmpty()) {
                    ItemStack inserted = insertContainer.insertItem(extracted, true);
                    if (!inserted.isEmpty()) {
                        container.extractItem(1, false);
                        insertContainer.insertItem(extracted, false);
                    }
                }
            }
        }
    }

    @Override
    public @Nullable WrappedBlockItemContainer<SimpleItemContainer> getItemContainer(Level level, BlockPos pos, BlockState state, @Nullable BlockEntity entity, @Nullable Direction direction) {
        return container == null ? this.container = new WrappedBlockItemContainer<>(new SimpleItemContainer(9), level, pos, state) : this.container;
    }

    public WrappedBlockItemContainer<SimpleItemContainer> getItemContainer() {
        if (getLevel() == null) return null;
        return getItemContainer(getLevel(), getBlockPos(), getBlockState(), this, null);
    }
}
