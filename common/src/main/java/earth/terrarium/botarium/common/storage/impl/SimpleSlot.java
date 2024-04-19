package earth.terrarium.botarium.common.storage.impl;

import earth.terrarium.botarium.common.storage.base.UnitSlot;
import earth.terrarium.botarium.common.storage.util.UpdateManager;
import earth.terrarium.botarium.common.transfer.base.TransferUnit;
import net.minecraft.util.Tuple;

public class SimpleSlot<T extends TransferUnit<?>> implements UnitSlot<T>, UpdateManager<Tuple<T, Long>> {
    private final T initialUnit;
    private final long slotLimit;
    private final Runnable onUpdate;
    private T unit;
    private long amount = 0;

    public SimpleSlot(T initialUnit, long slotLimit, Runnable onUpdate) {
        this.unit = initialUnit;
        this.initialUnit = initialUnit;
        this.slotLimit = slotLimit;
        this.onUpdate = onUpdate;
    }

    @Override
    public long getLimit() {
        return slotLimit;
    }

    @Override
    public boolean isValueValid(T unit) {
        return true;
    }

    @Override
    public T getUnit() {
        return unit;
    }

    @Override
    public long getAmount() {
        return amount;
    }

    protected void setUnit(T unit) {
        this.unit = unit;
    }

    protected void setAmount(long amount) {
        this.amount = amount;
    }

    @Override
    public long insert(T unit, long amount, boolean simulate) {
        if (!this.unit.isBlank() && !match(unit)) return 0;
        long inserted = 0;
        if (this.unit.isBlank()) {
            this.unit = unit;
            inserted = Math.min(amount, getLimit());
            if (!simulate) {
                this.amount = inserted;
            }
        } else {
            inserted = Math.min(amount, getLimit() - this.amount);
            if (!simulate) {
                this.amount += inserted;
            }
        }
        return inserted;
    }

    @Override
    public long extract(T unit, long amount, boolean simulate) {
        if (this.unit.isBlank() || !match(unit)) return 0;
        long extracted = Math.min(amount, this.amount);
        if (!simulate) {
            this.amount -= extracted;
            if (this.amount == 0) {
                this.unit = this.initialUnit;
            }
        }
        return extracted;
    }

    @Override
    public Tuple<T, Long> createSnapshot() {
        return new Tuple<>(this.unit, this.amount);
    }

    @Override
    public void readSnapshot(Tuple<T, Long> snapshot) {
        this.unit = snapshot.getA();
        this.amount = snapshot.getB();
    }

    public boolean match(T unit) {
        return this.unit.unit() == unit.unit() && this.unit.componentsMatch(unit.components());
    }

    @Override
    public void update() {
        onUpdate.run();
    }
}
