package earth.terrarium.botarium.common.energy.base;

import earth.terrarium.botarium.util.Updatable;
import net.minecraft.world.item.ItemStack;
import earth.terrarium.botarium.common.energy.impl.WrappedItemEnergyContainer;

/**
 * Functional interface that represents an item that can provide an energy storage container.
 *
 * @param <T> The type of energy storage container. Botarium provides a default implementation for this with {@link WrappedItemEnergyContainer}.
 */
@FunctionalInterface
public interface BotariumEnergyItem<T extends EnergyContainer & Updatable> {
    /**
     * Retrieves the energy storage container for the given holder.
     *
     * @param holder The holder item stack.
     * @return The energy storage container.
     */
    T getEnergyStorage(ItemStack holder);
}
