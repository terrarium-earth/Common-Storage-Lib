package earth.terrarium.botarium.api.fluid;

import net.minecraft.world.item.ItemStack;

public interface FluidHoldingItem {
    ItemFluidContainer getFluidContainer(ItemStack stack);
}
