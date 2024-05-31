package earth.terrarium.common_storage_lib.item.lookup;

import earth.terrarium.common_storage_lib.CommonStorageLib;
import earth.terrarium.common_storage_lib.item.wrappers.CommonItemContainer;
import earth.terrarium.common_storage_lib.item.wrappers.NeoItemHandler;
import earth.terrarium.common_storage_lib.lookup.EntityLookup;
import earth.terrarium.common_storage_lib.lookup.RegistryEventListener;
import earth.terrarium.common_storage_lib.resources.item.ItemResource;
import earth.terrarium.common_storage_lib.storage.base.CommonStorage;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public final class ItemEntityLookup implements EntityLookup<CommonStorage<ItemResource>, Direction>, RegistryEventListener<Entity> {
    public static final ItemEntityLookup INSTANCE = new ItemEntityLookup();
    public static final ResourceLocation NAME = new ResourceLocation(CommonStorageLib.MOD_ID, "item_entity");

    private final List<Consumer<EntityRegistrar<CommonStorage<ItemResource>, Direction>>> registrars = new ArrayList<>();

    private ItemEntityLookup() {
        RegistryEventListener.registerEntity(this);
    }

    @Override
    public @Nullable CommonStorage<ItemResource> find(Entity entity, Direction context) {
        LazyOptional<IItemHandler> storage = entity.getCapability(ForgeCapabilities.ITEM_HANDLER, context);
        if (storage.isPresent()) {
            IItemHandler itemStorage = storage.orElseThrow(IllegalStateException::new);
            if (itemStorage instanceof NeoItemHandler(CommonStorage<ItemResource> container)) {
                return container;
            }
            return new CommonItemContainer(itemStorage);
        }
        return null;
    }

    @Override
    public void onRegister(Consumer<EntityRegistrar<CommonStorage<ItemResource>, Direction>> registrar) {
        registrars.add(registrar);
    }


    @Override
    public void register(AttachCapabilitiesEvent<Entity> event) {
        registrars.forEach(registrar -> registrar.accept((getter, entityTypes) -> {
            for (EntityType<?> entityType : entityTypes) {
                if (entityType == event.getObject().getType()) {
                    event.addCapability(NAME, new ItemCap(getter, event.getObject()));
                    return;
                }
            }
        }));
    }

    public static class ItemCap implements ICapabilityProvider {
        private final EntityGetter<CommonStorage<ItemResource>, Direction> getter;
        private final Entity entity;

        public ItemCap(EntityGetter<CommonStorage<ItemResource>, Direction> getter, Entity entity) {
            this.getter = getter;
            this.entity = entity;
        }

        @Override
        public <T> LazyOptional<T> getCapability(net.minecraftforge.common.capabilities.Capability<T> cap, @Nullable Direction side) {
            CommonStorage<ItemResource> container = getter.getContainer(entity, side);
            LazyOptional<IItemHandler> optional = LazyOptional.of(() -> new NeoItemHandler(container));
            return cap.orEmpty(ForgeCapabilities.ITEM_HANDLER, optional.cast()).cast();
        }
    }
}
