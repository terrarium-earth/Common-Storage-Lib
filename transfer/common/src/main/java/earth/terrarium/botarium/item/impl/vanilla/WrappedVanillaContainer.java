package earth.terrarium.botarium.item.impl.vanilla;

import earth.terrarium.botarium.item.util.ItemStorageData;
import earth.terrarium.botarium.storage.base.UpdateManager;
import earth.terrarium.botarium.resources.ResourceStack;
import net.minecraft.world.Container;

public class WrappedVanillaContainer extends AbstractVanillaContainer implements UpdateManager<ItemStorageData> {
    public WrappedVanillaContainer(Container container) {
        super(container);
    }

    @Override
    public ItemStorageData createSnapshot() {
        return ItemStorageData.of(this);
    }

    @Override
    public void readSnapshot(ItemStorageData snapshot) {
        for (int i = 0; i < container.getContainerSize(); i++) {
            container.setItem(i, ResourceStack.toItemStack(snapshot.stacks().get(i)));
        }
    }

    @Override
    public void update() {
        container.setChanged();
    }
}
