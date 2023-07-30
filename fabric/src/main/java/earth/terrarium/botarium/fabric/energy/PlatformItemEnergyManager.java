package earth.terrarium.botarium.fabric.energy;

import earth.terrarium.botarium.common.energy.base.EnergyContainer;
import earth.terrarium.botarium.common.energy.base.EnergySnapshot;
import earth.terrarium.botarium.common.energy.impl.SimpleEnergySnapshot;
import earth.terrarium.botarium.common.item.ItemStackHolder;
import earth.terrarium.botarium.fabric.fluid.holder.ItemStackStorage;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import team.reborn.energy.api.EnergyStorage;

@SuppressWarnings("UnstableApiUsage")
public record PlatformItemEnergyManager(ItemStackHolder holder, ContainerItemContext context,
                                        EnergyStorage energy) implements EnergyContainer {

    public PlatformItemEnergyManager(ItemStackHolder stack) {
        this(stack, ItemStackStorage.of(stack.getStack()));
    }

    public PlatformItemEnergyManager(ItemStackHolder stack, ContainerItemContext context) {
        this(stack, context, EnergyStorage.ITEM.find(stack.getStack(), context));
    }

    @Override
    public long insertEnergy(long maxAmount, boolean simulate) {
        try (Transaction txn = Transaction.openOuter()) {
            long insert = energy.insert(maxAmount, txn);
            if (simulate) txn.abort();
            else {
                txn.commit();
                holder.setStack(context.getItemVariant().toStack());
            }
            return insert;
        }
    }

    @Override
    public long extractEnergy(long maxAmount, boolean simulate) {
        try (Transaction txn = Transaction.openOuter()) {
            long extract = energy.extract(maxAmount, txn);
            if (simulate) txn.abort();
            else {
                txn.commit();
                holder.setStack(context.getItemVariant().toStack());
            }
            return extract;
        }
    }

    @Override
    public void setEnergy(long energy) {
        try (Transaction txn = Transaction.openOuter()) {
            if (energy > this.energy.getAmount()) {
                this.energy.insert(energy - this.energy.getAmount(), txn);
            } else if (energy < this.energy.getAmount()) {
                this.energy.extract(this.energy.getAmount() - energy, txn);
            }
            txn.commit();
            holder.setStack(context.getItemVariant().toStack());
        }
    }

    @Override
    public long getStoredEnergy() {
        return energy.getAmount();
    }

    @Override
    public long getMaxCapacity() {
        return energy.getCapacity();
    }

    @Override
    public long maxInsert() {
        return energy.getCapacity();
    }

    @Override
    public long maxExtract() {
        return energy.getCapacity();
    }

    @Override
    public boolean allowsInsertion() {
        return energy.supportsInsertion();
    }

    @Override
    public boolean allowsExtraction() {
        return energy.supportsExtraction();
    }

    @Override
    public EnergySnapshot createSnapshot() {
        return new SimpleEnergySnapshot(this);
    }

    @Override
    public void deserialize(CompoundTag nbt) {

    }

    @Override
    public CompoundTag serialize(CompoundTag nbt) {
        return nbt;
    }

    @Override
    public void clearContent() {
        try (Transaction txn = Transaction.openOuter()) {
            energy.extract(energy.getAmount(), txn);
            txn.commit();
            holder.setStack(context.getItemVariant().toStack());
        }
    }
}
