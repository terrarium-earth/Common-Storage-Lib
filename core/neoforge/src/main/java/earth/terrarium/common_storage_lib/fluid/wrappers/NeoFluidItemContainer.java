package earth.terrarium.common_storage_lib.fluid.wrappers;

import earth.terrarium.common_storage_lib.context.ItemContext;
import earth.terrarium.common_storage_lib.resources.fluid.FluidResource;
import earth.terrarium.common_storage_lib.resources.item.ItemResource;
import earth.terrarium.common_storage_lib.storage.base.CommonStorage;
import earth.terrarium.common_storage_lib.storage.base.StorageSlot;
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
