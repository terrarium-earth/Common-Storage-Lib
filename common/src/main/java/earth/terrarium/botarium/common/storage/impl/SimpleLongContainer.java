package earth.terrarium.botarium.common.storage.impl;

import earth.terrarium.botarium.common.context.ItemContext;
import earth.terrarium.botarium.common.data.utils.ContainerDataManagers;
import earth.terrarium.botarium.common.storage.base.LongContainer;
import earth.terrarium.botarium.common.storage.util.UpdateManager;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.world.item.ItemStack;

public class SimpleLongContainer implements LongContainer, UpdateManager<Long> {
    private final long capacity;
    private final Runnable onUpdate;
    private long amount;

    public SimpleLongContainer(long capacity, Runnable onUpdate) {
        this.capacity = capacity;
        this.onUpdate = onUpdate;
    }

    @SuppressWarnings("DataFlowIssue")
    public SimpleLongContainer(long capacity, ItemStack stack, ItemContext context) {
        this.capacity = capacity;
        this.onUpdate = () -> {
            DataComponentPatch data = DataComponentPatch.builder().set(ContainerDataManagers.LONG_CONTENTS.componentType(), this.amount).build();
            context.modify(data);
            context.updateAll();
        };
        if (stack.has(ContainerDataManagers.LONG_CONTENTS.componentType())) {
            this.amount = stack.get(ContainerDataManagers.LONG_CONTENTS.componentType());
        }
    }

    public SimpleLongContainer(long capacity, long amount, Object entityOrBlockEntity) {
        this.capacity = capacity;
        this.onUpdate = () -> ContainerDataManagers.LONG_CONTENTS.setData(entityOrBlockEntity, this.amount);
        this.amount = ContainerDataManagers.LONG_CONTENTS.getData(entityOrBlockEntity);
    }

    @Override
    public long getStoredAmount() {
        return amount;
    }

    @Override
    public long getCapacity() {
        return capacity;
    }

    public void set(long amount) {
        this.amount = amount;
    }

    @Override
    public boolean allowsInsertion() {
        return true;
    }

    @Override
    public boolean allowsExtraction() {
        return true;
    }

    @Override
    public long insert(long amount, boolean simulate) {
        long inserted = Math.min(amount, capacity - this.amount);
        if (!simulate) {
            this.amount += inserted;
        }
        return inserted;
    }

    @Override
    public long extract(long amount, boolean simulate) {
        long extracted = Math.min(amount, this.amount);
        if (!simulate) {
            this.amount -= extracted;
        }
        return extracted;
    }

    @Override
    public Long createSnapshot() {
        return amount;
    }

    @Override
    public void readSnapshot(Long snapshot) {
        this.amount = snapshot;
    }

    @Override
    public void update() {
        onUpdate.run();
    }
}
