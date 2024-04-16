package earth.terrarium.botarium.common.lookup;

import earth.terrarium.botarium.common.context.ItemContext;
import earth.terrarium.botarium.common.storage.base.SingleSlotContainer;
import earth.terrarium.botarium.common.storage.base.SlottedContainer;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class CommonItemContext implements ItemContext {
    private final ContainerItemContext context;
    private final OuterContainer outerContainer = new OuterContainer();
    private final MainSlot mainSlot = new MainSlot();

    public CommonItemContext(ContainerItemContext context) {
        this.context = context;
    }

    @Override
    public SlottedContainer<ItemStack> outerContainer() {
        return outerContainer;
    }

    @Override
    public SingleSlotContainer<ItemStack> mainSlot() {
        return mainSlot;
    }

    public class OuterContainer implements SlottedContainer<ItemStack> {
        @Override
        public int getSlotCount() {
            return context.getAdditionalSlots().size();
        }

        @Override
        public @NotNull ItemStack getValueInSlot(int slot) {
            SingleSlotStorage<ItemVariant> view = context.getAdditionalSlots().get(slot);
            return view.getResource().toStack((int) view.getAmount());
        }

        @Override
        public int getSlotLimit(int slot) {
            return (int) context.getAdditionalSlots().get(slot).getCapacity();
        }

        @Override
        public boolean isValueValid(int slot, @NotNull ItemStack value) {
            return true;
        }

        @Override
        public long insertIntoSlot(int slot, @NotNull ItemStack value, boolean simulate) {
            if (slot < getSlotCount()) {
                SingleSlotStorage<ItemVariant> storage = context.getAdditionalSlots().get(slot);
                try (var transaction = Transaction.openOuter()) {
                    long inserted = storage.insert(ItemVariant.of(value), value.getCount(), transaction);
                    if (!simulate) transaction.commit();
                    return inserted;
                }
            }
            return 0;
        }

        @Override
        public @NotNull ItemStack extractFromSlot(int slot, long amount, boolean simulate) {
            if (slot < getSlotCount()) {
                SingleSlotStorage<ItemVariant> storage = context.getAdditionalSlots().get(slot);
                long extracted;
                try (var transaction = Transaction.openOuter()) {
                    extracted = storage.extract(storage.getResource(), amount, transaction);
                    if (!simulate) transaction.commit();
                }
                return storage.getResource().toStack((int) extracted);
            }
            return ItemStack.EMPTY;
        }

        @Override
        public long insert(ItemStack value, boolean simulate) {
            try (var transaction = Transaction.openOuter()) {
                long inserted = context.insert(ItemVariant.of(value), value.getCount(), transaction);
                if (!simulate) transaction.commit();
                return inserted;
            }
        }

        @Override
        public ItemStack extract(long amount, boolean simulate) {
            long extracted = 0;
            try (var transaction = Transaction.openOuter()) {
                for (SingleSlotStorage<ItemVariant> view : context.getAdditionalSlots()) {
                    if (view.isResourceBlank() || view.getAmount() == 0) continue;
                    extracted += view.extract(view.getResource(), amount - extracted, transaction);
                    if (extracted >= amount) break;
                }
                if (!simulate) transaction.commit();
            }
            return ItemVariant.of(ItemStack.EMPTY).toStack((int) extracted);
        }
    }

    public class MainSlot implements SingleSlotContainer<ItemStack> {
        @Override
        public @NotNull ItemStack getValue() {
            return context.getItemVariant().toStack((int) context.getAmount());
        }

        @Override
        public int getLimit() {
            return (int) context.getMainSlot().getCapacity();
        }

        @Override
        public boolean isValueValid(@NotNull ItemStack value) {
            return true;
        }

        @Override
        public long insert(@NotNull ItemStack value, boolean simulate) {
            long inserted = 0;
            try (var transaction = Transaction.openOuter()) {
                inserted = context.getMainSlot().insert(ItemVariant.of(value), value.getCount(), transaction);
                if (!simulate) transaction.commit();
            }
            return inserted;
        }

        @Override
        public @NotNull ItemStack extract(long amount, boolean simulate) {
            long extracted;
            ItemVariant itemVariant;
            try (var transaction = Transaction.openOuter()) {
                extracted = context.getMainSlot().extract(ItemVariant.of(ItemStack.EMPTY), amount, transaction);
                itemVariant = context.getMainSlot().getResource();
                if (!simulate) transaction.commit();
            }
            return itemVariant.toStack((int) extracted);
        }
    }
}
