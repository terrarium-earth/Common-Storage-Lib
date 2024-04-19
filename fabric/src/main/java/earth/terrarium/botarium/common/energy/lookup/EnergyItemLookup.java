package earth.terrarium.botarium.common.energy.lookup;

import earth.terrarium.botarium.common.context.ItemContext;
import earth.terrarium.botarium.common.lookup.ItemLookup;
import earth.terrarium.botarium.common.storage.base.LongContainer;
import earth.terrarium.botarium.common.storage.base.UnitContainer;
import earth.terrarium.botarium.common.storage.common.CommonLongContainer;
import earth.terrarium.botarium.common.storage.common.CommonWrappedContainer;
import earth.terrarium.botarium.common.storage.context.CommonItemContext;
import earth.terrarium.botarium.common.storage.context.FabricItemContext;
import earth.terrarium.botarium.common.storage.fabric.FabricLongStorage;
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
import team.reborn.energy.api.EnergyStorage;

import java.util.function.Function;
import java.util.function.Supplier;

public class EnergyItemLookup implements ItemLookup<LongContainer, ItemContext> {

    @Override
    public @Nullable LongContainer find(ItemStack stack, ItemContext context) {
        EnergyStorage storage = EnergyStorage.ITEM.find(stack, new FabricItemContext(context));
        if (storage == null) {
            return null;
        }
        if (storage instanceof FabricLongStorage<?> fabric) {
            return fabric.getContainer();
        }
        return new CommonLongContainer(storage);
    }

    @Override
    public void registerItems(ItemGetter<LongContainer, ItemContext> getter, Supplier<Item>... containers) {
        for (Supplier<Item> item : containers) {
            EnergyStorage.ITEM.registerForItems((stack, context) -> {
                LongContainer container = getter.getContainer(stack, new CommonItemContext(context));
                if (container instanceof UpdateManager<?> updateManager) {
                    return new FabricLongStorage<>(container, updateManager);
                }
                return null;
            }, item.get());
        }
    }
}
