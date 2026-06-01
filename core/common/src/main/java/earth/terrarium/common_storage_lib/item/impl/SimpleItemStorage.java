package earth.terrarium.common_storage_lib.item.impl;

import earth.terrarium.common_storage_lib.context.ItemContext;
import earth.terrarium.common_storage_lib.data.DataManager;
import earth.terrarium.common_storage_lib.fluid.impl.SimpleFluidSlot;
import earth.terrarium.common_storage_lib.resources.item.ItemResource;
import earth.terrarium.common_storage_lib.item.util.ItemStorageData;
import earth.terrarium.common_storage_lib.storage.base.CommonStorage;
import earth.terrarium.common_storage_lib.storage.base.UpdateManager;
import earth.terrarium.common_storage_lib.storage.util.TransferUtil;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponentType;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public class SimpleItemStorage implements CommonStorage<ItemResource>, UpdateManager<ItemStorageData> {
    protected final NonNullList<SimpleItemSlot> slots;
    private final Runnable onUpdate;
    private final Runnable save;

    public SimpleItemStorage(int size) {
        this.slots = NonNullList.withSize(size, new SimpleItemSlot(this::update));
        this.onUpdate = () -> {};
        this.save = () -> {};
    }

    public SimpleItemStorage(ItemContext context, DataComponentType<ItemStorageData> componentType, int size) {
        this.onUpdate = context::updateAll;
        this.save = () -> context.set(componentType, ItemStorageData.of(this));
        this.slots = NonNullList.withSize(size, new SimpleItemSlot(this::update, this.save));
        if (context.getResource().has(componentType)) {
            this.readSnapshot(context.getResource().get(componentType));
        }
    }

    public SimpleItemStorage(Object entityOrBlockEntity, DataManager<ItemStorageData> dataManager, int size) {
        this.slots = NonNullList.withSize(size, new SimpleItemSlot(this::update));
        this.onUpdate = () -> dataManager.set(entityOrBlockEntity, ItemStorageData.of(this));
        this.save = () -> {};
        this.readSnapshot(dataManager.get(entityOrBlockEntity));
    }

    public SimpleItemStorage filter(int slot, Predicate<ItemResource> predicate) {
        SimpleItemSlot oldSlot = slots.get(slot);
        SimpleItemSlot newSlot = new SimpleItemSlot.Filtered(this::update, this.save, predicate);
        newSlot.readSnapshot(oldSlot.createSnapshot());
        slots.set(slot, newSlot);
        return this;
    }

    @Override
    public int size() {
        return slots.size();
    }

    @Override
    public @NotNull SimpleItemSlot get(int index) {
        return slots.get(index);
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
}
