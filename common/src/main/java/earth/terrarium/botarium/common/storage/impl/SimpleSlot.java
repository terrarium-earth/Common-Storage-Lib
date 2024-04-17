package earth.terrarium.botarium.common.storage.impl;

import com.mojang.serialization.Codec;
import earth.terrarium.botarium.common.storage.base.ContainerSlot;
import earth.terrarium.botarium.common.transfer.base.TransferUnit;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.world.item.component.CustomData;

import java.util.Optional;

public class SimpleSlot<T extends TransferUnit<?>> implements ContainerSlot<T> {
    private final Codec<T> unitCodec;
    private final T initialUnit;
    private final long slotLimit;
    private final Runnable onUpdate;
    private T unit;
    private long amount = 0;

    public SimpleSlot(T initialUnit, long slotLimit, Codec<T> unitCodec, Runnable onUpdate) {
        this.unit = initialUnit;
        this.initialUnit = initialUnit;
        this.slotLimit = slotLimit;
        this.unitCodec = unitCodec;
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
    public DataComponentPatch createSnapshot() {
        CompoundTag tag = new CompoundTag();
        unitCodec.encode(unit, NbtOps.INSTANCE, tag);
        tag.putLong("amount", 0);
        return DataComponentPatch.builder().set(DataComponents.CUSTOM_DATA, CustomData.of(tag)).build();
    }

    @Override
    public void readSnapshot(DataComponentPatch snapshot) {
        Optional<? extends CustomData> data = snapshot.get(DataComponents.CUSTOM_DATA);
        if (data != null) {
            data.ifPresent(customData -> {
                CompoundTag compound = customData.copyTag();
                this.unit = unitCodec.parse(NbtOps.INSTANCE, compound).result().orElse(this.initialUnit);
                this.amount = compound.getLong("amount");
            });
        } else {
            this.unit = this.initialUnit;
            this.amount = 0;
        }
    }

    public boolean match(T unit) {
        return this.unit.unit() == unit.unit() && this.unit.componentsMatch(unit.components());
    }

    @Override
    public void update() {
        onUpdate.run();
    }
}
