package earth.terrarium.botarium.common.storage.impl;

import earth.terrarium.botarium.common.data.impl.SingleFluidData;
import earth.terrarium.botarium.common.data.impl.SingleItemData;
import earth.terrarium.botarium.common.data.utils.ContainerSerializer;
import earth.terrarium.botarium.common.storage.base.UnitSlot;
import earth.terrarium.botarium.common.storage.util.UpdateManager;
import earth.terrarium.botarium.common.transfer.base.TransferUnit;
import earth.terrarium.botarium.common.transfer.impl.FluidUnit;
import earth.terrarium.botarium.common.transfer.impl.ItemUnit;
import net.minecraft.util.Tuple;

public class SimpleSlot<T extends TransferUnit<?>, S> implements UnitSlot<T>, UpdateManager<S> {
    private final T initialUnit;
    private final long slotLimit;
    private final Runnable onUpdate;
    private final ContainerSerializer<SimpleSlot<T, ?>, S> serializer;
    private T unit;
    private long amount = 0;

    public SimpleSlot(T initialUnit, long slotLimit, Runnable onUpdate, ContainerSerializer<SimpleSlot<T, ?>, S> serializer) {
        this.unit = initialUnit;
        this.initialUnit = initialUnit;
        this.slotLimit = slotLimit;
        this.onUpdate = onUpdate;
        this.serializer = serializer;
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

    public void setUnit(T unit) {
        this.unit = unit;
    }

    public void setAmount(long amount) {
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
    public S createSnapshot() {
        return serializer.captureData(this);
    }

    @Override
    public void readSnapshot(S snapshot) {
        serializer.applyData(this, snapshot);
    }

    public boolean match(T unit) {
        return this.unit.unit() == unit.unit() && this.unit.componentsMatch(unit.components());
    }

    @Override
    public void update() {
        onUpdate.run();
    }

    public static class Item extends SimpleSlot<ItemUnit, SingleItemData> {
        public Item(long slotLimit, Runnable onUpdate) {
            super(ItemUnit.BLANK, slotLimit, onUpdate, SingleItemData.SERIALIZER);
        }
    }

    public static class Fluid extends SimpleSlot<FluidUnit, SingleFluidData> {
        public Fluid(long slotLimit, Runnable onUpdate) {
            super(FluidUnit.BLANK, slotLimit, onUpdate, SingleFluidData.SERIALIZER);
        }
    }
}
