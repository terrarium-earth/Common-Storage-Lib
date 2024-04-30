package earth.terrarium.botarium.item.lookup;

import earth.terrarium.botarium.resource.item.ItemResource;
import earth.terrarium.botarium.item.wrappers.CommonItemContainer;
import earth.terrarium.botarium.item.wrappers.NeoItemHandler;
import earth.terrarium.botarium.lookup.RegistryEventListener;
import earth.terrarium.botarium.lookup.EntityLookup;
import earth.terrarium.botarium.storage.base.CommonStorage;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.EntityCapability;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public final class ItemEntityLookup<C> implements EntityLookup<CommonStorage<ItemResource>, C>, RegistryEventListener {
    public static final ItemEntityLookup<Void> INSTANCE = new ItemEntityLookup<>(Capabilities.ItemHandler.ENTITY);
    public static final ItemEntityLookup<Direction> AUTOMATION = new ItemEntityLookup<>(Capabilities.ItemHandler.ENTITY_AUTOMATION);

    private final List<Consumer<EntityRegistrar<CommonStorage<ItemResource>, C>>> registrars = new ArrayList<>();
    private final EntityCapability<IItemHandler, C> capability;

    private ItemEntityLookup(EntityCapability<IItemHandler, C> capability) {
        this.capability = capability;
        registerSelf();
    }

    @Override
    public @Nullable CommonStorage<ItemResource> find(Entity entity, C context) {
        IItemHandler handler = entity.getCapability(capability, context);
        if (handler instanceof NeoItemHandler(CommonStorage<ItemResource> container)) {
            return container;
        }

        return handler == null ? null : new CommonItemContainer(handler);
    }

    @Override
    public void onRegister(Consumer<EntityRegistrar<CommonStorage<ItemResource>, C>> registrar) {
        registrars.add(registrar);
    }

    @Override
    public void register(RegisterCapabilitiesEvent event) {
        registrars.forEach(registrar -> registrar.accept((getter, entityTypes) -> {
            for (EntityType<?> entityType : entityTypes) {
                event.registerEntity(capability, entityType, (entity, direction) -> {
                    CommonStorage<ItemResource> container = getter.getContainer(entity, direction);
                    return container == null ? null : new NeoItemHandler(container);
                });
            }
        }));
    }
}
