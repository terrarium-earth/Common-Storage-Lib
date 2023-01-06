package earth.terrarium.botarium.fabric.fluid.holder;

import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.StoragePreconditions;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleVariantStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.world.item.ItemStack;

import java.util.Collections;
import java.util.List;

@SuppressWarnings("UnstableApiUsage")
public class PlatformContainerItemContext implements ContainerItemContext {
    protected final ItemStack stack;

    private final SingleVariantStorage<ItemVariant> backingSlot = new SingleVariantStorage<>() {
        @Override
        protected ItemVariant getBlankVariant() {
            return ItemVariant.blank();
        }

        @Override
        protected long getCapacity(ItemVariant variant) {
            return stack.getMaxStackSize();
        }

        @Override
        public ItemVariant getResource() {
            return new MutableItemVariant(stack);
        }

        @Override
        public long getAmount() {
            return stack.getCount();
        }
    };

    public PlatformContainerItemContext(ItemStack stack) {
        this.stack = stack;
    }

    @Override
    public SingleSlotStorage<ItemVariant> getMainSlot() {
        return backingSlot;
    }

    @Override
    public long insertOverflow(ItemVariant itemVariant, long maxAmount, TransactionContext transactionContext) {
        StoragePreconditions.notBlankNotNegative(itemVariant, maxAmount);
        return maxAmount;
    }

    @Override
    public List<SingleSlotStorage<ItemVariant>> getAdditionalSlots() {
        return Collections.emptyList();
    }
}
