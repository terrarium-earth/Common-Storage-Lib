package earth.terrarium.common_storage_lib.item.impl.vanilla;

import earth.terrarium.common_storage_lib.item.util.ItemStorageData;
import earth.terrarium.common_storage_lib.storage.base.UpdateManager;
import earth.terrarium.common_storage_lib.resources.ResourceStack;
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
