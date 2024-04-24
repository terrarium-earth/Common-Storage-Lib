package earth.terrarium.botarium.common.storage.impl;

import earth.terrarium.botarium.common.context.ItemContext;
import earth.terrarium.botarium.common.data.impl.FluidContainerData;
import earth.terrarium.botarium.common.data.utils.ContainerDataManagers;
import earth.terrarium.botarium.common.storage.util.ContainerExtras;
import earth.terrarium.botarium.common.storage.base.UnitContainer;
import earth.terrarium.botarium.common.storage.util.UpdateManager;
import earth.terrarium.botarium.common.transfer.impl.FluidUnit;
import io.netty.util.collection.IntObjectMap;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public class SimpleFluidContainer implements UnitContainer<FluidUnit>, ContainerExtras<FluidUnit>, UpdateManager<FluidContainerData> {
    protected final NonNullList<SimpleFluidSlot> slots;
    private final Runnable update;

    public SimpleFluidContainer(int size, int limit) {
        this.slots = NonNullList.withSize(size, new SimpleFluidSlot(limit, this::update));
        this.update = () -> {};
    }

    public SimpleFluidContainer(int size, int limit, ItemStack stack, ItemContext context) {
        this.slots = NonNullList.withSize(size, new SimpleFluidSlot(limit, this::update));
        this.update = () -> {
            FluidContainerData data = FluidContainerData.of(this);
            DataComponentPatch patch = DataComponentPatch.builder().set(ContainerDataManagers.FLUID_CONTENTS.componentType(), data).build();
            context.modify(patch);
            context.updateAll();
        };
        ContainerDataManagers.FLUID_CONTENTS.getData(stack).applyData(this);
    }

    public SimpleFluidContainer(int size, int limit, Object entityOrBlockEntity) {
        this.slots = NonNullList.withSize(size, new SimpleFluidSlot(limit, this::update));
        this.update = () -> {
            FluidContainerData data = FluidContainerData.of(this);
            ContainerDataManagers.FLUID_CONTENTS.setData(entityOrBlockEntity, data);
        };
        ContainerDataManagers.FLUID_CONTENTS.getData(entityOrBlockEntity).applyData(this);
    }

    @Override
    public void setUnitInSlot(int slot, FluidUnit unit) {
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
    public @NotNull SimpleFluidSlot getSlot(int slot) {
        return slots.get(slot);
    }

    @Override
    public FluidContainerData createSnapshot() {
        return FluidContainerData.of(this);
    }

    @Override
    public void readSnapshot(FluidContainerData snapshot) {
        snapshot.applyData(this);
    }

    @Override
    public void update() {
        update.run();
    }

    public static class Filtered extends SimpleFluidContainer {
        public Filtered(int size, int limit, IntObjectMap<Predicate<FluidUnit>> validator) {
            super(size, limit);
            validator.forEach((slot, predicate) -> this.slots.set(slot, new SimpleFluidSlot.Filtered(limit, this::update, predicate)));
        }

        public Filtered(int size, int limit, IntObjectMap<Predicate<FluidUnit>> validator, ItemStack stack, ItemContext context) {
            super(size, limit, stack, context);
            validator.forEach((slot, predicate) -> this.slots.set(slot, new SimpleFluidSlot.Filtered(limit, this::update, predicate)));
        }

        public Filtered(int size, int limit, IntObjectMap<Predicate<FluidUnit>> validator, Object entityOrBlockEntity) {
            super(size, limit, entityOrBlockEntity);
            validator.forEach((slot, predicate) -> this.slots.set(slot, new SimpleFluidSlot.Filtered(limit, this::update, predicate)));
        }
    }
}
