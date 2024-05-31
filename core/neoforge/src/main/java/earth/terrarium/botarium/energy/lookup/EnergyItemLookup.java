package earth.terrarium.botarium.energy.lookup;

import earth.terrarium.botarium.Botarium;
import earth.terrarium.botarium.context.ItemContext;
import earth.terrarium.botarium.context.impl.IsolatedSlotContext;
import earth.terrarium.botarium.context.impl.ModifyOnlyContext;
import earth.terrarium.botarium.energy.wrappers.CommonItemEnergyStorage;
import earth.terrarium.botarium.energy.wrappers.NeoEnergyContainer;
import earth.terrarium.botarium.lookup.RegistryEventListener;
import earth.terrarium.botarium.lookup.ItemLookup;
import earth.terrarium.botarium.storage.base.ValueStorage;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public final class EnergyItemLookup implements ItemLookup<ValueStorage, ItemContext>, RegistryEventListener<ItemStack> {
    public static final EnergyItemLookup INSTANCE = new EnergyItemLookup();
    public static final ResourceLocation NAME = new ResourceLocation(Botarium.MOD_ID, "energy_item");
    public static final ResourceLocation BOTARIUM_SPECIFIC_NAME = new ResourceLocation(Botarium.MOD_ID, "botarium_energy_item");
    private static final Capability<ItemGetter<ValueStorage, ItemContext>> CAPABILITY = CapabilityManager.get(new CapabilityToken<>(){});

    private final List<Consumer<ItemRegistrar<ValueStorage, ItemContext>>> registrars = new ArrayList<>();

    private EnergyItemLookup() {
        registerItem(this);
    }

    @Override
    public @Nullable ValueStorage find(ItemStack stack, ItemContext context) {
        LazyOptional<ItemGetter<ValueStorage, ItemContext>> botariumCap = stack.getCapability(CAPABILITY);
        if (botariumCap.isPresent()) {
            return botariumCap.orElseThrow(IllegalStateException::new).getContainer(stack, context);
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
                    event.addCapability(BOTARIUM_SPECIFIC_NAME, new BotariumEnergyCap(getter));
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

    public static class BotariumEnergyCap implements ICapabilityProvider {
        private final ItemGetter<ValueStorage, ItemContext> getter;

        public BotariumEnergyCap(ItemGetter<ValueStorage, ItemContext> getter) {
            this.getter = getter;
        }

        @Override
        public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction arg) {
            LazyOptional<ItemGetter<ValueStorage, ItemContext>> of = LazyOptional.of(() -> getter);
            return capability.orEmpty(CAPABILITY, of.cast()).cast();
        }
    }
}
