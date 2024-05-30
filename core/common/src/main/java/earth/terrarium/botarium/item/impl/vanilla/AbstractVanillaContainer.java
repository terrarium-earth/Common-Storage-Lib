package earth.terrarium.botarium.item.impl.vanilla;

import earth.terrarium.botarium.resources.item.ItemResource;
import earth.terrarium.botarium.storage.base.CommonStorage;
import earth.terrarium.botarium.storage.base.StorageSlot;
import earth.terrarium.botarium.storage.util.TransferUtil;
import net.minecraft.world.Container;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractVanillaContainer implements CommonStorage<ItemResource> {
    Container container;
    List<VanillaDelegatingSlot> slots;

    public AbstractVanillaContainer(Container container) {
        this.container = container;
        this.slots = new ArrayList<>();
        for (int i = 0; i < container.getContainerSize(); i++) {
            slots.add(new VanillaDelegatingSlot(this, i));
        }
    }

    @Override
    public long extract(ItemResource resource, long amount, boolean simulate) {
        return TransferUtil.extractSlots(this, resource, amount, simulate);
    }

    @Override
    public long insert(ItemResource resource, long amount, boolean simulate) {
        return TransferUtil.insertSlots(this, resource, amount, simulate);
    }

    @Override
    public int size() {
        return container.getContainerSize();
    }

    @Override
    public @NotNull StorageSlot<ItemResource> get(int index) {
        return slots.get(index);
    }
}
