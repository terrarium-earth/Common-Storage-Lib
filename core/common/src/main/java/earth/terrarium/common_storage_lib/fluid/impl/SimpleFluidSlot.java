package earth.terrarium.common_storage_lib.fluid.impl;

import earth.terrarium.common_storage_lib.resources.fluid.FluidResource;
import earth.terrarium.common_storage_lib.storage.base.StorageSlot;
import earth.terrarium.common_storage_lib.storage.base.UpdateManager;
import earth.terrarium.common_storage_lib.resources.ResourceStack;

import java.util.function.Predicate;

public class SimpleFluidSlot implements StorageSlot<FluidResource>, UpdateManager<ResourceStack<FluidResource>> {
    private final long limit;
    private final Runnable update;
    private final Runnable save;
    private FluidResource resource;
    private long amount;

    public SimpleFluidSlot(long limit, Runnable update, Runnable save) {
        this.resource = FluidResource.BLANK;
        this.amount = getAmount();
        this.limit = limit;
        this.update = update;
        this.save = save;
    }

    public SimpleFluidSlot(long limit, Runnable update) {
        this(limit, update, () -> {});
    }

    @Override
    public long getLimit(FluidResource resource) {
        return limit;
    }

    @Override
    public boolean isResourceValid(FluidResource resource) {
        return true;
    }

    @Override
    public FluidResource getResource() {
        return resource;
    }

    @Override
    public long getAmount() {
        return amount;
    }

    @Override
    public long insert(FluidResource resource, long amount, boolean simulate) {
        if (!isResourceValid(resource)) return 0;
        if (this.resource.isBlank()) {
            long inserted = Math.min(amount, limit);
            if (!simulate) {
                this.resource = resource;
                this.amount = inserted;
                save.run();
            }
            return inserted;
        } else if (this.resource.equals(resource)) {
            long inserted = Math.min(amount, limit - this.amount);
            if (!simulate) {
                this.amount += inserted;
                save.run();
            }
            return inserted;
        }
        return 0;
    }

    @Override
    public long extract(FluidResource resource, long amount, boolean simulate) {
        if (this.resource.equals(resource)) {
            long extracted = Math.min(amount, this.amount);
            if (!simulate) {
                this.amount -= extracted;
                if (this.amount == 0) {
                    this.resource = FluidResource.BLANK;
                }
                save.run();
            }
            return extracted;
        }
        return 0;
    }

    @Override
    public ResourceStack<FluidResource> createSnapshot() {
        return new ResourceStack<>(resource, amount);
    }

    @Override
    public void readSnapshot(ResourceStack<FluidResource> snapshot) {
        this.resource = snapshot.resource();
        this.amount = snapshot.amount();
    }

    @Override
    public void update() {
        update.run();
    }

    public static class Filtered extends SimpleFluidSlot {
        private final Predicate<FluidResource> filter;

        public Filtered(long limit, Runnable update, Runnable save, Predicate<FluidResource> filter) {
            super(limit, update, save);
            this.filter = filter;
        }

        @Deprecated
        public Filtered(long limit, Runnable update, Predicate<FluidResource> filter) {
            this(limit, update, () -> {}, filter);
        }

        @Override
        public boolean isResourceValid(FluidResource resource) {
            return filter.test(resource);
        }
    }
}