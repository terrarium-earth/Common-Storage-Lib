package earth.terrarium.botarium.common.item.impl.noops;

import earth.terrarium.botarium.common.storage.base.UnitContainer;
import earth.terrarium.botarium.common.storage.base.UnitSlot;
import earth.terrarium.botarium.common.storage.util.NoUpdate;
import earth.terrarium.botarium.common.transfer.impl.ItemUnit;
import org.jetbrains.annotations.NotNull;

public final class NoOpsItemContainer implements UnitContainer<ItemUnit>, NoUpdate {
    public static final NoOpsItemContainer NO_OPS = new NoOpsItemContainer();

    @Override
    public int getSlotCount() {
        return 0;
    }

    @Override
    public @NotNull UnitSlot<ItemUnit> getSlot(int slot) {
        return NoOpsItemSlot.NO_OPS;
    }

    @Override
    public long insert(ItemUnit unit, long amount, boolean simulate) {
        return 0;
    }

    @Override
    public long extract(ItemUnit unit, long amount, boolean simulate) {
        return 0;
    }

    @Override
    public boolean allowsInsertion() {
        return false;
    }

    @Override
    public boolean allowsExtraction() {
        return false;
    }
}