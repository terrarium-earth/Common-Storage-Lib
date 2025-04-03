package earth.terrarium.common_storage_lib.wrapped;

import earth.terrarium.common_storage_lib.resources.fluid.FluidResource;
import earth.terrarium.common_storage_lib.storage.ConversionUtils;
import earth.terrarium.common_storage_lib.resources.Resource;
import earth.terrarium.common_storage_lib.context.ItemContext;
import earth.terrarium.common_storage_lib.lookup.ItemLookup;
import earth.terrarium.common_storage_lib.storage.base.CommonStorage;
import earth.terrarium.common_storage_lib.storage.common.CommonWrappedContainer;
import earth.terrarium.common_storage_lib.storage.context.CommonItemContext;
import earth.terrarium.common_storage_lib.storage.context.FabricItemContext;
import earth.terrarium.common_storage_lib.storage.fabric.FabricWrappedContainer;
import earth.terrarium.common_storage_lib.storage.impl.AutoUpdatingCommonStorage;
import net.fabricmc.fabric.api.lookup.v1.item.ItemApiLookup;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.TransferVariant;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Predicate;

public abstract class WrappedItemLookup<U extends Resource, V extends TransferVariant<?>> implements ItemLookup<CommonStorage<U>, ItemContext> {
    private final ItemApiLookup<Storage<V>, ContainerItemContext> fabricLookup;

    public WrappedItemLookup(ItemApiLookup<Storage<V>, ContainerItemContext> fabricLookup) {
        this.fabricLookup = fabricLookup;
    }

    @Override
    public void onRegister(Consumer<ItemRegistrar<CommonStorage<U>, ItemContext>> registrar) {
        registrar.accept(this::registerItems);
    }

    public void registerItems(ItemGetter<CommonStorage<U>, ItemContext> getter, Item... items) {
        fabricLookup.registerForItems((stack, context) -> {
            CommonStorage<U> container = getter.getContainer(stack, new CommonItemContext(context));
            return container == null ? null : wrap(container);
        }, items);
    }

    @Override
    public void registerFallback(ItemGetter<CommonStorage<U>, ItemContext> getter) {
        fabricLookup.registerFallback((stack, context) -> {
            CommonStorage<U> container = getter.getContainer(stack, new CommonItemContext(context));
            return container == null ? null : wrap(container);
        });
    }

    @Override
    public void registerFallback(ItemGetter<CommonStorage<U>, ItemContext> getter, Predicate<Item> itemPredicate) {
        registerFallback(getter);
    }

    public abstract FabricWrappedContainer<U, V> wrap(CommonStorage<U> container);

    public ItemApiLookup<Storage<V>, ContainerItemContext> getFabricLookup() {
        return fabricLookup;
    }

    public static class OfFluid extends WrappedItemLookup<FluidResource, FluidVariant> {
        public OfFluid() {
            super(FluidStorage.ITEM);
        }

        @Override
        public FabricWrappedContainer<FluidResource, FluidVariant> wrap(CommonStorage<FluidResource> container) {
            return new FabricWrappedContainer.OfFluid(container);
        }

        @Override
        public @Nullable CommonStorage<FluidResource> find(ItemStack stack, ItemContext context) {
            Storage<FluidVariant> storage = getFabricLookup().find(stack, new FabricItemContext(context));
            if (storage != null) {
                if (storage instanceof FabricWrappedContainer.OfFluid container) {
                    return new AutoUpdatingCommonStorage<>(container.container());
                }
                return new CommonWrappedContainer<>(storage, ConversionUtils::toVariant, ConversionUtils::toResource);
            }
            return null;
        }
    }
}
