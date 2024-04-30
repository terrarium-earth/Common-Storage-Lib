package earth.terrarium.botarium.fluid.lookup;

import earth.terrarium.botarium.BotariumTransfer;
import earth.terrarium.botarium.context.ItemContext;
import earth.terrarium.botarium.context.impl.IsolatedSlotContext;
import earth.terrarium.botarium.resource.fluid.FluidResource;
import earth.terrarium.botarium.fluid.wrappers.CommonFluidItemContainer;
import earth.terrarium.botarium.fluid.wrappers.NeoFluidItemContainer;
import earth.terrarium.botarium.lookup.RegistryEventListener;
import earth.terrarium.botarium.lookup.ItemLookup;
import earth.terrarium.botarium.storage.base.CommonStorage;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.ItemCapability;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

public final class FluidItemLookup implements ItemLookup<CommonStorage<FluidResource>, ItemContext>, RegistryEventListener {
    public static final FluidItemLookup INSTANCE = new FluidItemLookup();
    private static final ItemCapability<CommonStorage<FluidResource>, ItemContext> CAPABILITY = ItemCapability.create(new ResourceLocation(BotariumTransfer.MOD_ID, "fluid_item"), CommonStorage.asClass(), ItemContext.class);

    private List<Consumer<ItemRegistrar<CommonStorage<FluidResource>, ItemContext>>> registrars;

    private FluidItemLookup() {
        registerSelf();
    }

    @Override
    public @Nullable CommonStorage<FluidResource> find(ItemStack stack, ItemContext context) {
        CommonStorage<FluidResource> unitContainer = stack.getCapability(CAPABILITY, context);
        if (unitContainer != null) {
            return unitContainer;
        }
        IFluidHandlerItem handler = stack.getCapability(Capabilities.FluidHandler.ITEM);
        return handler != null ? new CommonFluidItemContainer(handler, context) : null;
    }

    @Override
    public void onRegister(Consumer<ItemRegistrar<CommonStorage<FluidResource>, ItemContext>> registrar) {
        registrars.add(registrar);
    }

    @Override
    public void register(RegisterCapabilitiesEvent event) {
        registrars.forEach(registrar -> registrar.accept((getter, items) -> {
            event.registerItem(CAPABILITY, getter::getContainer, items);

            event.registerItem(Capabilities.FluidHandler.ITEM, (stack, ignored) -> {
                IsolatedSlotContext context = new IsolatedSlotContext(stack);
                CommonStorage<FluidResource> container = getter.getContainer(stack, context);
                return container == null ? null : new NeoFluidItemContainer(container, context);
            }, items);
        }));
    }
}
