package earth.terrarium.botarium.common.item.impl.vanilla;

import earth.terrarium.botarium.common.storage.util.UpdateManager;
import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;

public class WrappedVanillaContainer extends AbstractVanillaContainer implements UpdateManager<NonNullList<ItemStack>> {
    private final Runnable update;

    public WrappedVanillaContainer(Container container, Runnable update) {
        super(container);
        this.update = update;
    }

    @Override
    public NonNullList<ItemStack> createSnapshot() {
        NonNullList<ItemStack> snapshot = NonNullList.withSize(container.getContainerSize(), ItemStack.EMPTY);
        for (int i = 0; i < container.getContainerSize(); i++) {
            snapshot.set(i, container.getItem(i).copy());
        }
        return snapshot;
    }

    @Override
    public void readSnapshot(NonNullList<ItemStack> snapshot) {
        for (int i = 0; i < container.getContainerSize(); i++) {
            container.setItem(i, snapshot.get(i));
        }
    }

    @Override
    public void update() {
        update.run();
    }
}
