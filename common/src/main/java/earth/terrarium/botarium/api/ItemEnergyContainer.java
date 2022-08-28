package earth.terrarium.botarium.api;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;

public class ItemEnergyContainer implements EnergyContainer {
    private final ItemStack stack;
    private final long capacity;
    private long energy;

    public ItemEnergyContainer(ItemStack stack, long maxCapacity) {
        this.stack = stack;
        this.capacity = maxCapacity;
    }

    @Override
    public long insertEnergy(long maxAmount) {
        long inserted = Mth.clamp(maxAmount, 0, getMaxCapacity() - getStoredEnergy());
        this.setEnergy(this.energy + inserted);
        return inserted;
    }

    @Override
    public long extractEnergy(long maxAmount) {
        long extracted = Mth.clamp(maxAmount, 0, getStoredEnergy());
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
    public CompoundTag serialize(CompoundTag tag) {
        tag.putLong("Energy", this.energy);
        return tag;
    }

    @Override
    public void deseralize(CompoundTag tag) {
        this.energy = tag.getLong("Energy");
    }
}
