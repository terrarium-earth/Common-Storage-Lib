package earth.terrarium.botarium.api.fluid;

import net.minecraft.world.item.ItemStack;

public interface ItemFluidContainer extends FluidContainer {
    ItemStack getContainerItem();
}
