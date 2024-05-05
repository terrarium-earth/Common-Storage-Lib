package earth.terrarium.botarium.data;

import earth.terrarium.botarium.data.network.BlockEntitySyncAllPacket;
import earth.terrarium.botarium.data.network.EntitySyncAllPacket;
import earth.terrarium.botarium.data.network.EntitySyncPacket;
import earth.terrarium.botarium.data.sync.AttachmentData;
import earth.terrarium.botarium.data.network.BlockEntitySyncPacket;
import earth.terrarium.botarium.data.sync.DataSyncSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.networking.v1.EntityTrackingEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.Registry;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public class BotariumData implements ModInitializer {
    public static final ResourceKey<Registry<DataSyncSerializer<?>>> SYNC_SERIALIZERS_KEY = ResourceKey.createRegistryKey(new ResourceLocation("botarium", "sync_serializers"));
    public static final Registry<DataSyncSerializer<?>> SYNC_SERIALIZERS = FabricRegistryBuilder.createSimple(SYNC_SERIALIZERS_KEY).buildAndRegister();
    public static StreamCodec<RegistryFriendlyByteBuf, AttachmentData<?>> SYNC_SERIALIZER_STREAM_CODEC = new StreamCodec<>() {
        @Override
        public void encode(RegistryFriendlyByteBuf object, AttachmentData<?> object2) {
            object2.encode(object);
        }

        @Override
        public AttachmentData<?> decode(RegistryFriendlyByteBuf object) {
            ResourceLocation key = object.readResourceLocation();
            DataSyncSerializer<?> serializer = SYNC_SERIALIZERS.get(key);
            if (serializer == null) {
                throw new IllegalStateException("Unknown sync serializer: " + key);
            }
            return serializer.decode(object);
        }
    };

    @Override
    public void onInitialize() {
        PayloadTypeRegistry.playS2C().register(BlockEntitySyncPacket.TYPE, BlockEntitySyncPacket.CODEC);
        PayloadTypeRegistry.playS2C().register(EntitySyncPacket.TYPE, EntitySyncPacket.CODEC);
        PayloadTypeRegistry.playS2C().register(BlockEntitySyncAllPacket.TYPE, BlockEntitySyncAllPacket.CODEC);
        PayloadTypeRegistry.playS2C().register(EntitySyncAllPacket.TYPE, EntitySyncAllPacket.CODEC);

        EntityTrackingEvents.START_TRACKING.register((entity, player) -> {
            EntitySyncAllPacket entitySyncAllPacket = EntitySyncAllPacket.of(entity);
            ServerPlayNetworking.send(player, entitySyncAllPacket);
        });

    }
}
