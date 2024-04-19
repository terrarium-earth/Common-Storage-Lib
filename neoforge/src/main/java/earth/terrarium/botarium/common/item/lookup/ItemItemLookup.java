package earth.terrarium.botarium.common.item.lookup;

import earth.terrarium.botarium.Botarium;
import earth.terrarium.botarium.common.context.ItemContext;
import earth.terrarium.botarium.common.context.impl.ModifyOnlyContext;
import earth.terrarium.botarium.common.item.wrappers.CommonItemContainer;
import earth.terrarium.botarium.common.item.wrappers.CommonItemContainerItem;
import earth.terrarium.botarium.common.item.wrappers.NeoItemHandler;
import earth.terrarium.botarium.common.lookup.CapabilityRegisterer;
import earth.terrarium.botarium.common.lookup.ItemLookup;
import earth.terrarium.botarium.common.storage.base.UnitContainer;
import earth.terrarium.botarium.common.transfer.impl.ItemUnit;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.ItemCapability;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ItemItemLookup implements ItemLookup<UnitContainer<ItemUnit>, ItemContext>, CapabilityRegisterer {
    public static final ItemCapability<UnitContainer<ItemUnit>, ItemContext> CAPABILITY = ItemCapability.create(new ResourceLocation(Botarium.MOD_ID, "item_item"), UnitContainer.asClass(), ItemContext.class);
    private final List<Consumer<ItemRegistrar<UnitContainer<ItemUnit>, ItemContext>>> registrars = new ArrayList<>();

    @Override
    public @Nullable UnitContainer<ItemUnit> find(ItemStack stack, ItemContext context) {
        UnitContainer<ItemUnit> capability = stack.getCapability(CAPABILITY, context);
        if (capability != null) {
            return capability;
        }
        IItemHandler handler = stack.getCapability(Capabilities.ItemHandler.ITEM);
        return handler != null ? new CommonItemContainerItem(handler, stack, context) : null;
    }

    @Override
    public void onRegister(Consumer<ItemRegistrar<UnitContainer<ItemUnit>, ItemContext>> registrar) {
        registrars.add(registrar);
    }

    @Override
    public void register(RegisterCapabilitiesEvent event) {
        registrars.forEach(registrar -> registrar.accept((getter, containers) -> {
            event.registerItem(CAPABILITY, getter::getContainer, containers);

            event.registerItem(Capabilities.ItemHandler.ITEM, (stack, ignored) -> {
                ModifyOnlyContext context = new ModifyOnlyContext(stack);
                UnitContainer<ItemUnit> container = getter.getContainer(stack, context);
                return container == null ? null : new NeoItemHandler(container);
            }, containers);
        }));
    }
}
