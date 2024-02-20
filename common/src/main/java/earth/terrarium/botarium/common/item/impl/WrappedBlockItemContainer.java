package earth.terrarium.botarium.common.item.impl;

import earth.terrarium.botarium.common.item.base.ItemContainer;
import earth.terrarium.botarium.common.item.base.ItemSnapshot;
import earth.terrarium.botarium.util.Serializable;
import earth.terrarium.botarium.util.Updatable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public record WrappedBlockItemContainer<T extends ItemContainer & Serializable>(T container, Level level, BlockPos blockPos, BlockState state) implements ItemContainer, Serializable, Updatable<Void> {

    public WrappedBlockItemContainer(T container, BlockEntity entity) {
        this(container, entity.getLevel(), entity.getBlockPos(), entity.getBlockState());
    }

    @Override
    public int getSlots() {
        return container.getSlots();
    }

    @Override
    public @NotNull ItemStack getStackInSlot(int slot) {
        return container.getStackInSlot(slot);
    }

    @Override
    public int getSlotLimit(int slot) {
        return container.getSlotLimit(slot);
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return container.isItemValid(slot, stack);
    }

    @Override
    public @NotNull ItemStack insertItem(@NotNull ItemStack stack, boolean simulate) {
        return container.insertItem(stack, simulate);
    }

    @Override
    public @NotNull ItemStack insertIntoSlot(int slot, @NotNull ItemStack stack, boolean simulate) {
        return container.insertIntoSlot(slot, stack, simulate);
    }

    @Override
    public @NotNull ItemStack extractItem(int amount, boolean simulate) {
        return container.extractItem(amount, simulate);
    }

    @Override
    public @NotNull ItemStack extractFromSlot(int slot, int amount, boolean simulate) {
        return container.extractFromSlot(slot, amount, simulate);
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public ItemSnapshot createSnapshot() {
        return container.createSnapshot();
    }

    @Override
    public void loadSnapshot(ItemSnapshot snapshot) {
        container.loadSnapshot(snapshot);
    }

    @Override
    public void deserialize(CompoundTag nbt) {
        container.deserialize(nbt);
    }

    @Override
    public CompoundTag serialize(CompoundTag nbt) {
        return container.serialize(nbt);
    }

    @Override
    public void clearContent() {
        container.clearContent();
    }

    @Override
    public void update(Void ignored) {
        BlockEntity blockEntity = level.getBlockEntity(blockPos);
        if (blockEntity != null) {
            blockEntity.setChanged();
        }
        level.sendBlockUpdated(blockPos, state, state, Block.UPDATE_ALL);
    }
}
