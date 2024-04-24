package earth.terrarium.botarium.common.item.impl.vanilla;

import earth.terrarium.botarium.common.data.impl.ItemContainerData;
import earth.terrarium.botarium.common.data.impl.SingleItemData;
import earth.terrarium.botarium.common.storage.util.UpdateManager;
import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;

public class WrappedVanillaContainer extends AbstractVanillaContainer implements UpdateManager<ItemContainerData> {
    private final Runnable update;

    public WrappedVanillaContainer(Container container, Runnable update) {
        super(container);
        this.update = update;
    }

    @Override
    public ItemContainerData createSnapshot() {
        return ItemContainerData.of(this);
    }

    @Override
    public void readSnapshot(ItemContainerData snapshot) {
        for (int i = 0; i < container.getContainerSize(); i++) {
            container.setItem(i, snapshot.stacks().get(i).createStack());
        }
    }

    @Override
    public void update() {
        update.run();
    }
}
