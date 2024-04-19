package earth.terrarium.botarium.common.storage.context;

import earth.terrarium.botarium.common.context.ItemContext;
import earth.terrarium.botarium.common.storage.ConversionUtils;
import earth.terrarium.botarium.common.storage.base.UnitContainer;
import earth.terrarium.botarium.common.storage.base.UnitSlot;
import earth.terrarium.botarium.common.storage.common.CommonWrappedSlotSlot;
import earth.terrarium.botarium.common.transfer.impl.ItemUnit;
import it.unimi.dsi.fastutil.objects.Object2LongLinkedOpenHashMap;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;

public record CommonItemContext(ContainerItemContext context) implements ItemContext {
    @Override
    public long insert(ItemUnit unit, long amount, boolean simulate) {
        Object2LongLinkedOpenHashMap<ItemVariant> map = new Object2LongLinkedOpenHashMap<>();
        try (var transaction = Transaction.openOuter()) {
            long inserted = context.insert(ConversionUtils.toVariant(unit), amount, transaction);
            if (!simulate) {
                transaction.commit();
            }
            return inserted;
        }
    }

    @Override
    public long extract(ItemUnit unit, long amount, boolean simulate) {
        try (var transaction = Transaction.openOuter()) {
            long extracted = context.extract(ConversionUtils.toVariant(unit), amount, transaction);
            if (!simulate) {
                transaction.commit();
            }
            return extracted;
        }
    }

    @Override
    public long exchange(ItemUnit unit, long amount, boolean simulate) {
        try (var transaction = Transaction.openOuter()) {
            long exchanged = context.exchange(ConversionUtils.toVariant(unit), amount, transaction);
            if (!simulate) {
                transaction.commit();
            }
            return exchanged;
        }
    }

    @Override
    public UnitContainer<ItemUnit> outerContainer() {
        return new ContextItemContainer(context.getAdditionalSlots(), context::insertOverflow);
    }

    @Override
    public UnitSlot<ItemUnit> mainSlot() {
        return new CommonWrappedSlotSlot<>(context.getMainSlot(), ConversionUtils::toVariant, ConversionUtils::toUnit);
    }
}
