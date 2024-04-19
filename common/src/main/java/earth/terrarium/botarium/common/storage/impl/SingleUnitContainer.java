package earth.terrarium.botarium.common.storage.impl;

import earth.terrarium.botarium.common.storage.base.UnitContainer;
import earth.terrarium.botarium.common.storage.base.UnitSlot;
import earth.terrarium.botarium.common.storage.util.UpdateManager;
import earth.terrarium.botarium.common.transfer.base.TransferUnit;
import net.minecraft.util.Tuple;
import org.jetbrains.annotations.NotNull;

public abstract class SingleUnitContainer<T extends TransferUnit<?>> implements UnitContainer<T>, UpdateManager<Tuple<T, Long>> {
    private final SimpleSlot<T> slot;

    public SingleUnitContainer(T blankValue, long limit) {
        this.slot = new SimpleSlot<>(blankValue, limit, this::update);
    }

    @Override
    public int getSlotCount() {
        return 1;
    }

    @Override
    public @NotNull UnitSlot<T> getSlot(int slot) {
        return this.slot;
    }

    @Override
    public long extract(T unit, long amount, boolean simulate) {
        return this.slot.extract(unit, amount, simulate);
    }

    @Override
    public long insert(T unit, long amount, boolean simulate) {
        return this.slot.insert(unit, amount, simulate);
    }

    @Override
    public Tuple<T, Long> createSnapshot() {
        return new Tuple<>(slot.getUnit(), slot.getAmount());
    }

    @Override
    public void readSnapshot(Tuple<T, Long> snapshot) {
        slot.setUnit(snapshot.getA());
        slot.setAmount(snapshot.getB());
    }
}
