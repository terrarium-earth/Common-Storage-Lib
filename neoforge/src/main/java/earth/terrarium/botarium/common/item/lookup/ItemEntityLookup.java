package earth.terrarium.botarium.common.item.lookup;

import earth.terrarium.botarium.common.item.wrappers.CommonItemContainer;
import earth.terrarium.botarium.common.item.wrappers.NeoItemHandler;
import earth.terrarium.botarium.common.lookup.CapabilityRegisterer;
import earth.terrarium.botarium.common.lookup.EntityLookup;
import earth.terrarium.botarium.common.storage.base.UnitContainer;
import earth.terrarium.botarium.common.transfer.impl.ItemUnit;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.capabilities.EntityCapability;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ItemEntityLookup<C> implements EntityLookup<UnitContainer<ItemUnit>, C>, CapabilityRegisterer {
    private final List<Consumer<EntityRegistrar<UnitContainer<ItemUnit>, C>>> registrars = new ArrayList<>();
    private final EntityCapability<IItemHandler, C> capability;

    public ItemEntityLookup(EntityCapability<IItemHandler, C> capability) {
        this.capability = capability;
    }

    @Override
    public @Nullable UnitContainer<ItemUnit> find(Entity entity, C context) {
        IItemHandler handler = entity.getCapability(capability, context);
        if (handler instanceof NeoItemHandler(UnitContainer<ItemUnit> container)) {
            return container;
        }

        return handler == null ? null : new CommonItemContainer(handler);
    }

    @Override
    public void onRegister(Consumer<EntityRegistrar<UnitContainer<ItemUnit>, C>> registrar) {
        registrars.add(registrar);
    }

    @Override
    public void register(RegisterCapabilitiesEvent event) {
        registrars.forEach(registrar -> registrar.accept((getter, entityTypes) -> {
            for (EntityType<?> entityType : entityTypes) {
                event.registerEntity(capability, entityType, (entity, direction) -> {
                    UnitContainer<ItemUnit> container = getter.getContainer(entity, direction);
                    return container == null ? null : new NeoItemHandler(container);
                });
            }
        }));
    }
}
