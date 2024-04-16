package earth.terrarium.botarium.common.lookup;

import earth.terrarium.botarium.common.context.ItemContext;
import earth.terrarium.botarium.common.item.container.FabricSingleSlotContainer;
import earth.terrarium.botarium.common.storage.base.SingleSlotContainer;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.List;

public class PlatformItemContext<T> implements ContainerItemContext {
    private final List<SingleSlotStorage<ItemVariant>> container;
    private final FabricSingleSlotContainer mainSlot;
    private final ItemContext context;

    public PlatformItemContext(ItemContext context) {
        this.context = context;
        this.container = SingleSlotContainer.list(context.outerContainer()).stream().map(slotContainer -> (SingleSlotStorage<ItemVariant>) new FabricSingleSlotContainer(slotContainer)).toList();
        this.mainSlot = new FabricSingleSlotContainer(context.mainSlot());
    }

    @Override
    public SingleSlotStorage<ItemVariant> getMainSlot() {
        return mainSlot;
    }

    @Override
    public long insertOverflow(ItemVariant itemVariant, long maxAmount, TransactionContext transactionContext) {
        try (var transaction = transactionContext.openNested()) {
            return context.outerContainer().insert(itemVariant.toStack((int) maxAmount), false);
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    public @UnmodifiableView List<SingleSlotStorage<ItemVariant>> getAdditionalSlots() {
        return container;
    }
}
