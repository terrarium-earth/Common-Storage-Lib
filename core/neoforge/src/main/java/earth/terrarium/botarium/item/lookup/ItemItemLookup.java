package earth.terrarium.botarium.item.lookup;

import earth.terrarium.botarium.Botarium;
import earth.terrarium.botarium.context.ItemContext;
import earth.terrarium.botarium.context.impl.ModifyOnlyContext;
import earth.terrarium.botarium.item.wrappers.CommonItemContainerItem;
import earth.terrarium.botarium.item.wrappers.NeoItemHandler;
import earth.terrarium.botarium.lookup.ItemLookup;
import earth.terrarium.botarium.lookup.RegistryEventListener;
import earth.terrarium.botarium.resources.item.ItemResource;
import earth.terrarium.botarium.storage.base.CommonStorage;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public final class ItemItemLookup implements ItemLookup<CommonStorage<ItemResource>, ItemContext>, RegistryEventListener<ItemStack> {
    public static final ItemItemLookup INSTANCE = new ItemItemLookup();
    public static final ResourceLocation NAME = new ResourceLocation(Botarium.MOD_ID, "item_item");
    public static final ResourceLocation BOTARIUM_SPECIFIC_NAME = new ResourceLocation(Botarium.MOD_ID, "botarium_item_item");
    private static final Capability<ItemGetter<CommonStorage<ItemResource>, ItemContext>> CAPABILITY = CapabilityManager.get(new CapabilityToken<>(){});
    private final List<Consumer<ItemRegistrar<CommonStorage<ItemResource>, ItemContext>>> registrars = new ArrayList<>();

    private ItemItemLookup() {
        RegistryEventListener.registerItem(this);
    }

    @Override
    public @Nullable CommonStorage<ItemResource> find(ItemStack stack, ItemContext context) {
        LazyOptional<ItemGetter<CommonStorage<ItemResource>, ItemContext>> botariumCap = stack.getCapability(CAPABILITY);
        if (botariumCap.isPresent()) {
            return botariumCap.orElseThrow(IllegalStateException::new).getContainer(stack, context);
        }
        LazyOptional<IItemHandler> storage = stack.getCapability(ForgeCapabilities.ITEM_HANDLER);
        if (storage.isPresent()) {
            IItemHandler itemStorage = storage.orElseThrow(IllegalStateException::new);
            if (itemStorage instanceof NeoItemHandler(CommonStorage<ItemResource> container)) {
                return container;
            }
            return new CommonItemContainerItem(itemStorage, stack, context);
        }
        return null;
    }

    @Override
    public void onRegister(Consumer<ItemRegistrar<CommonStorage<ItemResource>, ItemContext>> registrar) {
        registrars.add(registrar);
    }

    @Override
    public void register(AttachCapabilitiesEvent<ItemStack> event) {
        registrars.forEach(registrar -> registrar.accept((getter, items) -> {
            for (Item item : items) {
                if (item == event.getObject().getItem()) {
                    event.addCapability(ItemItemLookup.NAME, new ItemCap(getter, event.getObject()));
                    event.addCapability(ItemItemLookup.BOTARIUM_SPECIFIC_NAME, new BotariumItemCap(getter));
                    return;
                }
            }
        }));
    }

    public static class ItemCap implements ICapabilityProvider {
        private final ItemGetter<CommonStorage<ItemResource>, ItemContext> getter;
        private final ItemStack stack;

        public ItemCap(ItemGetter<CommonStorage<ItemResource>, ItemContext> getter, ItemStack stack) {
            this.getter = getter;
            this.stack = stack;
        }

        @Override
        public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction arg) {
            CommonStorage<ItemResource> storage = getter.getContainer(stack, new ModifyOnlyContext(stack));
            LazyOptional<IItemHandler> of = LazyOptional.of(() -> new NeoItemHandler(storage));
            return capability.orEmpty(ForgeCapabilities.FLUID_HANDLER_ITEM, of.cast()).cast();
        }
    }

    public static class BotariumItemCap implements ICapabilityProvider {
        private final ItemGetter<CommonStorage<ItemResource>, ItemContext> getter;

        public BotariumItemCap(ItemGetter<CommonStorage<ItemResource>, ItemContext> getter) {
            this.getter = getter;
        }

        @Override
        public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction arg) {
            LazyOptional<ItemGetter<CommonStorage<ItemResource>, ItemContext>> of = LazyOptional.of(() -> getter);
            return capability.orEmpty(CAPABILITY, of.cast()).cast();
        }
    }
}
