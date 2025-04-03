package earth.terrarium.common_storage_lib.storage.context;

import earth.terrarium.common_storage_lib.context.ItemContext;
import earth.terrarium.common_storage_lib.resources.item.ItemResource;
import earth.terrarium.common_storage_lib.storage.ConversionUtils;
import earth.terrarium.common_storage_lib.storage.base.CommonStorage;
import earth.terrarium.common_storage_lib.storage.base.StorageSlot;
import earth.terrarium.common_storage_lib.storage.common.CommonWrappedSlotSlot;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;

public record CommonItemContext(ContainerItemContext context) implements ItemContext {
    @Override
    public long insert(ItemResource resource, long amount, boolean simulate) {
        try (var transaction = ConversionUtils.getTransaction()) {
            long inserted = context.insert(ConversionUtils.toVariant(resource), amount, transaction);
            if (!simulate) {
                transaction.commit();
            }
            return inserted;
        }
    }

    @Override
    public long extract(ItemResource resource, long amount, boolean simulate) {
        try (var transaction = ConversionUtils.getTransaction()) {
            long extracted = context.extract(ConversionUtils.toVariant(resource), amount, transaction);
            if (!simulate) {
                transaction.commit();
            }
            return extracted;
        }
    }

    @Override
    public long exchange(ItemResource newResource, long amount, boolean simulate) {
        try (var transaction = ConversionUtils.getTransaction()) {
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
