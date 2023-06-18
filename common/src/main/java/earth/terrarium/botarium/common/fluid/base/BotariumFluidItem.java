package earth.terrarium.botarium.common.fluid.base;

import earth.terrarium.botarium.util.Updatable;
import net.minecraft.world.item.ItemStack;

public interface BotariumFluidItem<T extends ItemFluidContainer & Updatable<ItemStack>> {

    /**
     * @return The {@link ItemFluidContainer} for the block.
     */
    T getFluidContainer(ItemStack holder);
}