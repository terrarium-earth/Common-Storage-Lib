package earth.terrarium.botarium.common.generic.base;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public interface ItemContainerLookup<T, C> {

    /**
     * @return The {@link T} for the block.
     */
    T getContainer(ItemStack stack, @Nullable C context);

    void registerItems(ItemGetter<T, C> getter, Supplier<Item>... containers);

    interface ItemGetter<T, C> {
        T getContainer(ItemStack stack, @Nullable C context);
    }
}
