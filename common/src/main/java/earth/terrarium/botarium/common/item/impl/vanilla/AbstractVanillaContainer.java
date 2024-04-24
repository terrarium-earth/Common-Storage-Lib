package earth.terrarium.botarium.common.item.impl.vanilla;

import earth.terrarium.botarium.common.storage.base.UnitContainer;
import earth.terrarium.botarium.common.storage.base.UnitSlot;
import earth.terrarium.botarium.common.transfer.impl.ItemUnit;
import net.minecraft.world.Container;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractVanillaContainer implements UnitContainer<ItemUnit> {
    Container container;

    public AbstractVanillaContainer(Container container) {
        this.container = container;
    }

    @Override
    public int getSlotCount() {
        return container.getContainerSize();
    }

    @Override
    public @NotNull UnitSlot<ItemUnit> getSlot(int slot) {
        return new VanillaDelegatingSlot(this, slot);
    }
}
