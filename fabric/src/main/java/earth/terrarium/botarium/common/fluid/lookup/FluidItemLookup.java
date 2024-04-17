package earth.terrarium.botarium.common.fluid.lookup;

import earth.terrarium.botarium.common.context.ItemContext;
import earth.terrarium.botarium.common.fluid.base.FluidContainer;
import earth.terrarium.botarium.common.lookup.ItemLookup;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class FluidItemLookup implements ItemLookup<FluidContainer, ItemContext> {
    @Override
    public @Nullable FluidContainer find(ItemStack stack, @Nullable ItemContext context) {
        return null;
    }

    @Override
    public void registerItems(ItemGetter<FluidContainer, ItemContext> getter, Supplier<Item>... containers) {
        for (Supplier<Item> container : containers) {
            FluidStorage.ITEM.registerForItems((itemStack, context) -> {
                FluidContainer container1 = getter.getContainer(itemStack, context);
                if (container1 != null) {
                    return container1;
                }
                return null;
            }, container.get()
        }
    }
}
