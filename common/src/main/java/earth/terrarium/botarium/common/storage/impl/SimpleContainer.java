package earth.terrarium.botarium.common.storage.impl;

import earth.terrarium.botarium.common.storage.base.UnitSlot;
import earth.terrarium.botarium.common.storage.base.UnitContainer;
import earth.terrarium.botarium.common.storage.util.UpdateManager;
import earth.terrarium.botarium.common.transfer.base.TransferUnit;
import net.minecraft.core.NonNullList;
import net.minecraft.util.Tuple;
import org.jetbrains.annotations.NotNull;

public abstract class SimpleContainer<T extends TransferUnit<?>> implements UnitContainer<T>, UpdateManager<NonNullList<Tuple<T, Long>>> {
    private final NonNullList<SimpleSlot<T>> slots;
    private final T blankUnit;
    private final Runnable onUpdate;

    public SimpleContainer(int size, long slotLimit, T blankUnit, Runnable onUpdate) {
        this.slots = NonNullList.withSize(size, new SimpleSlot<>(blankUnit, slotLimit, this::update));
        this.blankUnit = blankUnit;
        this.onUpdate = onUpdate;
    }

    @Override
    public int getSlotCount() {
        return slots.size();
    }

    @Override
    public @NotNull UnitSlot<T> getSlot(int slot) {
        return this.slots.get(slot);
    }

    @Override
    public NonNullList<Tuple<T, Long>> createSnapshot() {
        NonNullList<Tuple<T, Long>> snapshot = NonNullList.withSize(slots.size(), new Tuple<>(blankUnit, 0L));
        for (int i = 0; i < slots.size(); i++) {
            SimpleSlot<T> slot = slots.get(i);
            snapshot.set(i, new Tuple<>(slot.getUnit(), slot.getAmount()));
        }
        return snapshot;
    }

    @Override
    public void readSnapshot(NonNullList<Tuple<T, Long>> snapshot) {
        for (int i = 0; i < slots.size(); i++) {
            SimpleSlot<T> slot = slots.get(i);
            Tuple<T, Long> data = snapshot.get(i);
            slot.setUnit(data.getA());
            slot.setAmount(data.getB());
        }
    }

    @Override
    public void update() {
        onUpdate.run();
    }
}
