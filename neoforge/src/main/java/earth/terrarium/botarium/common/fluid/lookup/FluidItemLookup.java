package earth.terrarium.botarium.common.fluid.lookup;

import earth.terrarium.botarium.Botarium;
import earth.terrarium.botarium.common.context.ItemContext;
import earth.terrarium.botarium.common.context.impl.IsolatedSlotContext;
import earth.terrarium.botarium.common.fluid.wrappers.CommonFluidItemContainer;
import earth.terrarium.botarium.common.fluid.wrappers.NeoFluidItemContainer;
import earth.terrarium.botarium.common.lookup.CapabilityRegisterer;
import earth.terrarium.botarium.common.lookup.ItemLookup;
import earth.terrarium.botarium.common.storage.base.UnitContainer;
import earth.terrarium.botarium.common.transfer.impl.FluidUnit;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.ItemCapability;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

public class FluidItemLookup implements ItemLookup<UnitContainer<FluidUnit>, ItemContext>, CapabilityRegisterer {
    public static final ItemCapability<UnitContainer<FluidUnit>, ItemContext> CAPABILITY = ItemCapability.create(new ResourceLocation(Botarium.MOD_ID, "fluid_item"), UnitContainer.asClass(), ItemContext.class);

    private List<Consumer<ItemRegistrar<UnitContainer<FluidUnit>, ItemContext>>> registrars;

    @Override
    public @Nullable UnitContainer<FluidUnit> find(ItemStack stack, ItemContext context) {
        UnitContainer<FluidUnit> unitContainer = stack.getCapability(CAPABILITY, context);
        if (unitContainer != null) {
            return unitContainer;
        }
        IFluidHandlerItem handler = stack.getCapability(Capabilities.FluidHandler.ITEM);
        return handler != null ? new CommonFluidItemContainer(handler, context) : null;
    }

    @Override
    public void onRegister(Consumer<ItemRegistrar<UnitContainer<FluidUnit>, ItemContext>> registrar) {
        registrars.add(registrar);
    }

    @Override
    public void register(RegisterCapabilitiesEvent event) {
        registrars.forEach(registrar -> registrar.accept((getter, items) -> {
            event.registerItem(CAPABILITY, getter::getContainer, items);

            event.registerItem(Capabilities.FluidHandler.ITEM, (stack, ignored) -> {
                IsolatedSlotContext context = new IsolatedSlotContext(stack);
                UnitContainer<FluidUnit> container = getter.getContainer(stack, context);
                return container == null ? null : new NeoFluidItemContainer(container, context);
            }, items);
        }));
    }
}
