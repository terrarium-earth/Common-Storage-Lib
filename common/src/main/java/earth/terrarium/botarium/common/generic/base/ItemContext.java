package earth.terrarium.botarium.common.generic.base;

import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface ItemContext {
    int insert(@NotNull ItemStack value, boolean simulate);

    ItemStack extract(int amount, boolean simulate);

}
