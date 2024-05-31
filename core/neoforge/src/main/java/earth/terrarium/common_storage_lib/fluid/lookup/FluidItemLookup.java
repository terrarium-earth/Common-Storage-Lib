package earth.terrarium.common_storage_lib.fluid.lookup;

import earth.terrarium.common_storage_lib.CommonStorageLib;
import earth.terrarium.common_storage_lib.context.ItemContext;
import earth.terrarium.common_storage_lib.context.impl.IsolatedSlotContext;
import earth.terrarium.common_storage_lib.resources.fluid.FluidResource;
import earth.terrarium.common_storage_lib.fluid.wrappers.CommonFluidItemContainer;
import earth.terrarium.common_storage_lib.fluid.wrappers.NeoFluidItemContainer;
import earth.terrarium.common_storage_lib.lookup.RegistryEventListener;
import earth.terrarium.common_storage_lib.lookup.ItemLookup;
import earth.terrarium.common_storage_lib.storage.base.CommonStorage;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public final class FluidItemLookup implements ItemLookup<CommonStorage<FluidResource>, ItemContext>, RegistryEventListener<ItemStack> {
    public static final FluidItemLookup INSTANCE = new FluidItemLookup();
    public static final ResourceLocation NAME = new ResourceLocation(CommonStorageLib.MOD_ID, "fluid_item");
    public static final ResourceLocation SPECIFIC_NAME = new ResourceLocation(CommonStorageLib.MOD_ID, "common_fluid_item");
    private static final Capability<ItemGetter<CommonStorage<FluidResource>, ItemContext>> CAPABILITY = CapabilityManager.get(new CapabilityToken<>(){});
    private final List<Consumer<ItemRegistrar<CommonStorage<FluidResource>, ItemContext>>> registrars = new ArrayList<>();

    private FluidItemLookup() {
        RegistryEventListener.registerItem(this);
    }

    @Override
    public @Nullable CommonStorage<FluidResource> find(ItemStack stack, ItemContext context) {
        LazyOptional<ItemGetter<CommonStorage<FluidResource>, ItemContext>> commonCap = stack.getCapability(CAPABILITY);
        if (commonCap.isPresent()) {
            return commonCap.orElseThrow(IllegalStateException::new).getContainer(stack, context);
        }
        LazyOptional<IFluidHandlerItem> storage = stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM);
        if (storage.isPresent()) {
            IFluidHandlerItem fluidStorage = storage.orElseThrow(IllegalStateException::new);
            return new CommonFluidItemContainer(fluidStorage, context);
        }
        return null;
    }

    @Override
    public void onRegister(Consumer<ItemRegistrar<CommonStorage<FluidResource>, ItemContext>> registrar) {
        registrars.add(registrar);
    }

    @Override
    public void register(AttachCapabilitiesEvent<ItemStack> event) {
        registrars.forEach(registrar -> registrar.accept((getter, items) -> {
            for (Item item : items) {
                if (item == event.getObject().getItem()) {
                    event.addCapability(NAME, new FluidItemCap(getter, event.getObject(), new IsolatedSlotContext(event.getObject())));
                    event.addCapability(SPECIFIC_NAME, new CommonFluidCap(getter));
                    return;
                }
            }
        }));
    }

    public static class FluidItemCap implements ICapabilityProvider {
        private final ItemGetter<CommonStorage<FluidResource>, ItemContext> getter;
        private final ItemStack stack;
        private final ItemContext context;

        public FluidItemCap(ItemGetter<CommonStorage<FluidResource>, ItemContext> getter, ItemStack stack, ItemContext context) {
            this.getter = getter;
            this.stack = stack;
            this.context = context;
        }

        @Override
        public <T> @NotNull LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction side) {
            CommonStorage<FluidResource> storage = getter.getContainer(stack, context);
            LazyOptional<IFluidHandlerItem> of = LazyOptional.of(() -> new NeoFluidItemContainer(storage, context));
            return capability.orEmpty(ForgeCapabilities.FLUID_HANDLER_ITEM, of.cast()).cast();
        }
    }

    public static class CommonFluidCap implements ICapabilityProvider {
        private final ItemGetter<CommonStorage<FluidResource>, ItemContext> getter;

        public CommonFluidCap(ItemGetter<CommonStorage<FluidResource>, ItemContext> getter) {
            this.getter = getter;
        }

        @Override
        public <T> @NotNull LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction side) {
            LazyOptional<ItemGetter<CommonStorage<FluidResource>, ItemContext>> of = LazyOptional.of(() -> getter);
            return capability.orEmpty(ForgeCapabilities.FLUID_HANDLER_ITEM, of.cast()).cast();
        }
    }
}
