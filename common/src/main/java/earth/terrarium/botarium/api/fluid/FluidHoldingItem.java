package earth.terrarium.botarium.api.fluid;

import net.minecraft.world.item.ItemStack;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public interface FluidHoldingItem {

    /**
     * Gets the {@link ItemFluidContainer} for the given {@link ItemStack}.
     *
     * @param stack the {@link ItemStack} to get the {@link ItemFluidContainer} from.
     * @return the {@link ItemFluidContainer} for the given {@link ItemStack}.
     */
    ItemFluidContainer getFluidContainer(ItemStack stack);
}
