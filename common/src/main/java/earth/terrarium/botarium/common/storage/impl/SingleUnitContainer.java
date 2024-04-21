package earth.terrarium.botarium.common.storage.impl;

import earth.terrarium.botarium.common.context.ItemContext;
import earth.terrarium.botarium.common.data.DataManager;
import earth.terrarium.botarium.common.data.impl.SingleFluidData;
import earth.terrarium.botarium.common.data.impl.SingleItemData;
import earth.terrarium.botarium.common.data.utils.ContainerDataManagers;
import earth.terrarium.botarium.common.data.utils.ContainerSerializer;
import earth.terrarium.botarium.common.storage.base.UnitContainer;
import earth.terrarium.botarium.common.storage.base.UnitSlot;
import earth.terrarium.botarium.common.storage.util.UpdateManager;
import earth.terrarium.botarium.common.transfer.base.TransferUnit;
import earth.terrarium.botarium.common.transfer.impl.FluidUnit;
import earth.terrarium.botarium.common.transfer.impl.ItemUnit;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public abstract class SingleUnitContainer<T extends TransferUnit<?>, D> extends SimpleSlot<T, D> implements UnitContainer<T>, UpdateManager<D> {
    private final Consumer<SingleUnitContainer<T, D>> onUpdate;

    public SingleUnitContainer(T initialUnit, long slotLimit, ContainerSerializer<SimpleSlot<T, ?>, D> serializer, Consumer<SingleUnitContainer<T, D>> onUpdate) {
        super(initialUnit, slotLimit, () -> {}, serializer);
        this.onUpdate = onUpdate;
    }

    public SingleUnitContainer(T initialUnit, long slotLimit, ContainerSerializer<SimpleSlot<T, ?>, D> serializer, DataComponentType<D> type, ItemStack stack, ItemContext context) {
        this(initialUnit, slotLimit, serializer, container -> {
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

    public SingleUnitContainer(T initialUnit, long slotLimit, ContainerSerializer<SimpleSlot<T, ?>, D> serializer, DataManager<D> dataManager, Object entityOrBlockEntity) {
        this(initialUnit, slotLimit, serializer, container -> {
            D data = serializer.captureData(container);
            dataManager.setData(entityOrBlockEntity, data);
        });
        D data = dataManager.getData(entityOrBlockEntity);
        this.readSnapshot(data);
    }

    @Override
    public int getSlotCount() {
        return 1;
    }

    @Override
    public @NotNull UnitSlot<T> getSlot(int slot) {
        return this;
    }

    @Override
    public void update() {
        this.onUpdate.accept(this);
    }

    public static class Item extends SingleUnitContainer<ItemUnit, SingleItemData> {
        public Item(long slotLimit, Consumer<SingleUnitContainer<ItemUnit, SingleItemData>> onUpdate) {
            super(ItemUnit.BLANK, slotLimit, SingleItemData.SERIALIZER, onUpdate);
        }

        public Item(long slotLimit, ItemStack stack, ItemContext context) {
            super(ItemUnit.BLANK, slotLimit, SingleItemData.SERIALIZER, ContainerDataManagers.SINGLE_ITEM_CONTENTS.componentType(), stack, context);
        }

        public Item(long slotLimit, Object entityOrBlockEntity) {
            super(ItemUnit.BLANK, slotLimit, SingleItemData.SERIALIZER, ContainerDataManagers.SINGLE_ITEM_CONTENTS, entityOrBlockEntity);
        }
    }

    public static class Fluid extends SingleUnitContainer<FluidUnit, SingleFluidData> {
        public Fluid(long slotLimit, Consumer<SingleUnitContainer<FluidUnit, SingleFluidData>> onUpdate) {
            super(FluidUnit.BLANK, slotLimit, SingleFluidData.SERIALIZER, onUpdate);
        }

        public Fluid(long slotLimit, ItemStack stack, ItemContext context) {
            super(FluidUnit.BLANK, slotLimit, SingleFluidData.SERIALIZER, ContainerDataManagers.SINGLE_FLUID_CONTENTS.componentType(), stack, context);
        }

        public Fluid(long slotLimit, Object entityOrBlockEntity) {
            super(FluidUnit.BLANK, slotLimit, SingleFluidData.SERIALIZER, ContainerDataManagers.SINGLE_FLUID_CONTENTS, entityOrBlockEntity);
        }
    }
}
