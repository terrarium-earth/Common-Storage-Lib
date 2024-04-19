package earth.terrarium.botarium.common.lookup.impl;

import earth.terrarium.botarium.common.lookup.CapabilityRegisterer;
import earth.terrarium.botarium.common.lookup.EntityLookup;
import earth.terrarium.botarium.common.lookup.ItemLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.EntityCapability;
import net.neoforged.neoforge.capabilities.ItemCapability;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class NeoEntityLookup<T, C> implements EntityLookup<T, C>, CapabilityRegisterer {
    private final List<Consumer<EntityRegistrar<T, C>>> registrars = new ArrayList<>();
    private final EntityCapability<T, C> capability;

    public NeoEntityLookup(EntityCapability<T, C> capability) {
        this.capability = capability;
    }

    public NeoEntityLookup(ResourceLocation id, Class<T> type, Class<C> contextType) {
        this(EntityCapability.create(id, type, contextType));
    }

    @Override
    public @Nullable T find(Entity entity, C context) {
        return entity.getCapability(capability, context);
    }

    @Override
    public void onRegister(Consumer<EntityRegistrar<T, C>> registrar) {
        registrars.add(registrar);
    }

    @Override
    public void register(RegisterCapabilitiesEvent event) {
        registrars.forEach(registrar -> registrar.accept((getter, types) -> {
            for (EntityType<?> type : types) {
                event.registerEntity(capability, type, getter::getContainer);
            }
        }));
    }
}
