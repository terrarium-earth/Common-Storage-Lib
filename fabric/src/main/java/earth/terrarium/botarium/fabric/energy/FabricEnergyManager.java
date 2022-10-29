package earth.terrarium.botarium.fabric.energy;

import earth.terrarium.botarium.api.energy.PlatformEnergyManager;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.ApiStatus;
import team.reborn.energy.api.EnergyStorage;

@ApiStatus.Internal
@SuppressWarnings("UnstableApiUsage")
public class FabricEnergyManager implements PlatformEnergyManager {
    private final EnergyStorage energy;

    public FabricEnergyManager(ItemStack stack) {
        this.energy = EnergyStorage.ITEM.find(stack, ContainerItemContext.withInitial(stack));
    }

    public FabricEnergyManager(BlockEntity entity, Direction direction) {
        this.energy = EnergyStorage.SIDED.find(entity.getLevel(), entity.getBlockPos(), direction);
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
    public long extract(long amount, boolean simulate) {
        try (Transaction txn = Transaction.openOuter()) {
            long extract = energy.extract(amount, txn);
            if(simulate) txn.abort();
            else txn.commit();
            return extract;
        }
    }

    @Override
    public long insert(long amount, boolean simulate) {
        try (Transaction txn = Transaction.openOuter()) {
            long insert = energy.insert(amount, txn);
            if(simulate) txn.abort();
            else txn.commit();
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
