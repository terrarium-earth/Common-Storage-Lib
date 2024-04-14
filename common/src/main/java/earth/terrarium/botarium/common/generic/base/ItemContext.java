package earth.terrarium.botarium.common.generic.base;

import earth.terrarium.botarium.common.item.base.ItemContainer;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface ItemContext {
    int insert(@NotNull ItemStack value, boolean simulate);

    ItemStack extract(int amount, boolean simulate) {
        return getContainer().extractFromSlot(getSlot(), amount, simulate);
    }

    int exchange(@NotNull ItemStack value, boolean simulate);

    int insertIndiscriminately(@NotNull ItemStack value, boolean simulate);

    ItemContainer getContainer();

    int getSlot();
}
