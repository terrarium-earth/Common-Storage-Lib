package earth.terrarium.common_storage_lib.data;

import earth.terrarium.common_storage_lib.data.network.BlockEntitySyncAllPacket;
import earth.terrarium.common_storage_lib.data.network.BlockEntitySyncPacket;
import earth.terrarium.common_storage_lib.data.network.EntitySyncAllPacket;
import earth.terrarium.common_storage_lib.data.network.EntitySyncPacket;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import java.util.Optional;

public class FabricDataLibClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(BlockEntitySyncPacket.TYPE, (payload, context) -> {
            context.player().level().getBlockEntity(payload.pos(), payload.blockEntityType()).ifPresent(entity -> {
                payload.syncData().updateTarget(entity);
            });
        });

        ClientPlayNetworking.registerGlobalReceiver(EntitySyncPacket.TYPE, (payload, context) -> {
            Optional.ofNullable(context.player().level().getEntity(payload.entityId())).ifPresent(entity -> {
                payload.syncData().updateTarget(entity);
            });
        });

        ClientPlayNetworking.registerGlobalReceiver(EntitySyncAllPacket.TYPE, (payload, context) -> {
            Optional.ofNullable(context.player().level().getEntity(payload.entityId())).ifPresent(entity -> {
                payload.syncData().forEach(data -> data.updateTarget(entity));
            });
        });

        ClientPlayNetworking.registerGlobalReceiver(BlockEntitySyncAllPacket.TYPE, (payload, context) -> {
            context.player().level().getBlockEntity(payload.pos(), payload.blockEntityType()).ifPresent(entity -> {
                payload.syncData().forEach(data -> data.updateTarget(entity));
            });
        });
    }
}
