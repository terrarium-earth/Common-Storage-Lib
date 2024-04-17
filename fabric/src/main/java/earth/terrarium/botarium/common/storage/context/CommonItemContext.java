package earth.terrarium.botarium.common.storage.context;

import earth.terrarium.botarium.common.context.ItemContext;
import earth.terrarium.botarium.common.item.base.ItemContainer;
import earth.terrarium.botarium.common.storage.base.ContainerSlot;
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
            long inserted = context.insert(toVariant(unit), amount, transaction);
            if (!simulate) {
                transaction.commit();
            }
            return inserted;
        }
    }

    @Override
    public long extract(ItemUnit unit, long amount, boolean simulate) {
        try (var transaction = Transaction.openOuter()) {
            long extracted = context.extract(toVariant(unit), amount, transaction);
            if (!simulate) {
                transaction.commit();
            }
            return extracted;
        }
    }

    @Override
    public long exchange(ItemUnit unit, long amount, boolean simulate) {
        try (var transaction = Transaction.openOuter()) {
            long exchanged = context.exchange(toVariant(unit), amount, transaction);
            if (!simulate) {
                transaction.commit();
            }
            return exchanged;
        }
    }

    @Override
    public long insertIndiscriminately(ItemUnit unit, long amount, boolean simulate) {
        try (var transaction = Transaction.openOuter()) {
            long inserted = context.insertOverflow(toVariant(unit), amount, transaction);
            if (!simulate) {
                transaction.commit();
            }
            return inserted;
        }
    }

    @Override
    public ItemContainer outerContainer() {
        return new ContextItemContainer(context.getAdditionalSlots());
    }

    @Override
    public ContainerSlot<ItemUnit> mainSlot() {
        return new CommonWrappedSlotSlot<>(context.getMainSlot(), CommonItemContext::toVariant, CommonItemContext::toUnit);
    }

    public static ItemUnit toUnit(ItemVariant variant) {
        return ItemUnit.of(variant.getItem(), variant.getComponents());
    }

    public static ItemVariant toVariant(ItemUnit unit) {
        return ItemVariant.of(unit.unit(), unit.components());
    }
}
