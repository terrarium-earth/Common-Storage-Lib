package earth.terrarium.botarium.common.fluid.base;

import earth.terrarium.botarium.util.Updatable;
import net.minecraft.world.item.ItemStack;
import earth.terrarium.botarium.common.fluid.impl.WrappedItemFluidContainer;

/**
 * An interface that represents an item that can provide a fluid container for a Botarium block.
 *
 * @param <T> The type of the fluid container. Must implement the {@link ItemFluidContainer} and {@link Updatable} interfaces. Botarium provides a default implementation for this with {@link WrappedItemFluidContainer}.
 */
public interface BotariumFluidItem<T extends ItemFluidContainer & Updatable> {

    /**
     * @return The {@link ItemFluidContainer} for the block.
     */
    T getFluidContainer(ItemStack holder);
}