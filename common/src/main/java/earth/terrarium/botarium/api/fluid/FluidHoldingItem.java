package earth.terrarium.botarium.api.fluid;

import net.minecraft.world.item.ItemStack;

public interface FluidHoldingItem {
    FluidContainer getFluidContainer(ItemStack stack);
}
