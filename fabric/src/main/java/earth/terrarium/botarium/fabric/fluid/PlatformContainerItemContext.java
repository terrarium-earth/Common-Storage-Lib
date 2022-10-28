package earth.terrarium.botarium.fabric.fluid;

import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;

import java.util.List;

public class PlatformContainerItemContext implements ContainerItemContext {
    @Override
    public SingleSlotStorage<ItemVariant> getMainSlot() {

    }

    @Override
    public long insertOverflow(ItemVariant itemVariant, long maxAmount, TransactionContext transactionContext) {
        return 0;
    }

    @Override
    public List<SingleSlotStorage<ItemVariant>> getAdditionalSlots() {
        return null;
    }
}
