package earth.terrarium.botarium.item.impl;

import earth.terrarium.botarium.Botarium;
import earth.terrarium.botarium.context.ItemContext;
import earth.terrarium.botarium.resources.item.ItemResource;
import earth.terrarium.botarium.item.util.ItemStorageData;
import earth.terrarium.botarium.storage.base.CommonStorage;
import earth.terrarium.botarium.storage.base.UpdateManager;
import earth.terrarium.botarium.storage.util.TransferUtil;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public class SimpleItemStorage implements CommonStorage<ItemResource>, UpdateManager<ItemStorageData> {
    protected final NonNullList<SimpleItemSlot> slots;
    private final Runnable onUpdate;

    public SimpleItemStorage(int size) {
        this.slots = NonNullList.withSize(size, new SimpleItemSlot(this::update));
        this.onUpdate = () -> {};
    }

    public SimpleItemStorage(int size, ItemStack stack, ItemContext context) {
        this.slots = NonNullList.withSize(size, new SimpleItemSlot(this::update));
        this.onUpdate = () -> {
            ItemStorageData data = ItemStorageData.of(this);
            DataComponentPatch patch = DataComponentPatch.builder().set(Botarium.ITEM_CONTENTS.componentType(), data).build();
            context.modify(patch);
        };
        this.readSnapshot(Botarium.ITEM_CONTENTS.get(stack));
    }

    public SimpleItemStorage(int size, Object entityOrBlockEntity) {
        this.slots = NonNullList.withSize(size, new SimpleItemSlot(this::update));
        this.onUpdate = () -> {
            ItemStorageData data = ItemStorageData.of(this);
            Botarium.ITEM_CONTENTS.set(entityOrBlockEntity, data);
        };
        this.readSnapshot(Botarium.ITEM_CONTENTS.get(entityOrBlockEntity));
    }

    public SimpleItemStorage filter(int slot, Predicate<ItemResource> predicate) {
        slots.set(slot, new SimpleItemSlot.Filtered(this::update, predicate));
        return this;
    }

    @Override
    public int size() {
        return slots.size();
    }

    @Override
    public @NotNull SimpleItemSlot getSlot(int slot) {
        return slots.get(slot);
    }

    @Override
    public ItemStorageData createSnapshot() {
        return ItemStorageData.of(this);
    }

    @Override
    public void readSnapshot(ItemStorageData snapshot) {
        for (int i = 0; i < slots.size() && i < snapshot.stacks().size(); i++) {
            slots.get(i).readSnapshot(snapshot.stacks().get(i));
        }
    }

    @Override
    public void update() {
        onUpdate.run();
    }

    @Override
    public long insert(ItemResource resource, long amount, boolean simulate) {
        return TransferUtil.insertSlots(this, resource, amount, simulate);
    }

    @Override
    public long extract(ItemResource resource, long amount, boolean simulate) {
        return TransferUtil.extractSlots(this, resource, amount, simulate);
    }

    public static class ExtractOnly extends SimpleItemStorage {
        public ExtractOnly(int size) {
            super(size);
        }

        public ExtractOnly(int size, ItemStack stack, ItemContext context) {
            super(size, stack, context);
        }

        public ExtractOnly(int size, Object entityOrBlockEntity) {
            super(size, entityOrBlockEntity);
        }

        @Override
        public long insert(ItemResource resource, long amount, boolean simulate) {
            return 0;
        }

        public long internalInsert(ItemResource resource, long amount, boolean simulate) {
            return super.insert(resource, amount, simulate);
        }

        @Override
        public ExtractOnly filter(int slot, Predicate<ItemResource> predicate) {
            return (ExtractOnly) super.filter(slot, predicate);
        }

        @Override
        public boolean allowsInsertion() {
            return false;
        }
    }

    public static class InsertOnly extends SimpleItemStorage {
        public InsertOnly(int size) {
            super(size);
        }

        public InsertOnly(int size, ItemStack stack, ItemContext context) {
            super(size, stack, context);
        }

        public InsertOnly(int size, Object entityOrBlockEntity) {
            super(size, entityOrBlockEntity);
        }

        @Override
        public long extract(ItemResource resource, long amount, boolean simulate) {
            return 0;
        }

        public long internalExtract(ItemResource resource, long amount, boolean simulate) {
            return super.extract(resource, amount, simulate);
        }

        @Override
        public InsertOnly filter(int slot, Predicate<ItemResource> predicate) {
            return (InsertOnly) super.filter(slot, predicate);
        }

        @Override
        public boolean allowsExtraction() {
            return false;
        }
    }
}
