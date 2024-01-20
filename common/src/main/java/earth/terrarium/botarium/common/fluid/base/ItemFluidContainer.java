package earth.terrarium.botarium.common.fluid.base;

import net.minecraft.world.item.ItemStack;

public interface ItemFluidContainer extends FluidContainer {

    /**
     * @return The {@link ItemStack} that is used as container item for this fluid container.
     */
    ItemStack getContainerItem();
}
