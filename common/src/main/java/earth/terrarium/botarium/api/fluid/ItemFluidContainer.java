package earth.terrarium.botarium.api.fluid;

import net.minecraft.world.item.ItemStack;

public interface ItemFluidContainer extends FluidContainer {

    /**
     * @return The {@link ItemStack} that is used as container item for this fluid container.
     */
    ItemStack getContainerItem();
}
