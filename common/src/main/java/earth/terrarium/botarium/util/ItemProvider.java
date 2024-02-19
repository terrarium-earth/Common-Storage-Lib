package earth.terrarium.botarium.util;

import net.minecraft.world.item.ItemStack;

public interface ItemProvider<T> extends Updatable, SnapshotProvider<T> {
    ItemStack getContainerItem();
}
