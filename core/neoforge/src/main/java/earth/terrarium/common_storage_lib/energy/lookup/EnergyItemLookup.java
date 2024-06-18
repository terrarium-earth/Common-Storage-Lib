package earth.terrarium.common_storage_lib.energy.lookup;

import earth.terrarium.common_storage_lib.CommonStorageLib;
import earth.terrarium.common_storage_lib.context.ItemContext;
import earth.terrarium.common_storage_lib.context.impl.ModifyOnlyContext;
import earth.terrarium.common_storage_lib.energy.wrappers.CommonItemEnergyStorage;
import earth.terrarium.common_storage_lib.energy.wrappers.NeoEnergyContainer;
import earth.terrarium.common_storage_lib.lookup.RegistryEventListener;
import earth.terrarium.common_storage_lib.lookup.ItemLookup;
import earth.terrarium.common_storage_lib.storage.base.ValueStorage;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.ItemCapability;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.energy.IEnergyStorage;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public final class EnergyItemLookup implements ItemLookup<ValueStorage, ItemContext>, RegistryEventListener {
    public static final EnergyItemLookup INSTANCE = new EnergyItemLookup();
    private static final ItemCapability<ValueStorage, ItemContext> CAPABILITY = ItemCapability.create(ResourceLocation.fromNamespaceAndPath(CommonStorageLib.MOD_ID, "energy_item"), ValueStorage.class, ItemContext.class);

    private final List<Consumer<ItemRegistrar<ValueStorage, ItemContext>>> registrars = new ArrayList<>();

    private EnergyItemLookup() {
        registerSelf();
    }

    @Override
    public @Nullable ValueStorage find(ItemStack stack, ItemContext context) {
        ValueStorage container = stack.getCapability(CAPABILITY, context);

        if (container != null) {
            return container;
        }

        IEnergyStorage storage = stack.getCapability(Capabilities.EnergyStorage.ITEM);
        return storage == null ? null : new CommonItemEnergyStorage(storage, stack, context);
    }

    public void onRegister(Consumer<ItemRegistrar<ValueStorage, ItemContext>> registrar) {
        registrars.add(registrar);
    }

    public void register(RegisterCapabilitiesEvent event) {
        registrars.forEach(registrar -> registrar.accept((getter, items) -> {
           event.registerItem(Capabilities.EnergyStorage.ITEM, (stack, ignored) -> {
               ValueStorage storage = getter.getContainer(stack, new ModifyOnlyContext(stack));
               return storage == null ? null : new NeoEnergyContainer(storage);
           }, items);

           event.registerItem(CAPABILITY, getter::getContainer, items);
        }));
    }
}
