package earth.terrarium.botarium.fluid.lookup;

import earth.terrarium.botarium.resources.fluid.FluidResource;
import earth.terrarium.botarium.fluid.wrappers.CommonFluidContainer;
import earth.terrarium.botarium.fluid.wrappers.NeoFluidContainer;
import earth.terrarium.botarium.lookup.RegistryEventListener;
import earth.terrarium.botarium.lookup.EntityLookup;
import earth.terrarium.botarium.storage.base.CommonStorage;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public final class FluidEntityLookup implements EntityLookup<CommonStorage<FluidResource>, Direction>, RegistryEventListener {
    public static final FluidEntityLookup INSTANCE = new FluidEntityLookup();
    private final List<Consumer<EntityRegistrar<CommonStorage<FluidResource>, Direction>>> registrars = new ArrayList<>();

    private FluidEntityLookup() {
        registerSelf();
    }

    @Override
    public @Nullable CommonStorage<FluidResource> find(Entity entity, Direction context) {
        IFluidHandler handler = entity.getCapability(Capabilities.FluidHandler.ENTITY, context);

        if (handler instanceof NeoFluidContainer(CommonStorage<FluidResource> container)) {
            return container;
        }

        return handler == null ? null : new CommonFluidContainer(handler);
    }

    @Override
    public void onRegister(Consumer<EntityRegistrar<CommonStorage<FluidResource>, Direction>> registrar) {
        registrars.add(registrar);
    }

    @Override
    public void register(RegisterCapabilitiesEvent event) {
        registrars.forEach(registrar -> registrar.accept((getter, containers) -> {
            for (var container : containers) {
                event.registerEntity(Capabilities.FluidHandler.ENTITY, container, (entity, direction) -> {
                    CommonStorage<FluidResource> storage = getter.getContainer(entity, direction);
                    return storage == null ? null : new NeoFluidContainer(storage);
                });
            }
        }));
    }
}
