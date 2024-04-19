package earth.terrarium.botarium.common.fluid.lookup;

import earth.terrarium.botarium.common.fluid.wrappers.CommonFluidContainer;
import earth.terrarium.botarium.common.fluid.wrappers.NeoFluidContainer;
import earth.terrarium.botarium.common.lookup.CapabilityRegisterer;
import earth.terrarium.botarium.common.lookup.EntityLookup;
import earth.terrarium.botarium.common.storage.base.UnitContainer;
import earth.terrarium.botarium.common.transfer.impl.FluidUnit;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class FluidEntityLookup implements EntityLookup<UnitContainer<FluidUnit>, Direction>, CapabilityRegisterer {
    private final List<Consumer<EntityRegistrar<UnitContainer<FluidUnit>, Direction>>> registrars = new ArrayList<>();

    @Override
    public @Nullable UnitContainer<FluidUnit> find(Entity entity, Direction context) {
        IFluidHandler handler = entity.getCapability(Capabilities.FluidHandler.ENTITY, context);

        if (handler instanceof NeoFluidContainer(UnitContainer<FluidUnit> container)) {
            return container;
        }

        return handler == null ? null : new CommonFluidContainer(handler);
    }

    @Override
    public void onRegister(Consumer<EntityRegistrar<UnitContainer<FluidUnit>, Direction>> registrar) {
        registrars.add(registrar);
    }

    @Override
    public void register(RegisterCapabilitiesEvent event) {
        registrars.forEach(registrar -> registrar.accept((getter, containers) -> {
            for (var container : containers) {
                event.registerEntity(Capabilities.FluidHandler.ENTITY, container, (entity, direction) -> {
                    UnitContainer<FluidUnit> storage = getter.getContainer(entity, direction);
                    return storage == null ? null : new NeoFluidContainer(storage);
                });
            }
        }));
    }
}
