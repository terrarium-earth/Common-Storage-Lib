package earth.terrarium.botarium.energy.impl;

import earth.terrarium.botarium.BotariumStorage;
import earth.terrarium.botarium.context.ItemContext;
import earth.terrarium.botarium.storage.base.ValueStorage;
import earth.terrarium.botarium.storage.base.UpdateManager;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.world.item.ItemStack;

public class SimpleValueStorage implements ValueStorage, UpdateManager<Long> {
    private final long capacity;
    private final Runnable onUpdate;
    private long amount;

    public SimpleValueStorage(long capacity) {
        this.capacity = capacity;
        this.onUpdate = () -> {};
    }

    @SuppressWarnings("DataFlowIssue")
    public SimpleValueStorage(long capacity, ItemStack stack, ItemContext context) {
        this.capacity = capacity;
        this.onUpdate = () -> {
            DataComponentPatch data = DataComponentPatch.builder().set(BotariumStorage.VALUE_CONTENT.componentType(), this.amount).build();
            context.modify(data);
            context.updateAll();
        };
        if (stack.has(BotariumStorage.VALUE_CONTENT.componentType())) {
            this.amount = stack.get(BotariumStorage.VALUE_CONTENT.componentType());
        }
    }

    public SimpleValueStorage(long capacity, Object entityOrBlockEntity) {
        this.capacity = capacity;
        this.onUpdate = () -> BotariumStorage.VALUE_CONTENT.set(entityOrBlockEntity, this.amount);
        this.amount = BotariumStorage.VALUE_CONTENT.get(entityOrBlockEntity);
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
