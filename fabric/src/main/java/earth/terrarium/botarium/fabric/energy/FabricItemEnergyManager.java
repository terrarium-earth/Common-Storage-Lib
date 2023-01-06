package earth.terrarium.botarium.fabric.energy;

import earth.terrarium.botarium.common.menu.base.PlatformItemEnergyManager;
import earth.terrarium.botarium.common.item.ItemStackHolder;
import earth.terrarium.botarium.fabric.fluid.holder.ItemStackStorage;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.world.item.ItemStack;
import team.reborn.energy.api.EnergyStorage;

@SuppressWarnings("UnstableApiUsage")
public record FabricItemEnergyManager(ItemStack stack, ContainerItemContext context, EnergyStorage energy) implements PlatformItemEnergyManager {

    public FabricItemEnergyManager(ItemStack stack) {
        this(stack, ItemStackStorage.of(stack));
    }

    public FabricItemEnergyManager(ItemStack stack, ContainerItemContext context) {
        this(stack, context, EnergyStorage.ITEM.find(stack, context));
    }

    @Override
    public long getStoredEnergy() {
        return energy.getAmount();
    }

    @Override
    public long getCapacity() {
        return energy.getCapacity();
    }

    @Override
    public long extract(ItemStackHolder holder, long amount, boolean simulate) {
        try (Transaction txn = Transaction.openOuter()) {
            long extract = energy.extract(amount, txn);
            if(simulate) txn.abort();
            else txn.commit();
            holder.setStack(context.getItemVariant().toStack());
            return extract;
        }
    }

    @Override
    public long insert(ItemStackHolder holder, long amount, boolean simulate) {
        try (Transaction txn = Transaction.openOuter()) {
            long insert = energy.insert(amount, txn);
            if(simulate) txn.abort();
            else txn.commit();
            holder.setStack(context.getItemVariant().toStack());
            return insert;
        }
    }

    @Override
    public boolean supportsInsertion() {
        return energy.supportsInsertion();
    }

    @Override
    public boolean supportsExtraction() {
        return energy.supportsExtraction();
    }
}
