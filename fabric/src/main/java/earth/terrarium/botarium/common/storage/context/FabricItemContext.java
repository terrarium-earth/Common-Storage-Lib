package earth.terrarium.botarium.common.storage.context;

import earth.terrarium.botarium.common.context.ItemContext;
import earth.terrarium.botarium.common.storage.ConversionUtils;
import earth.terrarium.botarium.common.storage.fabric.FabricWrappedContainer;
import earth.terrarium.botarium.common.storage.fabric.FabricWrappedSlot;
import earth.terrarium.botarium.common.storage.util.UpdateManager;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.SlottedStorage;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.ArrayList;
import java.util.List;

public final class FabricItemContext implements ContainerItemContext {
    private final SingleSlotStorage<ItemVariant> mainSlot;
    private final SlottedStorage<ItemVariant> container;

    public FabricItemContext(ItemContext context) {
        this.mainSlot = new FabricWrappedSlot<>(context.mainSlot(), ConversionUtils::toVariant, ConversionUtils::toUnit);
        this.container = new FabricWrappedContainer<>(context.outerContainer(), ConversionUtils::toVariant, ConversionUtils::toUnit);
    }

    @Override
    public SingleSlotStorage<ItemVariant> getMainSlot() {
        return mainSlot;
    }

    @Override
    public long insertOverflow(ItemVariant itemVariant, long maxAmount, TransactionContext transactionContext) {
        return container.insert(itemVariant, maxAmount, transactionContext);
    }

    @Override
    public @UnmodifiableView List<SingleSlotStorage<ItemVariant>> getAdditionalSlots() {
        return container.getSlots();
    }
}
