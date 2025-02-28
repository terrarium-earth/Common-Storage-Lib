package earth.terrarium.common_storage_lib.lookup.impl;

import earth.terrarium.common_storage_lib.lookup.ItemLookup;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.lookup.v1.item.ItemApiLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class FabricItemLookup<T, C> implements ItemLookup<T, C> {
    private final ItemApiLookup<T, C> lookup;

    public FabricItemLookup(ItemApiLookup<T, C> lookup) {
        this.lookup = lookup;
    }

    public FabricItemLookup(ResourceLocation id, Class<T> type, Class<C> contextType) {
        this(ItemApiLookup.get(id, type, contextType));
    }

    @Override
    public @Nullable T find(ItemStack stack, C context) {
        return lookup.find(stack, context);
    }

    @Override
    public void onRegister(Consumer<ItemRegistrar<T, C>> registrar) {
        registrar.accept((getter, containers) -> lookup.registerForItems(getter::getContainer, containers));
    }

    @Override
    public void registerFallback(ItemGetter<T, C> getter) {
        lookup.registerFallback(getter::getContainer);
    }

    @Override
    public void registerFallback(ItemGetter<T, C> getter, Predicate<Item> itemPredicate) {
        ServerLifecycleEvents.SERVER_STARTED.register(ignored -> ItemLookup.super.registerFallback(getter, itemPredicate));
    }
}
