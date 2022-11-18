package earth.terrarium.botarium.api.fluid;

import earth.terrarium.botarium.api.BotariumCap;
import net.minecraft.world.item.ItemStack;

public interface FluidHoldingItem extends BotariumCap {

    /**
     * Gets the {@link ItemFluidContainer} for the given {@link ItemStack}.
     *
     * @param stack the {@link ItemStack} to get the {@link ItemFluidContainer} from.
     * @return the {@link ItemFluidContainer} for the given {@link ItemStack}.
     */
    ItemFluidContainer getFluidContainer(ItemStack stack);
}
