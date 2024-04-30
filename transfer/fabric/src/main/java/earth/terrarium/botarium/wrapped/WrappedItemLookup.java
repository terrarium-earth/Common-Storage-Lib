package earth.terrarium.botarium.wrapped;

import earth.terrarium.botarium.fluid.base.FluidUnit;
import earth.terrarium.botarium.item.base.ItemUnit;
import earth.terrarium.botarium.storage.ConversionUtils;
import earth.terrarium.botarium.storage.unit.TransferUnit;
import earth.terrarium.botarium.context.ItemContext;
import earth.terrarium.botarium.lookup.ItemLookup;
import earth.terrarium.botarium.storage.base.CommonStorage;
import earth.terrarium.botarium.storage.common.CommonWrappedContainer;
import earth.terrarium.botarium.storage.context.CommonItemContext;
import earth.terrarium.botarium.storage.context.FabricItemContext;
import earth.terrarium.botarium.storage.fabric.FabricWrappedContainer;
import net.fabricmc.fabric.api.lookup.v1.item.ItemApiLookup;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.TransferVariant;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Function;

public abstract class WrappedItemLookup<T, U extends TransferUnit<T, U>, V extends TransferVariant<T>> implements ItemLookup<CommonStorage<U>, ItemContext> {
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
            if (container != null) {
                return wrap(container);
            }
            return null;
        }, items);
    }

    public abstract FabricWrappedContainer<T, U, V> wrap(CommonStorage<U> container);

    public ItemApiLookup<Storage<V>, ContainerItemContext> getFabricLookup() {
        return fabricLookup;
    }

    public static class OfFluid extends WrappedItemLookup<Fluid, FluidUnit, FluidVariant> {
        public OfFluid() {
            super(FluidStorage.ITEM);
        }

        @Override
        public FabricWrappedContainer<Fluid, FluidUnit, FluidVariant> wrap(CommonStorage<FluidUnit> container) {
            return new FabricWrappedContainer.OfFluid(container);
        }

        @Override
        public @Nullable CommonStorage<FluidUnit> find(ItemStack stack, ItemContext context) {
            Storage<FluidVariant> storage = getFabricLookup().find(stack, new FabricItemContext(context));
            if (storage != null) {
                if (storage instanceof FabricWrappedContainer.OfFluid container) {
                    return container.container();
                }
                return new CommonWrappedContainer<>(storage, ConversionUtils::toVariant, ConversionUtils::toUnit);
            }
            return null;
        }
    }
}
