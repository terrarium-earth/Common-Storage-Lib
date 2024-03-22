package earth.terrarium.botarium.common.item.base;

import earth.terrarium.botarium.util.Snapshotable;
import earth.terrarium.botarium.util.Updatable;
import net.minecraft.world.item.ItemStack;

public interface BotariumItemItem<T extends ItemContainer & Updatable & Snapshotable<ItemSnapshot>> {
    T getItemContainer(ItemStack stack);
}
