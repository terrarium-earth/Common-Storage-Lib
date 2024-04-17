package earth.terrarium.botarium.common.lookup;

import earth.terrarium.botarium.common.context.ItemContext;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public interface ItemLookup<T, C> {

    static <T, C> ItemLookup<T, C> create(ResourceLocation name, Class<T> typeClass, Class<C> contextClass) {
        return null;
    }

    static <T> ItemLookup<T, ItemContext> create(ResourceLocation name, Class<T> typeClass) {
        return create(name, typeClass, ItemContext.class);
    }

    /**
     * @return The {@link T} for the block.
     */
    @Nullable
    T find(ItemStack stack, @Nullable C context);

    @SuppressWarnings("unchecked")
    void registerItems(ItemGetter<T, C> getter, Supplier<Item>... containers);

    interface ItemGetter<T, C> {
        T getContainer(ItemStack stack, @Nullable C context);
    }
}
