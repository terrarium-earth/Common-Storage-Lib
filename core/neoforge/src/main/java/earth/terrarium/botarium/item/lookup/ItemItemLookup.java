package earth.terrarium.botarium.item.lookup;

import earth.terrarium.botarium.Botarium;
import earth.terrarium.botarium.context.ItemContext;
import earth.terrarium.botarium.context.impl.ModifyOnlyContext;
import earth.terrarium.botarium.resources.item.ItemResource;
import earth.terrarium.botarium.item.wrappers.CommonItemContainerItem;
import earth.terrarium.botarium.item.wrappers.NeoItemHandler;
import earth.terrarium.botarium.lookup.RegistryEventListener;
import earth.terrarium.botarium.lookup.ItemLookup;
import earth.terrarium.botarium.storage.base.CommonStorage;
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

public final class ItemItemLookup implements ItemLookup<CommonStorage<ItemResource>, ItemContext>, RegistryEventListener {
    public static final ItemItemLookup INSTANCE = new ItemItemLookup();
    private static final ItemCapability<CommonStorage<ItemResource>, ItemContext> CAPABILITY = ItemCapability.create(new ResourceLocation(Botarium.MOD_ID, "item_item"), CommonStorage.asClass(), ItemContext.class);

    private final List<Consumer<ItemRegistrar<CommonStorage<ItemResource>, ItemContext>>> registrars = new ArrayList<>();

    private ItemItemLookup() {
        registerSelf();
    }

    @Override
    public @Nullable CommonStorage<ItemResource> find(ItemStack stack, ItemContext context) {
        CommonStorage<ItemResource> capability = stack.getCapability(CAPABILITY, context);
        if (capability != null) {
            return capability;
        }
        IItemHandler handler = stack.getCapability(Capabilities.ItemHandler.ITEM);
        return handler != null ? new CommonItemContainerItem(handler, stack, context) : null;
    }

    @Override
    public void onRegister(Consumer<ItemRegistrar<CommonStorage<ItemResource>, ItemContext>> registrar) {
        registrars.add(registrar);
    }

    @Override
    public void register(RegisterCapabilitiesEvent event) {
        registrars.forEach(registrar -> registrar.accept((getter, containers) -> {
            event.registerItem(CAPABILITY, getter::getContainer, containers);

            event.registerItem(Capabilities.ItemHandler.ITEM, (stack, ignored) -> {
                ModifyOnlyContext context = new ModifyOnlyContext(stack);
                CommonStorage<ItemResource> container = getter.getContainer(stack, context);
                return container == null ? null : new NeoItemHandler(container);
            }, containers);
        }));
    }
}