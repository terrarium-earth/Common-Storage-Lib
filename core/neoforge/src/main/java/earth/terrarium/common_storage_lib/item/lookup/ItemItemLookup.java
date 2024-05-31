package earth.terrarium.common_storage_lib.item.lookup;

import earth.terrarium.common_storage_lib.CommonStorageLib;
import earth.terrarium.common_storage_lib.context.ItemContext;
import earth.terrarium.common_storage_lib.context.impl.ModifyOnlyContext;
import earth.terrarium.common_storage_lib.item.wrappers.CommonItemContainerItem;
import earth.terrarium.common_storage_lib.item.wrappers.NeoItemHandler;
import earth.terrarium.common_storage_lib.lookup.ItemLookup;
import earth.terrarium.common_storage_lib.lookup.RegistryEventListener;
import earth.terrarium.common_storage_lib.resources.item.ItemResource;
import earth.terrarium.common_storage_lib.storage.base.CommonStorage;
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
    public static final ResourceLocation NAME = new ResourceLocation(CommonStorageLib.MOD_ID, "item_item");
    public static final ResourceLocation SPECIFIC_NAME = new ResourceLocation(CommonStorageLib.MOD_ID, "common_item_item");
    private static final Capability<ItemGetter<CommonStorage<ItemResource>, ItemContext>> CAPABILITY = CapabilityManager.get(new CapabilityToken<>(){});
    private final List<Consumer<ItemRegistrar<CommonStorage<ItemResource>, ItemContext>>> registrars = new ArrayList<>();

    private ItemItemLookup() {
        RegistryEventListener.registerItem(this);
    }

    @Override
    public @Nullable CommonStorage<ItemResource> find(ItemStack stack, ItemContext context) {
        LazyOptional<ItemGetter<CommonStorage<ItemResource>, ItemContext>> commonCap = stack.getCapability(CAPABILITY);
        if (commonCap.isPresent()) {
            return commonCap.orElseThrow(IllegalStateException::new).getContainer(stack, context);
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
                    event.addCapability(ItemItemLookup.SPECIFIC_NAME, new CommonItemCap(getter));
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

    public static class CommonItemCap implements ICapabilityProvider {
        private final ItemGetter<CommonStorage<ItemResource>, ItemContext> getter;

        public CommonItemCap(ItemGetter<CommonStorage<ItemResource>, ItemContext> getter) {
            this.getter = getter;
        }

        @Override
        public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction arg) {
            LazyOptional<ItemGetter<CommonStorage<ItemResource>, ItemContext>> of = LazyOptional.of(() -> getter);
            return capability.orEmpty(CAPABILITY, of.cast()).cast();
        }
    }
}
