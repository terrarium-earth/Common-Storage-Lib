package earth.terrarium.botarium.data;

import earth.terrarium.botarium.data.network.BlockEntitySyncAllPacket;
import earth.terrarium.botarium.data.network.BlockEntitySyncPacket;
import earth.terrarium.botarium.data.network.EntitySyncAllPacket;
import earth.terrarium.botarium.data.network.EntitySyncPacket;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import java.util.Optional;

public class BotariumDataClient implements ClientModInitializer {
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
