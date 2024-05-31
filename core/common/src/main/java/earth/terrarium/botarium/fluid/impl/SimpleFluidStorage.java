package earth.terrarium.botarium.fluid.impl;

import earth.terrarium.botarium.context.ItemContext;
import earth.terrarium.botarium.fluid.util.FluidStorageData;
import earth.terrarium.botarium.resources.fluid.FluidResource;
import earth.terrarium.botarium.resources.item.ItemResource;
import earth.terrarium.botarium.storage.base.CommonStorage;
import earth.terrarium.botarium.storage.base.UpdateManager;
import earth.terrarium.botarium.storage.util.TransferUtil;
import net.minecraft.core.NonNullList;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public class SimpleFluidStorage implements CommonStorage<FluidResource>, UpdateManager<FluidStorageData> {
    public static final String KEY = "botarium:fluid_contents";
    protected final NonNullList<SimpleFluidSlot> slots;
    private final Runnable update;
    private final long limit;

    public SimpleFluidStorage(int size, long limit, Runnable runnable) {
        this.slots = NonNullList.withSize(size, new SimpleFluidSlot(limit, this::update));
        this.limit = limit;
        this.update = runnable;
    }

    public SimpleFluidStorage(int size, long limit, ItemContext context) {
        this.slots = NonNullList.withSize(size, new SimpleFluidSlot(limit, this::update));
        this.limit = limit;
        this.update = () -> {
            FluidStorageData data = FluidStorageData.from(this);
            ItemResource resource = context.getResource().set(FluidStorageData.CODEC, KEY, data);
            context.exchange(resource, context.getAmount(), false);
        };
        readSnapshot(context.getResource().getOrDefault(FluidStorageData.CODEC, KEY, FluidStorageData.EMPTY));
    }

    public SimpleFluidStorage filter(int slot, Predicate<FluidResource> predicate) {
        slots.set(slot, new SimpleFluidSlot.Filtered(limit, this::update, predicate));
        return this;
    }

    @Override
    public int size() {
        return slots.size();
    }

    @Override
    public @NotNull SimpleFluidSlot get(int index) {
        return slots.get(index);
    }

    @Override
    public FluidStorageData createSnapshot() {
        return FluidStorageData.from(this);
    }

    @Override
    public void readSnapshot(FluidStorageData snapshot) {
        for (int i = 0; i < slots.size() && i < snapshot.stacks().size(); i++) {
            slots.get(i).readSnapshot(snapshot.stacks().get(i));
        }
    }

    @Override
    public void update() {
        update.run();
    }

    @Override
    public long insert(FluidResource resource, long amount, boolean simulate) {
        return TransferUtil.insertSlots(this, resource, amount, simulate);
    }

    public long insertAndUpdate(FluidResource resource, long amount, boolean simulate) {
        long inserted = TransferUtil.insertSlots(this, resource, amount, simulate);
        if (!simulate) update();
        return inserted;
    }

    @Override
    public long extract(FluidResource resource, long amount, boolean simulate) {
        return TransferUtil.extractSlots(this, resource, amount, simulate);
    }

    public long extractAndUpdate(FluidResource resource, long amount, boolean simulate) {
        long extracted = TransferUtil.extractSlots(this, resource, amount, simulate);
        if (!simulate) update();
        return extracted;
    }

    public static class ExtractOnly extends SimpleFluidStorage {
        public ExtractOnly(int size, long limit, Runnable runnable) {
            super(size, limit, runnable);
        }

        public ExtractOnly(int size, long limit, ItemContext context) {
            super(size, limit, context);
        }

        @Override
        public long insert(FluidResource resource, long amount, boolean simulate) {
            return 0;
        }

        @Override
        public ExtractOnly filter(int slot, Predicate<FluidResource> predicate) {
            return (ExtractOnly) super.filter(slot, predicate);
        }

        @Override
        public boolean allowsInsertion() {
            return false;
        }
    }

    public static class InsertOnly extends SimpleFluidStorage {
        public InsertOnly(int size, long limit, Runnable runnable) {
            super(size, limit, runnable);
        }

        public InsertOnly(int size, long limit, ItemContext context) {
            super(size, limit, context);
        }

        @Override
        public long extract(FluidResource resource, long amount, boolean simulate) {
            return 0;
        }

        @Override
        public InsertOnly filter(int slot, Predicate<FluidResource> predicate) {
            return (InsertOnly) super.filter(slot, predicate);
        }

        @Override
        public boolean allowsExtraction() {
            return false;
        }
    }
}
