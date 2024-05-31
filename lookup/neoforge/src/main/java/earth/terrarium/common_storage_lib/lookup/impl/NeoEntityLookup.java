package earth.terrarium.common_storage_lib.lookup.impl;

import earth.terrarium.common_storage_lib.lookup.EntityLookup;
import earth.terrarium.common_storage_lib.lookup.RegistryEventListener;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
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

public class NeoEntityLookup<T, C> implements EntityLookup<T, C>, RegistryEventListener<Entity> {
    private final List<Consumer<EntityRegistrar<T, C>>> registrars = new ArrayList<>();
    private final ResourceLocation id;
    private final Capability<EntityGetter<T, C>> capability;

    public NeoEntityLookup(ResourceLocation id) {
        this.id = id;
        this.capability = CapabilityManager.get(new CapabilityToken<>(){});
    }

    @Override
    public @Nullable T find(Entity entity, C context) {
        return entity.getCapability(capability).map(getter -> getter.getContainer(entity, context)).orElse(null);
    }

    @Override
    public void onRegister(Consumer<EntityRegistrar<T, C>> registrar) {
        registrars.add(registrar);
    }

    @Override
    public void register(AttachCapabilitiesEvent<Entity> event) {
        registrars.forEach(registrar -> registrar.accept((getter, entityTypes) -> {
            for (EntityType<?> entityType : entityTypes) {
                if (entityType == event.getObject().getType()) {
                    event.addCapability(id, new EntityCapability(getter));
                    return;
                }
            }
        }));
    }

    public class EntityCapability implements ICapabilityProvider {
        private final EntityGetter<T, C> getter;

        public EntityCapability(EntityGetter<T, C> getter) {
            this.getter = getter;
        }

        @Override
        public @NotNull <X> LazyOptional<X> getCapability(@NotNull Capability<X> capability, @Nullable Direction arg) {
            return capability.orEmpty(NeoEntityLookup.this.capability, LazyOptional.of(() -> getter).cast()).cast();
        }
    }
}
