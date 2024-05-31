package earth.terrarium.common_storage_lib.storage.context;

import earth.terrarium.common_storage_lib.context.ItemContext;
import earth.terrarium.common_storage_lib.resources.item.ItemResource;
import earth.terrarium.common_storage_lib.storage.ConversionUtils;
import earth.terrarium.common_storage_lib.storage.base.CommonStorage;
import earth.terrarium.common_storage_lib.storage.base.StorageSlot;
import earth.terrarium.common_storage_lib.storage.common.CommonWrappedSlotSlot;
import it.unimi.dsi.fastutil.objects.Object2LongLinkedOpenHashMap;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;

public record CommonItemContext(ContainerItemContext context) implements ItemContext {
    @Override
    public long insert(ItemResource resource, long amount, boolean simulate) {
        Object2LongLinkedOpenHashMap<ItemVariant> map = new Object2LongLinkedOpenHashMap<>();
        try (var transaction = Transaction.openOuter()) {
            long inserted = context.insert(ConversionUtils.toVariant(resource), amount, transaction);
            if (!simulate) {
                transaction.commit();
            }
            return inserted;
        }
    }

    @Override
    public long extract(ItemResource resource, long amount, boolean simulate) {
        try (var transaction = Transaction.openOuter()) {
            long extracted = context.extract(ConversionUtils.toVariant(resource), amount, transaction);
            if (!simulate) {
                transaction.commit();
            }
            return extracted;
        }
    }

    @Override
    public long exchange(ItemResource newResource, long amount, boolean simulate) {
        try (var transaction = Transaction.openOuter()) {
            long exchanged = context.exchange(ConversionUtils.toVariant(newResource), amount, transaction);
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
        return new CommonWrappedSlotSlot<>(context.getMainSlot(), ConversionUtils::toVariant, ConversionUtils::toResource);
    }
}
