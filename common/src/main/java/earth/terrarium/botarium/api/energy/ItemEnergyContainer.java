package earth.terrarium.botarium.api.energy;

import earth.terrarium.botarium.Botarium;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ShortTag;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;

public class ItemEnergyContainer implements StatefulEnergyContainer<ItemStack> {
    private final long capacity;
    private long energy;

    public ItemEnergyContainer(ItemStack itemStack, long maxCapacity) {
        this.capacity = maxCapacity;
        this.deserialize(itemStack.getOrCreateTagElement(Botarium.BOTARIUM_DATA));
    }

    @Override
    public long insertEnergy(long maxAmount, boolean simulate) {
        long inserted = Mth.clamp(maxAmount, 0, getMaxCapacity() - getStoredEnergy());
        if(simulate) return inserted;
        this.setEnergy(this.energy + inserted);
        return inserted;
    }

    @Override
    public long extractEnergy(long maxAmount, boolean simulate) {
        long extracted = Mth.clamp(maxAmount, 0, getStoredEnergy());
        if(simulate) return extracted;
        this.setEnergy(this.energy - extracted);
        return extracted;
    }

    @Override
    public void setEnergy(long energy) {
        this.energy = energy;
    }

    @Override
    public long getStoredEnergy() {
        return energy;
    }

    @Override
    public long getMaxCapacity() {
        return capacity;
    }

    @Override
    public long maxInsert() {
        return 1024;
    }

    @Override
    public long maxExtract() {
        return 1024;
    }

    @Override
    public CompoundTag serialize(CompoundTag tag) {
        tag.putLong("Energy", this.energy);
        return tag;
    }

    @Override
    public void deserialize(CompoundTag tag) {
        this.energy = tag.getLong("Energy");
    }

    @Override
    public boolean allowsInsertion() {
        return true;
    }

    @Override
    public boolean allowsExtraction() {
        return true;
    }

    @Override
    public EnergySnapshot createSnapshot() {
        return new SimpleEnergySnapshot(this);
    }

    @Override
    public void update(ItemStack stack) {
        serialize(stack.getOrCreateTagElement(Botarium.BOTARIUM_DATA));
    }
}
