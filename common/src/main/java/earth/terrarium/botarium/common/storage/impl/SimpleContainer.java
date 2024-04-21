package earth.terrarium.botarium.common.storage.impl;

import earth.terrarium.botarium.common.context.ItemContext;
import earth.terrarium.botarium.common.data.DataManager;
import earth.terrarium.botarium.common.data.impl.FluidContainerData;
import earth.terrarium.botarium.common.data.impl.ItemContainerData;
import earth.terrarium.botarium.common.data.impl.SingleFluidData;
import earth.terrarium.botarium.common.data.impl.SingleItemData;
import earth.terrarium.botarium.common.data.utils.ContainerDataManagers;
import earth.terrarium.botarium.common.data.utils.ContainerSerializer;
import earth.terrarium.botarium.common.storage.base.UnitSlot;
import earth.terrarium.botarium.common.storage.base.UnitContainer;
import earth.terrarium.botarium.common.storage.util.UpdateManager;
import earth.terrarium.botarium.common.transfer.base.TransferUnit;
import earth.terrarium.botarium.common.transfer.impl.FluidUnit;
import earth.terrarium.botarium.common.transfer.impl.ItemUnit;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class SimpleContainer<T extends TransferUnit<?>, D, S> implements UnitContainer<T>, UpdateManager<D> {
    private final ContainerSerializer<SimpleContainer<T, ?, ?>, D> serializer;
    private final NonNullList<SimpleSlot<T, S>> slots;
    private final Consumer<SimpleContainer<T, D, S>> onUpdate;

    public SimpleContainer(int size, long slotLimit, T blankUnit, ContainerSerializer<SimpleContainer<T, ?, ?>, D> serializer, ContainerSerializer<SimpleSlot<T, ?>, S> slotSerializer, Consumer<SimpleContainer<T, D, S>> onUpdate) {
        this.slots = NonNullList.withSize(size, new SimpleSlot<>(blankUnit, slotLimit, this::update, slotSerializer));
        this.onUpdate = onUpdate;
        this.serializer = serializer;
    }

    public SimpleContainer(int size, long slotLimit, T blankUnit, ContainerSerializer<SimpleContainer<T, ?, ?>, D> serializer, ContainerSerializer<SimpleSlot<T, ?>, S> slotSerializer, DataComponentType<D> type, ItemStack stack, ItemContext context) {
        this(size, slotLimit, blankUnit, serializer, slotSerializer, container -> {
            D data = serializer.captureData(container);
            DataComponentPatch update = DataComponentPatch.builder().set(type, data).build();
            context.modify(update);
            context.updateAll();
        });
        if (stack.has(type)) {
            D data = stack.get(type);
            this.readSnapshot(data);
        }
    }

    public SimpleContainer(int size, long slotLimit, T blankUnit, ContainerSerializer<SimpleContainer<T, ?, ?>, D> serializer, ContainerSerializer<SimpleSlot<T, ?>, S> slotSerializer, DataManager<D> dataManager, Object entityOrBlockEntity) {
        this(size, slotLimit, blankUnit, serializer, slotSerializer, container -> {
            D data = serializer.captureData(container);
            dataManager.setData(entityOrBlockEntity, data);
        });
        D data = dataManager.getData(entityOrBlockEntity);
        this.readSnapshot(data);
    }

    @Override
    public int getSlotCount() {
        return slots.size();
    }

    @Override
    public @NotNull UnitSlot<T> getSlot(int slot) {
        return this.slots.get(slot);
    }

    public NonNullList<SimpleSlot<T, S>> getSlots() {
        return slots;
    }

    @Override
    public D createSnapshot() {
        return serializer.captureData(this);
    }

    @Override
    public void readSnapshot(D snapshot) {
        serializer.applyData(this, snapshot);
    }

    @Override
    public void update() {
        onUpdate.accept(this);
    }

    public static class Item extends SimpleContainer<ItemUnit, ItemContainerData, SingleItemData> {
        public Item(int size, long slotLimit, Consumer<SimpleContainer<ItemUnit, ItemContainerData, SingleItemData>> onUpdate) {
            super(size, slotLimit, ItemUnit.BLANK, ItemContainerData.SERIALIZER, SingleItemData.SERIALIZER, onUpdate);
        }

        public Item(int size, long slotLimit, ItemStack stack, ItemContext context) {
            super(size, slotLimit, ItemUnit.BLANK, ItemContainerData.SERIALIZER, SingleItemData.SERIALIZER, ContainerDataManagers.ITEM_CONTENTS.componentType(), stack, context);
        }

        public Item(int size, long slotLimit, Object entityOrBlockEntity) {
            super(size, slotLimit, ItemUnit.BLANK, ItemContainerData.SERIALIZER, SingleItemData.SERIALIZER, ContainerDataManagers.ITEM_CONTENTS, entityOrBlockEntity);
        }
    }

    public static class Fluid extends SimpleContainer<FluidUnit, FluidContainerData, SingleFluidData> {
        public Fluid(int size, long slotLimit, Consumer<SimpleContainer<FluidUnit, FluidContainerData, SingleFluidData>> onUpdate) {
            super(size, slotLimit, FluidUnit.BLANK, FluidContainerData.SERIALIZER, SingleFluidData.SERIALIZER, onUpdate);
        }

        public Fluid(int size, long slotLimit, ItemStack stack, ItemContext context) {
            super(size, slotLimit, FluidUnit.BLANK, FluidContainerData.SERIALIZER, SingleFluidData.SERIALIZER, ContainerDataManagers.FLUID_CONTENTS.componentType(), stack, context);
        }

        public Fluid(int size, long slotLimit, Object entityOrBlockEntity) {
            super(size, slotLimit, FluidUnit.BLANK, FluidContainerData.SERIALIZER, SingleFluidData.SERIALIZER, ContainerDataManagers.FLUID_CONTENTS, entityOrBlockEntity);
        }
    }
}
