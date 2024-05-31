package earth.terrarium.common_storage_lib.lookup.impl;

import earth.terrarium.common_storage_lib.lookup.ItemLookup;
import earth.terrarium.common_storage_lib.lookup.RegistryEventListener;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class NeoItemLookup<T, C> implements ItemLookup<T, C>, RegistryEventListener<ItemStack> {
    private final List<Consumer<ItemRegistrar<T, C>>> registrars = new ArrayList<>();
    private final Capability<ItemGetter<T, C>> capability;
    private final ResourceLocation id;

    public NeoItemLookup(ResourceLocation id) {
        this.id = id;
        this.capability = CapabilityManager.get(new CapabilityToken<>(){});
    }

    @Override
    public @Nullable T find(ItemStack stack, C context) {
        return stack.getCapability(capability).map(getter -> getter.getContainer(stack, context)).orElse(null);
    }

    @Override
    public void onRegister(Consumer<ItemRegistrar<T, C>> registrar) {
        registrars.add(registrar);
    }

    @Override
    public void register(AttachCapabilitiesEvent<ItemStack> event) {
        registrars.forEach(registrar -> registrar.accept((getter, itemStacks) -> {
            for (Item item : itemStacks) {
                if (item == event.getObject().getItem()) {
                    event.addCapability(id, new ItemCapability(getter));
                    return;
                }
            }
        }));
    }

    public class ItemCapability implements ICapabilityProvider {
        private final ItemGetter<T, C> getter;

        public ItemCapability(ItemGetter<T, C> getter) {
            this.getter = getter;
        }

        @Override
        public @NotNull <X> LazyOptional<X> getCapability(@NotNull Capability<X> capability, @Nullable Direction arg) {
            return capability.orEmpty(NeoItemLookup.this.capability, LazyOptional.of(() -> getter).cast()).cast();
        }
    }
}
