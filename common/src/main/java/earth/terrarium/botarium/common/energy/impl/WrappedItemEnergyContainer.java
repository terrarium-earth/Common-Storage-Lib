package earth.terrarium.botarium.common.energy.impl;

import earth.terrarium.botarium.common.menu.base.EnergyContainer;
import earth.terrarium.botarium.common.menu.base.EnergySnapshot;
import earth.terrarium.botarium.util.Updatable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public record WrappedItemEnergyContainer(ItemStack stack, EnergyContainer container) implements EnergyContainer, Updatable<ItemStack> {

    public WrappedItemEnergyContainer {
        if(stack.hasTag()) container.deserialize(stack.getTag());
    }

    @Override
    public long insertEnergy(long energy, boolean simulate) {
        long insert = container.insertEnergy(energy, simulate);
        if (!simulate) update(stack);
        return insert;
    }

    @Override
    public long extractEnergy(long energy, boolean simulate) {
        long extract = container.extractEnergy(energy, simulate);
        if (!simulate) update(stack);
        return extract;
    }

    @Override
    public void setEnergy(long energy) {
        container.setEnergy(energy);
        update(stack);
    }

    @Override
    public long getStoredEnergy() {
        return container.getStoredEnergy();
    }

    @Override
    public long getMaxCapacity() {
        return container.getMaxCapacity();
    }

    @Override
    public long maxInsert() {
        return container.maxInsert();
    }

    @Override
    public long maxExtract() {
        return container.maxExtract();
    }

    @Override
    public boolean allowsInsertion() {
        return container.allowsInsertion();
    }

    @Override
    public boolean allowsExtraction() {
        return container.allowsExtraction();
    }

    @Override
    public EnergySnapshot createSnapshot() {
        return container.createSnapshot();
    }

    @Override
    public void deserialize(CompoundTag nbt) {
        container.deserialize(nbt);
        update(stack);
    }

    @Override
    public CompoundTag serialize(CompoundTag nbt) {
        return container.serialize(nbt);
    }

    @Override
    public void update(ItemStack object) {
        container.serialize(object.getOrCreateTag());
    }

    @Override
    public void clearContent() {
        container.clearContent();
    }
}
