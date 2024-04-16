package earth.terrarium.botarium.common.item.container;

import earth.terrarium.botarium.common.storage.base.SingleSlotContainer;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.world.item.ItemStack;

public record FabricSingleSlotContainer(SingleSlotContainer<ItemStack> slotContainer) implements SingleSlotStorage<ItemVariant> {
    @Override
    public long insert(ItemVariant resource, long maxAmount, TransactionContext transaction) {
        return slotContainer.insert(resource.toStack((int) maxAmount), false);
    }

    @Override
    public long extract(ItemVariant resource, long maxAmount, TransactionContext transaction) {
        return resource.matches(slotContainer.getValue()) ? slotContainer.extract(maxAmount, false).getCount() : 0;
    }

    @Override
    public boolean isResourceBlank() {
        return slotContainer.getValue().isEmpty();
    }

    @Override
    public ItemVariant getResource() {
        return ItemVariant.of(slotContainer.getValue());
    }

    @Override
    public long getAmount() {
        return slotContainer.getValue().getCount();
    }

    @Override
    public long getCapacity() {
        return slotContainer.getLimit();
    }
}
