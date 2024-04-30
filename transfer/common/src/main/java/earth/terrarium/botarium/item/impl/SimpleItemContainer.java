package earth.terrarium.botarium.item.impl;

import earth.terrarium.botarium.BotariumTransfer;
import earth.terrarium.botarium.context.ItemContext;
import earth.terrarium.botarium.item.base.ItemUnit;
import earth.terrarium.botarium.item.util.ItemStorageData;
import earth.terrarium.botarium.storage.base.CommonStorage;
import earth.terrarium.botarium.storage.base.UpdateManager;
import earth.terrarium.botarium.storage.unit.UnitStack;
import earth.terrarium.botarium.storage.util.TransferUtil;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public class SimpleItemContainer implements CommonStorage<ItemUnit>, UpdateManager<ItemStorageData> {
    protected final NonNullList<SimpleItemSlot> slots;
    private final Runnable onUpdate;

    public SimpleItemContainer(int size) {
        this.slots = NonNullList.withSize(size, new SimpleItemSlot(this::update));
        this.onUpdate = () -> {};
    }

    public SimpleItemContainer(int size, ItemStack stack, ItemContext context) {
        this.slots = NonNullList.withSize(size, new SimpleItemSlot(this::update));
        this.onUpdate = () -> {
            ItemStorageData data = ItemStorageData.of(this);
            DataComponentPatch patch = DataComponentPatch.builder().set(BotariumTransfer.ITEM_CONTENTS.componentType(), data).build();
            context.modify(patch);
        };
        this.readSnapshot(BotariumTransfer.ITEM_CONTENTS.get(stack));
    }

    public SimpleItemContainer(int size, Object entityOrBlockEntity) {
        this.slots = NonNullList.withSize(size, new SimpleItemSlot(this::update));
        this.onUpdate = () -> {
            ItemStorageData data = ItemStorageData.of(this);
            BotariumTransfer.ITEM_CONTENTS.set(entityOrBlockEntity, data);
        };
        this.readSnapshot(BotariumTransfer.ITEM_CONTENTS.get(entityOrBlockEntity));
    }

    public SimpleItemContainer filter(int slot, Predicate<ItemUnit> predicate) {
        slots.set(slot, new SimpleItemSlot.Filtered(this::update, predicate));
        return this;
    }

    @Override
    public int getSlotCount() {
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
        for (int i = 0; i < slots.size(); i++) {
            SimpleItemSlot slot = slots.get(i);
            UnitStack<ItemUnit> data = snapshot.stacks().get(i);
            slot.readSnapshot(data);
        }
    }

    @Override
    public void update() {
        onUpdate.run();
    }

    @Override
    public long insert(ItemUnit unit, long amount, boolean simulate) {
        return TransferUtil.insertSlots(this, unit, amount, simulate);
    }

    @Override
    public long extract(ItemUnit unit, long amount, boolean simulate) {
        return TransferUtil.extractSlots(this, unit, amount, simulate);
    }
}
