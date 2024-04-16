package earth.terrarium.botarium.common.item.base;

import earth.terrarium.botarium.common.storage.base.SlottedContainer;
import earth.terrarium.botarium.common.storage.util.UpdateManager;
import net.minecraft.world.item.ItemStack;

public interface ItemContainer<T> extends SlottedContainer<ItemStack>, UpdateManager<T> {
}
