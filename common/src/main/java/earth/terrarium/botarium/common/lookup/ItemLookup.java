package earth.terrarium.botarium.common.lookup;

import earth.terrarium.botarium.common.context.ItemContext;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.msrandom.multiplatform.annotations.Expect;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

public interface ItemLookup<T, C> {

    @Expect
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
    T find(ItemStack stack, C context);

    default void registerSelf(ItemGetter<T, C> getter, Item ... items) {
        onRegister(registrar -> registrar.register(getter, items));
    }

    default void registerFallback(ItemGetter<T, C> getter) {
        onRegister(registrar -> {
            for (Item item : BuiltInRegistries.ITEM) {
                registrar.register(getter, item);
            }
        });
    }

    default void registerFallback(ItemGetter<T, C> getter, Predicate<Item> itemPredicate) {
        onRegister(registrar -> {
            for (Item item : BuiltInRegistries.ITEM) {
                if (itemPredicate.test(item)) {
                    registrar.register(getter, item);
                }
            }
        });
    }

    void onRegister(Consumer<ItemRegistrar<T, C>> registrar);

    interface ItemGetter<T, C> {
        T getContainer(ItemStack stack, C context);
    }

    interface ItemRegistrar<T, C> {
        void register(ItemGetter<T, C> getter, Item... items);
    }
}
