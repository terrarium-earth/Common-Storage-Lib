package earth.terrarium.botarium.common.fluid.wrappers;

import earth.terrarium.botarium.common.context.ItemContext;
import earth.terrarium.botarium.common.storage.base.UnitContainer;
import earth.terrarium.botarium.common.storage.base.UnitSlot;
import earth.terrarium.botarium.common.transfer.impl.FluidUnit;
import earth.terrarium.botarium.common.transfer.impl.ItemUnit;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
import org.jetbrains.annotations.NotNull;

public record NeoFluidItemContainer(UnitContainer<FluidUnit> container, ItemContext context) implements AbstractNeoFluidHandler, IFluidHandlerItem {
    @Override
    public @NotNull ItemStack getContainer() {
        UnitSlot<ItemUnit> slot = context.mainSlot();
        return slot.getUnit().toStack((int) slot.getAmount());
    }
}
