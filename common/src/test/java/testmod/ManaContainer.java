package testmod;

import earth.terrarium.botarium.common.generic.util.AmountBasedContainer;
import earth.terrarium.botarium.util.Serializable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;

public class ManaContainer implements AmountBasedContainer, Serializable {
    long storedAmount = 0;

    @Override
    public long getStoredAmount() {
        return storedAmount;
    }

    @Override
    public long getCapacity() {
        return 10000;
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
    public long insert(long amount, boolean simulate) {
        long inserted = Mth.clamp(amount, 0, Math.min(maxInsert(), getCapacity() - getStoredAmount()));
        if (simulate) return inserted;
        this.setValue(this.storedAmount + inserted);
        return inserted;
    }

    @Override
    public long extract(long amount, boolean simulate) {
        long extracted = Mth.clamp(amount, 0, Math.min(maxExtract(), getStoredAmount()));
        if (simulate) return extracted;
        this.setValue(this.storedAmount - extracted);
        return extracted;
    }

    @Override
    public long maxInsert() {
        return Integer.MAX_VALUE;
    }

    @Override
    public long maxExtract() {
        return Integer.MAX_VALUE;
    }

    @Override
    public void clearContent() {
        this.storedAmount = 0;
    }

    public void setValue(long value) {
        this.storedAmount = value;
    }

    @Override
    public void deserialize(CompoundTag nbt) {
        this.storedAmount = nbt.getLong("Mana");
    }

    @Override
    public CompoundTag serialize(CompoundTag nbt) {
        nbt.putLong("Mana", this.storedAmount);
        return nbt;
    }
}
