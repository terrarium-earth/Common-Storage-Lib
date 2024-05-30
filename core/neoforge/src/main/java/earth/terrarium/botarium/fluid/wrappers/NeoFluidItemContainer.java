package earth.terrarium.botarium.fluid.wrappers;

import earth.terrarium.botarium.context.ItemContext;
import earth.terrarium.botarium.resources.fluid.FluidResource;
import earth.terrarium.botarium.resources.item.ItemResource;
import earth.terrarium.botarium.storage.base.CommonStorage;
import earth.terrarium.botarium.storage.base.StorageSlot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
import org.jetbrains.annotations.NotNull;

public record NeoFluidItemContainer(CommonStorage<FluidResource> container, ItemContext context) implements AbstractNeoFluidHandler, IFluidHandlerItem {
    @Override
    public @NotNull ItemStack getContainer() {
        StorageSlot<ItemResource> slot = context.mainSlot();
        return slot.getResource().toStack((int) slot.getAmount());
    }
}
