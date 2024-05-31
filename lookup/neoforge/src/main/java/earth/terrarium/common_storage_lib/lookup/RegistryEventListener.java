package earth.terrarium.common_storage_lib.lookup;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.IEventBus;

import java.util.ArrayList;
import java.util.List;

public interface RegistryEventListener<T> {
    List<RegistryEventListener<BlockEntity>> BLOCK_REGISTRARS = new ArrayList<>();
    List<RegistryEventListener<Entity>> ENTITY_REGISTRARS = new ArrayList<>();
    List<RegistryEventListener<ItemStack>> ITEM_REGISTRARS = new ArrayList<>();

    void register(AttachCapabilitiesEvent<T> event);

    static void registerAllBlocks(AttachCapabilitiesEvent<BlockEntity> event) {
        BLOCK_REGISTRARS.forEach(listener -> listener.register(event));
    }

    static void registerAllEntities(AttachCapabilitiesEvent<Entity> event) {
        ENTITY_REGISTRARS.forEach(listener -> listener.register(event));
    }

    static void registerAllItems(AttachCapabilitiesEvent<ItemStack> event) {
        ITEM_REGISTRARS.forEach(listener -> listener.register(event));
    }

    static void registerAll(IEventBus bus) {
        bus.addGenericListener(BlockEntity.class, (AttachCapabilitiesEvent<BlockEntity> event) -> RegistryEventListener.registerAllBlocks(event));
        bus.addGenericListener(Entity.class, (AttachCapabilitiesEvent<Entity> event) -> RegistryEventListener.registerAllEntities(event));
        bus.addGenericListener(ItemStack.class, (AttachCapabilitiesEvent<ItemStack> event) -> RegistryEventListener.registerAllItems(event));
    }

    static void registerBlock(RegistryEventListener<BlockEntity> listener) {
        BLOCK_REGISTRARS.add(listener);
    }

    static void registerEntity(RegistryEventListener<Entity> listener) {
        ENTITY_REGISTRARS.add(listener);
    }

    static void registerItem(RegistryEventListener<ItemStack> listener) {
        ITEM_REGISTRARS.add(listener);
    }
}
