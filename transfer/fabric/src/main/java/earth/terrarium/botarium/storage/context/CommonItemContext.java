package earth.terrarium.botarium.storage.context;

import earth.terrarium.botarium.context.ItemContext;
import earth.terrarium.botarium.resource.item.ItemResource;
import earth.terrarium.botarium.storage.ConversionUtils;
import earth.terrarium.botarium.storage.base.CommonStorage;
import earth.terrarium.botarium.storage.base.StorageSlot;
import earth.terrarium.botarium.storage.common.CommonWrappedSlotSlot;
import it.unimi.dsi.fastutil.objects.Object2LongLinkedOpenHashMap;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;

public record CommonItemContext(ContainerItemContext context) implements ItemContext {
    @Override
    public long insert(ItemResource unit, long amount, boolean simulate) {
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
    public long extract(ItemResource unit, long amount, boolean simulate) {
        try (var transaction = Transaction.openOuter()) {
            long extracted = context.extract(ConversionUtils.toVariant(unit), amount, transaction);
            if (!simulate) {
                transaction.commit();
            }
            return extracted;
        }
    }

    @Override
    public long exchange(ItemResource newUnit, long amount, boolean simulate) {
        try (var transaction = Transaction.openOuter()) {
            long exchanged = context.exchange(ConversionUtils.toVariant(newUnit), amount, transaction);
            if (!simulate) {
                transaction.commit();
            }
            return exchanged;
        }
    }

    @Override
    public CommonStorage<ItemResource> outerContainer() {
        return new ContextItemContainer(context.getAdditionalSlots(), context::insertOverflow);
    }

    @Override
    public StorageSlot<ItemResource> mainSlot() {
        return new CommonWrappedSlotSlot<>(context.getMainSlot(), ConversionUtils::toVariant, ConversionUtils::toUnit);
    }
}
