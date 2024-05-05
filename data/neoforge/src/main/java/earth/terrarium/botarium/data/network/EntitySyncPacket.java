package earth.terrarium.botarium.data.network;

import earth.terrarium.botarium.data.BotariumData;
import earth.terrarium.botarium.data.sync.AttachmentData;
import earth.terrarium.botarium.data.sync.DataSyncSerializer;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;

public record EntitySyncPacket(int entityId, AttachmentData<?> syncData) implements CustomPacketPayload {
    public static final Type<EntitySyncPacket> TYPE = new Type<>(new ResourceLocation("botarium_data", "entity"));
    public static final StreamCodec<RegistryFriendlyByteBuf, EntitySyncPacket> CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            EntitySyncPacket::entityId,
            BotariumData.SYNC_SERIALIZER_STREAM_CODEC,
            EntitySyncPacket::syncData,
            EntitySyncPacket::new
    );

    public static <T> EntitySyncPacket of(Entity entity, DataSyncSerializer<T> serializer, @Nullable T data) {
        return new EntitySyncPacket(entity.getId(), AttachmentData.of(serializer, data));
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
