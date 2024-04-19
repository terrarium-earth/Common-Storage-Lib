package earth.terrarium.botarium.common.energy.lookup;

import earth.terrarium.botarium.common.energy.wrappers.CommonEnergyContainer;
import earth.terrarium.botarium.common.energy.wrappers.NeoEnergyContainer;
import earth.terrarium.botarium.common.lookup.CapabilityRegisterer;
import earth.terrarium.botarium.common.lookup.EntityLookup;
import earth.terrarium.botarium.common.storage.base.LongContainer;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.energy.IEnergyStorage;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class EnergyEntityLookup implements EntityLookup<LongContainer, Direction>, CapabilityRegisterer {
    List<Consumer<EntityRegistrar<LongContainer, Direction>>> registrars = new ArrayList<>();

    @Override
    public @Nullable LongContainer find(Entity entity, Direction context) {
        IEnergyStorage capability = entity.getCapability(Capabilities.EnergyStorage.ENTITY, context);
        if (capability instanceof NeoEnergyContainer(LongContainer container)) {
            return container;
        }
        return capability == null ? null : new CommonEnergyContainer(capability);
    }

    @Override
    public void onRegister(Consumer<EntityRegistrar<LongContainer, Direction>> registrar) {
        registrars.add(registrar);
    }

    public void register(RegisterCapabilitiesEvent event) {
        registrars.forEach(registrar -> registrar.accept((getter, containers) -> {
            for (EntityType<?> container : containers) {
                event.registerEntity(Capabilities.EnergyStorage.ENTITY, container, (entity, direction) -> {
                    LongContainer storage = getter.getContainer(entity, direction);
                    return storage == null ? null : new NeoEnergyContainer(storage);
                });
            }
        }));
    }
}
