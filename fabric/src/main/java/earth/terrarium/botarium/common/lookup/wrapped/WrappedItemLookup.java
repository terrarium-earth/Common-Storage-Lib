package earth.terrarium.botarium.common.lookup.wrapped;

import earth.terrarium.botarium.common.context.ItemContext;
import earth.terrarium.botarium.common.lookup.ItemLookup;
import earth.terrarium.botarium.common.storage.base.UnitContainer;
import earth.terrarium.botarium.common.storage.common.CommonWrappedContainer;
import earth.terrarium.botarium.common.storage.context.CommonItemContext;
import earth.terrarium.botarium.common.storage.context.FabricItemContext;
import earth.terrarium.botarium.common.storage.fabric.FabricWrappedContainer;
import earth.terrarium.botarium.common.storage.util.UpdateManager;
import earth.terrarium.botarium.common.transfer.base.TransferUnit;
import net.fabricmc.fabric.api.lookup.v1.item.ItemApiLookup;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.TransferVariant;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Function;

public class WrappedItemLookup<T, U extends TransferUnit<T>, V extends TransferVariant<T>> implements ItemLookup<UnitContainer<U>, ItemContext> {
    private final ItemApiLookup<Storage<V>, ContainerItemContext> fabricLookup;
    private final Function<U, V> toVariant;
    private final Function<V, U> toUnit;

    public WrappedItemLookup(ItemApiLookup<Storage<V>, ContainerItemContext> fabricLookup, Function<U, V> toVariant, Function<V, U> toUnit) {
        this.fabricLookup = fabricLookup;
        this.toVariant = toVariant;
        this.toUnit = toUnit;
    }

    @Override
    public @Nullable UnitContainer<U> find(ItemStack stack, ItemContext context) {
        Storage<V> storage = fabricLookup.find(stack, new FabricItemContext(context));
        if (storage != null) {
            if (storage instanceof FabricWrappedContainer<T, U, V> fabric) {
                return fabric.getContainer();
            }
            return new CommonWrappedContainer<>(storage, toVariant, toUnit);
        }
        return null;
    }

    @Override
    public void onRegister(Consumer<ItemRegistrar<UnitContainer<U>, ItemContext>> registrar) {
        registrar.accept(this::registerItems);
    }

    public void registerItems(ItemGetter<UnitContainer<U>, ItemContext> getter, Item... items) {
        fabricLookup.registerForItems((stack, context) -> {
            UnitContainer<U> container = getter.getContainer(stack, new CommonItemContext(context));
            if (container != null) {
                return new FabricWrappedContainer<>(container, toVariant, toUnit);
            }
            return null;
        }, items);
    }
}
