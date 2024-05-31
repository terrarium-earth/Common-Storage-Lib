package earth.terrarium.common_storage_lib.energy.lookup;

import earth.terrarium.common_storage_lib.CommonStorageLib;
import earth.terrarium.common_storage_lib.context.ItemContext;
import earth.terrarium.common_storage_lib.context.impl.ModifyOnlyContext;
import earth.terrarium.common_storage_lib.energy.wrappers.CommonItemEnergyStorage;
import earth.terrarium.common_storage_lib.energy.wrappers.NeoEnergyContainer;
import earth.terrarium.common_storage_lib.lookup.RegistryEventListener;
import earth.terrarium.common_storage_lib.lookup.ItemLookup;
import earth.terrarium.common_storage_lib.storage.base.ValueStorage;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public final class EnergyItemLookup implements ItemLookup<ValueStorage, ItemContext>, RegistryEventListener<ItemStack> {
    public static final EnergyItemLookup INSTANCE = new EnergyItemLookup();
    public static final ResourceLocation NAME = new ResourceLocation(CommonStorageLib.MOD_ID, "energy_item");
    public static final ResourceLocation SPECIFIC_NAME = new ResourceLocation(CommonStorageLib.MOD_ID, "common_energy_item");
    private static final Capability<ItemGetter<ValueStorage, ItemContext>> CAPABILITY = CapabilityManager.get(new CapabilityToken<>(){});

    private final List<Consumer<ItemRegistrar<ValueStorage, ItemContext>>> registrars = new ArrayList<>();

    private EnergyItemLookup() {
        RegistryEventListener.registerItem(this);
    }

    @Override
    public @Nullable ValueStorage find(ItemStack stack, ItemContext context) {
        LazyOptional<ItemGetter<ValueStorage, ItemContext>> commonCap = stack.getCapability(CAPABILITY);
        if (commonCap.isPresent()) {
            return commonCap.orElseThrow(IllegalStateException::new).getContainer(stack, context);
        }
        LazyOptional<IEnergyStorage> storage = stack.getCapability(ForgeCapabilities.ENERGY);
        if (storage.isPresent()) {
            IEnergyStorage energyStorage = storage.orElseThrow(IllegalStateException::new);
            return new CommonItemEnergyStorage(energyStorage, stack, context);
        }
        return null;
    }

    public void onRegister(Consumer<ItemRegistrar<ValueStorage, ItemContext>> registrar) {
        registrars.add(registrar);
    }

    @Override
    public void register(AttachCapabilitiesEvent<ItemStack> event) {
        registrars.forEach(registrar -> registrar.accept((getter, items) -> {
            for (Item entityType : items) {
                if (entityType == event.getObject().getItem()) {
                    event.addCapability(NAME, new EnergyCap(getter, event.getObject()));
                    event.addCapability(SPECIFIC_NAME, new CommonEnergyCap(getter));
                    return;
                }
            }
        }));
    }

    public static class EnergyCap implements ICapabilityProvider {
        private final ItemGetter<ValueStorage, ItemContext> getter;
        private final ItemStack stack;

        public EnergyCap(ItemGetter<ValueStorage, ItemContext> getter, ItemStack stack) {
            this.getter = getter;
            this.stack = stack;
        }

        @Override
        public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction arg) {
            ValueStorage storage = getter.getContainer(stack, new ModifyOnlyContext(stack));
            LazyOptional<IEnergyStorage> of = LazyOptional.of(() -> new NeoEnergyContainer(storage));
            return capability.orEmpty(ForgeCapabilities.ENERGY, of.cast()).cast();
        }
    }

    public static class CommonEnergyCap implements ICapabilityProvider {
        private final ItemGetter<ValueStorage, ItemContext> getter;

        public CommonEnergyCap(ItemGetter<ValueStorage, ItemContext> getter) {
            this.getter = getter;
        }

        @Override
        public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction arg) {
            LazyOptional<ItemGetter<ValueStorage, ItemContext>> of = LazyOptional.of(() -> getter);
            return capability.orEmpty(CAPABILITY, of.cast()).cast();
        }
    }
}
