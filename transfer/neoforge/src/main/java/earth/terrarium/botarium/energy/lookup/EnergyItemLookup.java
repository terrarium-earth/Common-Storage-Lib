package earth.terrarium.botarium.energy.lookup;

import earth.terrarium.botarium.BotariumTransfer;
import earth.terrarium.botarium.context.ItemContext;
import earth.terrarium.botarium.context.impl.ModifyOnlyContext;
import earth.terrarium.botarium.energy.wrappers.CommonItemEnergyStorage;
import earth.terrarium.botarium.energy.wrappers.NeoEnergyContainer;
import earth.terrarium.botarium.lookup.RegistryEventListener;
import earth.terrarium.botarium.lookup.ItemLookup;
import earth.terrarium.botarium.storage.base.ValueStorage;
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
    private static final ItemCapability<ValueStorage, ItemContext> CAPABILITY = ItemCapability.create(new ResourceLocation(BotariumTransfer.MOD_ID, "energy_item"), ValueStorage.class, ItemContext.class);

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
