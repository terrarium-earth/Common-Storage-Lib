package earth.terrarium.botarium.api.fabric;

import earth.terrarium.botarium.api.ItemEnergyHandler;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.world.item.ItemStack;
import team.reborn.energy.api.EnergyStorage;

@SuppressWarnings("UnstableApiUsage")
public class FabricEnergyItemHandler implements ItemEnergyHandler {
    private final EnergyStorage energy;

    public FabricEnergyItemHandler(ItemStack stack) {
        this.energy = EnergyStorage.ITEM.find(stack, ContainerItemContext.withInitial(stack));
    }

    @Override
    public long getCapacity() {
        return energy.getAmount();
    }

    @Override
    public long maxCapacity() {
        return energy.getCapacity();
    }

    @Override
    public long extract(int amount, boolean simulate) {
        try (Transaction txn = Transaction.openOuter()) {
            long extract = energy.extract(amount, txn);
            if(simulate) txn.abort();
            else txn.commit();
            return extract;
        }
    }

    @Override
    public long insert(int amount, boolean simulate) {
        try (Transaction txn = Transaction.openOuter()) {
            long insert = energy.insert(amount, txn);
            if(simulate) txn.abort();
            else txn.commit();
            return insert;
        }
    }
}
