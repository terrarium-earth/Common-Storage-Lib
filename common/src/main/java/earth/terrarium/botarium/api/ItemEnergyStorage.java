package earth.terrarium.botarium.api;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;

public class ItemEnergyStorage implements EnergyHoldable {
    private final ItemStack stack;
    private final long capacity;

    public ItemEnergyStorage(ItemStack stack, long maxCapacity) {
        this.stack = stack;
        this.capacity = maxCapacity;
    }

    @Override
    public long insertEnergy(long maxAmount) {
        long storedEnergy = getStoredEnergy();
        long extracted = Mth.clamp(maxAmount, 0, this.getMaxCapacity() - storedEnergy);
        setEnergy(storedEnergy + extracted);
        return extracted;
    }

    @Override
    public long extractEnergy(long maxAmount) {
        long storedEnergy = getStoredEnergy();
        long extracted = Mth.clamp(maxAmount, 0, storedEnergy);
        setEnergy(storedEnergy - extracted);
        return extracted;
    }

    @Override
    public void setEnergy(long energy) {
        stack.getOrCreateTag().putLong("Energy", energy);
    }

    @Override
    public long getStoredEnergy() {
        return stack.getOrCreateTag().getLong("Energy");
    }

    @Override
    public long getMaxCapacity() {
        return capacity;
    }

    @Override
    public CompoundTag serialize(CompoundTag tag) {
        return tag;
    }

    @Override
    public void deseralize(CompoundTag tag) {}
}
