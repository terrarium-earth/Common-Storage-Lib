package earth.terrarium.botarium.fabric;

import earth.terrarium.botarium.api.EnergyContainer;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import team.reborn.energy.api.EnergyStorage;

@SuppressWarnings("UnstableApiUsage")
public class FabricEnergyStorage implements EnergyStorage {
    private final ContainerItemContext ctx;
    private final EnergyContainer container;

    public FabricEnergyStorage(ContainerItemContext ctx, EnergyContainer container) {
        this.ctx = ctx;
        CompoundTag nbt = ctx.getItemVariant().getNbt();
        if(nbt != null) container.deseralize(nbt);
        this.container = container;
    }

    @Override
    public long insert(long maxAmount, TransactionContext transaction) {
        long inserted = container.insertEnergy(maxAmount);
        setChanged(transaction);
        return inserted;
    }

    @Override
    public long extract(long maxAmount, TransactionContext transaction) {
        long l = container.extractEnergy(maxAmount);
        setChanged(transaction);
        return l;
    }

    @Override
    public long getAmount() {
        return container.getStoredEnergy();
    }

    @Override
    public long getCapacity() {
        return container.getMaxCapacity();
    }

    public void setChanged(TransactionContext transaction) {
        ItemStack stack = ctx.getItemVariant().toStack();
        this.container.serialize(stack.getOrCreateTag());
        ctx.exchange(ItemVariant.of(stack), ctx.getAmount(), transaction);
    }
}
