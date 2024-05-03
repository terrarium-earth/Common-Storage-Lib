package earth.terrarium.botarium.fluid.impl;

import earth.terrarium.botarium.BotariumStorage;
import earth.terrarium.botarium.context.ItemContext;
import earth.terrarium.botarium.fluid.util.FluidStorageData;
import earth.terrarium.botarium.resources.fluid.FluidResource;
import earth.terrarium.botarium.storage.base.CommonStorage;
import earth.terrarium.botarium.storage.base.UpdateManager;
import earth.terrarium.botarium.storage.util.TransferUtil;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public class SimpleFluidStorage implements CommonStorage<FluidResource>, UpdateManager<FluidStorageData> {
    protected final NonNullList<SimpleFluidSlot> slots;
    private final Runnable update;
    private final long limit;

    public SimpleFluidStorage(int size, long limit) {
        this.slots = NonNullList.withSize(size, new SimpleFluidSlot(limit, this::update));
        this.limit = limit;
        this.update = () -> {};
    }

    public SimpleFluidStorage(int size, long limit, ItemStack stack, ItemContext context) {
        this.slots = NonNullList.withSize(size, new SimpleFluidSlot(limit, this::update));
        this.limit = limit;
        this.update = () -> {
            FluidStorageData data = FluidStorageData.from(this);
            DataComponentPatch patch = DataComponentPatch.builder().set(BotariumStorage.FLUID_CONTENTS.componentType(), data).build();
            context.modify(patch);
            context.updateAll();
        };
        readSnapshot(BotariumStorage.FLUID_CONTENTS.get(stack));
    }

    public SimpleFluidStorage(int size, long limit, Object entityOrBlockEntity) {
        this.slots = NonNullList.withSize(size, new SimpleFluidSlot(limit, this::update));
        this.limit = limit;
        this.update = () -> {
            FluidStorageData data = FluidStorageData.from(this);
            BotariumStorage.FLUID_CONTENTS.set(entityOrBlockEntity, data);
        };
        readSnapshot(BotariumStorage.FLUID_CONTENTS.get(entityOrBlockEntity));
    }

    public SimpleFluidStorage filter(int slot, Predicate<FluidResource> predicate) {
        slots.set(slot, new SimpleFluidSlot.Filtered(limit, this::update, predicate));
        return this;
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
    public FluidStorageData createSnapshot() {
        return FluidStorageData.from(this);
    }

    @Override
    public void readSnapshot(FluidStorageData snapshot) {
        for (int i = 0; i < slots.size(); i++) {
            slots.get(i).readSnapshot(snapshot.stacks().get(i));
        }
    }

    @Override
    public void update() {
        update.run();
    }

    @Override
    public long insert(FluidResource unit, long amount, boolean simulate) {
        return TransferUtil.insertSlots(this, unit, amount, simulate);
    }

    @Override
    public long extract(FluidResource unit, long amount, boolean simulate) {
        return TransferUtil.extractSlots(this, unit, amount, simulate);
    }
}
