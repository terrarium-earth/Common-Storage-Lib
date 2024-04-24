package earth.terrarium.botarium.common.storage.impl;

import earth.terrarium.botarium.common.context.ItemContext;
import earth.terrarium.botarium.common.data.impl.ItemContainerData;
import earth.terrarium.botarium.common.data.utils.ContainerDataManagers;
import earth.terrarium.botarium.common.storage.util.ContainerExtras;
import earth.terrarium.botarium.common.storage.base.UnitContainer;
import earth.terrarium.botarium.common.storage.util.UpdateManager;
import earth.terrarium.botarium.common.transfer.impl.ItemUnit;
import io.netty.util.collection.IntObjectMap;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public class SimpleItemContainer implements UnitContainer<ItemUnit>, ContainerExtras<ItemUnit>, UpdateManager<ItemContainerData> {
    protected final NonNullList<SimpleItemSlot> slots;
    private final Runnable update;

    public SimpleItemContainer(int size, int limit) {
        this.slots = NonNullList.withSize(size, new SimpleItemSlot(limit, this::update));
        this.update = () -> {};
    }

    public SimpleItemContainer(int size, int limit, ItemStack stack, ItemContext context) {
        this.slots = NonNullList.withSize(size, new SimpleItemSlot(limit, this::update));
        this.update = () -> {
            ItemContainerData data = ItemContainerData.of(this);
            DataComponentPatch patch = DataComponentPatch.builder().set(ContainerDataManagers.ITEM_CONTENTS.componentType(), data).build();
            context.modify(patch);
            context.updateAll();
        };
        ContainerDataManagers.ITEM_CONTENTS.getData(stack).applyData(this);
    }

    public SimpleItemContainer(int size, int limit, Object entityOrBlockEntity) {
        this.slots = NonNullList.withSize(size, new SimpleItemSlot(limit, this::update));
        this.update = () -> {
            ItemContainerData data = ItemContainerData.of(this);
            ContainerDataManagers.ITEM_CONTENTS.setData(entityOrBlockEntity, data);
        };
        ContainerDataManagers.ITEM_CONTENTS.getData(entityOrBlockEntity).applyData(this);
    }

    @Override
    public void setUnitInSlot(int slot, ItemUnit unit) {
        slots.get(slot).setUnit(unit);
    }

    @Override
    public void setAmountInSlot(int slot, long amount) {
        slots.get(slot).setAmount(amount);
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
    public ItemContainerData createSnapshot() {
        return ItemContainerData.of(this);
    }

    @Override
    public void readSnapshot(ItemContainerData snapshot) {
        snapshot.applyData(this);
    }

    @Override
    public void update() {
        update.run();
    }

    public static class Filtered extends SimpleItemContainer {
        public Filtered(int size, int limit, IntObjectMap<Predicate<ItemUnit>> validator) {
            super(size, limit);
            validator.forEach((slot, predicate) -> this.slots.set(slot, new SimpleItemSlot.Filtered(limit, this::update, predicate)));
        }

        public Filtered(int size, int limit, IntObjectMap<Predicate<ItemUnit>> validator, ItemStack stack, ItemContext context) {
            super(size, limit, stack, context);
            validator.forEach((slot, predicate) -> this.slots.set(slot, new SimpleItemSlot.Filtered(limit, this::update, predicate)));
        }

        public Filtered(int size, int limit, IntObjectMap<Predicate<ItemUnit>> validator, Object entityOrBlockEntity) {
            super(size, limit, entityOrBlockEntity);
            validator.forEach((slot, predicate) -> this.slots.set(slot, new SimpleItemSlot.Filtered(limit, this::update, predicate)));
        }
    }
}
