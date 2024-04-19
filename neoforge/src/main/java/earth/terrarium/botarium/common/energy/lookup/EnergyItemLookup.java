package earth.terrarium.botarium.common.energy.lookup;

import earth.terrarium.botarium.Botarium;
import earth.terrarium.botarium.common.context.ItemContext;
import earth.terrarium.botarium.common.context.impl.ModifyOnlyContext;
import earth.terrarium.botarium.common.energy.wrappers.CommonEnergyContainer;
import earth.terrarium.botarium.common.energy.wrappers.CommonItemEnergyContainer;
import earth.terrarium.botarium.common.energy.wrappers.NeoEnergyContainer;
import earth.terrarium.botarium.common.lookup.BlockLookup;
import earth.terrarium.botarium.common.lookup.CapabilityRegisterer;
import earth.terrarium.botarium.common.lookup.ItemLookup;
import earth.terrarium.botarium.common.storage.base.LongContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.ItemCapability;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.energy.IEnergyStorage;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

public class EnergyItemLookup implements ItemLookup<LongContainer, ItemContext>, CapabilityRegisterer {
    public static final ItemCapability<LongContainer, ItemContext> CAPABILITY = ItemCapability.create(new ResourceLocation(Botarium.MOD_ID, "energy_item"), LongContainer.class, ItemContext.class);
    List<Consumer<ItemRegistrar<LongContainer, ItemContext>>> registrars;

    @Override
    public @Nullable LongContainer find(ItemStack stack, ItemContext context) {
        LongContainer container = stack.getCapability(CAPABILITY, context);

        if (container != null) {
            return container;
        }

        IEnergyStorage storage = stack.getCapability(Capabilities.EnergyStorage.ITEM);
        return storage == null ? null : new CommonItemEnergyContainer(storage, stack, context);
    }

    public void onRegister(Consumer<ItemRegistrar<LongContainer, ItemContext>> registrar) {
        registrars.add(registrar);
    }

    public void register(RegisterCapabilitiesEvent event) {
        registrars.forEach(registrar -> registrar.accept((getter, items) -> {
           event.registerItem(Capabilities.EnergyStorage.ITEM, (stack, ignored) -> {
               LongContainer storage = getter.getContainer(stack, new ModifyOnlyContext(stack));
               return storage == null ? null : new NeoEnergyContainer(storage);
           }, items);

           event.registerItem(CAPABILITY, getter::getContainer, items);
        }));
    }
}
