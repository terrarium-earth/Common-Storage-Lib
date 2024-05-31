package earth.terrarium.botarium.fluid.lookup;

import earth.terrarium.botarium.Botarium;
import earth.terrarium.botarium.resources.fluid.FluidResource;
import earth.terrarium.botarium.fluid.wrappers.CommonFluidContainer;
import earth.terrarium.botarium.fluid.wrappers.NeoFluidContainer;
import earth.terrarium.botarium.lookup.RegistryEventListener;
import earth.terrarium.botarium.lookup.EntityLookup;
import earth.terrarium.botarium.storage.base.CommonStorage;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public final class FluidEntityLookup implements EntityLookup<CommonStorage<FluidResource>, Direction>, RegistryEventListener<Entity> {
    public static final FluidEntityLookup INSTANCE = new FluidEntityLookup();
    public static final ResourceLocation NAME = new ResourceLocation(Botarium.MOD_ID, "fluid_entity");
    private final List<Consumer<EntityRegistrar<CommonStorage<FluidResource>, Direction>>> registrars = new ArrayList<>();

    private FluidEntityLookup() {
        RegistryEventListener.registerEntity(this);
    }

    @Override
    public @Nullable CommonStorage<FluidResource> find(Entity entity, Direction context) {
        LazyOptional<IFluidHandler> storage = entity.getCapability(ForgeCapabilities.FLUID_HANDLER, context);
        if (storage.isPresent()) {
            IFluidHandler fluidStorage = storage.orElseThrow(IllegalStateException::new);
            if (fluidStorage instanceof NeoFluidContainer(CommonStorage<FluidResource> container)) {
                return container;
            }
            return new CommonFluidContainer(fluidStorage);
        }
        return null;
    }

    @Override
    public void onRegister(Consumer<EntityRegistrar<CommonStorage<FluidResource>, Direction>> registrar) {
        registrars.add(registrar);
    }

    @Override
    public void register(AttachCapabilitiesEvent<Entity> event) {
        registrars.forEach(registrar -> registrar.accept((getter, blockEntityTypes) -> {
            for (EntityType<?> entityType : blockEntityTypes) {
                if (entityType == event.getObject().getType()) {
                    event.addCapability(NAME, new FluidCap(getter, event.getObject()));
                    return;
                }
            }
        }));
    }

    public static class FluidCap implements ICapabilityProvider {
        private final EntityGetter<CommonStorage<FluidResource>, Direction> getter;
        private final Entity entity;

        public FluidCap(EntityGetter<CommonStorage<FluidResource>, Direction> getter, Entity entity) {
            this.getter = getter;
            this.entity = entity;
        }

        @Override
        public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction arg) {
            CommonStorage<FluidResource> storage = getter.getContainer(entity, arg);
            LazyOptional<IFluidHandler> of = LazyOptional.of(() -> new NeoFluidContainer(storage));
            return capability.orEmpty(ForgeCapabilities.ENERGY, of.cast()).cast();
        }
    }
}
