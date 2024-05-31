package earth.terrarium.botarium.energy.lookup;

import earth.terrarium.botarium.Botarium;
import earth.terrarium.botarium.energy.wrappers.CommonEnergyStorage;
import earth.terrarium.botarium.energy.wrappers.NeoEnergyContainer;
import earth.terrarium.botarium.lookup.RegistryEventListener;
import earth.terrarium.botarium.lookup.EntityLookup;
import earth.terrarium.botarium.storage.base.ValueStorage;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public final class EnergyEntityLookup implements EntityLookup<ValueStorage, Direction>, RegistryEventListener<Entity> {
    public static final EnergyEntityLookup INSTANCE = new EnergyEntityLookup();
    public static final ResourceLocation NAME = new ResourceLocation(Botarium.MOD_ID, "energy_entity");
    private final List<Consumer<EntityLookup.EntityRegistrar<ValueStorage, Direction>>> registrars = new ArrayList<>();

    private EnergyEntityLookup() {
        RegistryEventListener.registerEntity(this);
    }

    @Override
    public @Nullable ValueStorage find(Entity entity, Direction context) {
        LazyOptional<IEnergyStorage> storage = entity.getCapability(ForgeCapabilities.ENERGY, context);
        if (storage.isPresent()) {
            IEnergyStorage energyStorage = storage.orElseThrow(IllegalStateException::new);
            if (energyStorage instanceof NeoEnergyContainer(ValueStorage container)) {
                return container;
            }
            return new CommonEnergyStorage(energyStorage);
        }
        return null;
    }

    @Override
    public void onRegister(Consumer<EntityRegistrar<ValueStorage, Direction>> registrar) {
        registrars.add(registrar);
    }

    @Override
    public void register(AttachCapabilitiesEvent<Entity> event) {
        registrars.forEach(registrar -> registrar.accept((getter, blockEntityTypes) -> {
            for (EntityType<?> entityType : blockEntityTypes) {
                if (entityType == event.getObject().getType()) {
                    event.addCapability(NAME, new EnergyCap(getter, event.getObject()));
                    return;
                }
            }
        }));
    }

    public static class EnergyCap implements ICapabilityProvider {
        private final EntityGetter<ValueStorage, Direction> getter;
        private final Entity entity;

        public EnergyCap(EntityGetter<ValueStorage, Direction> getter, Entity entity) {
            this.getter = getter;
            this.entity = entity;
        }

        @Override
        public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction arg) {
            ValueStorage storage = getter.getContainer(entity, arg);
            LazyOptional<IEnergyStorage> of = LazyOptional.of(() -> new NeoEnergyContainer(storage));
            return capability.orEmpty(ForgeCapabilities.ENERGY, of.cast()).cast();
        }
    }
}
