package earth.terrarium.common_storage_lib.data.network;

import earth.terrarium.common_storage_lib.data.NeoDataLib;
import earth.terrarium.common_storage_lib.data.sync.AttachmentData;
import earth.terrarium.common_storage_lib.data.sync.DataSyncSerializer;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;

public record EntitySyncPacket(int entityId, AttachmentData<?> syncData) implements CustomPacketPayload {
    public static final Type<EntitySyncPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(NeoDataLib.MOD_ID, "entity"));
    public static final StreamCodec<RegistryFriendlyByteBuf, EntitySyncPacket> CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            EntitySyncPacket::entityId,
            NeoDataLib.SYNC_SERIALIZER_STREAM_CODEC,
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
