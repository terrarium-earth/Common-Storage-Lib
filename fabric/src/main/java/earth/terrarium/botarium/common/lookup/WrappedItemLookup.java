package earth.terrarium.botarium.common.lookup;

import earth.terrarium.botarium.common.context.ItemContext;
import earth.terrarium.botarium.common.storage.ConversionUtils;
import earth.terrarium.botarium.common.storage.base.UnitContainer;
import earth.terrarium.botarium.common.storage.common.CommonWrappedContainer;
import earth.terrarium.botarium.common.storage.context.CommonItemContext;
import earth.terrarium.botarium.common.storage.context.FabricItemContext;
import earth.terrarium.botarium.common.storage.fabric.FabricWrappedContainer;
import earth.terrarium.botarium.common.storage.util.UpdateManager;
import earth.terrarium.botarium.common.transfer.base.TransferUnit;
import earth.terrarium.botarium.common.transfer.impl.FluidUnit;
import net.fabricmc.fabric.api.lookup.v1.item.ItemApiLookup;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.TransferVariant;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;
import java.util.function.Supplier;

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
            if (storage instanceof FabricWrappedContainer<?, U, V, ?, ?> fabric) {
                return fabric.getContainer();
            }
            return new CommonWrappedContainer<>(storage, toVariant, toUnit);
        }
        return null;
    }

    @Override
    public void registerItems(ItemGetter<UnitContainer<U>, ItemContext> getter, Supplier<Item>... items) {
        for (Supplier<Item> item : items) {
            fabricLookup.registerForItems((stack, context) -> {
                UnitContainer<U> container = getter.getContainer(stack, new CommonItemContext(context));
                if (container instanceof UpdateManager<?> updateManager) {
                    return new FabricWrappedContainer<>(container, updateManager, toVariant, toUnit);
                }
                return null;
            }, item.get());
        }
    }
}
