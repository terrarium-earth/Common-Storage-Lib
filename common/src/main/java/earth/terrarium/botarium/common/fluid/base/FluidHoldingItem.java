package earth.terrarium.botarium.common.fluid.base;

import earth.terrarium.botarium.util.BotariumAttachment;
import net.minecraft.world.item.ItemStack;

public interface FluidHoldingItem extends BotariumAttachment {

    /**
     * Gets the {@link ItemFluidContainer} for the given {@link ItemStack}.
     *
     * @param stack the {@link ItemStack} to get the {@link ItemFluidContainer} from.
     * @return the {@link ItemFluidContainer} for the given {@link ItemStack}.
     */
    ItemFluidContainer getFluidContainer(ItemStack stack);
}
