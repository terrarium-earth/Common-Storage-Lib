package earth.terrarium.common_storage_lib.data.network;

import earth.terrarium.common_storage_lib.data.NeoDataLib;
import earth.terrarium.common_storage_lib.data.sync.AttachmentData;
import earth.terrarium.common_storage_lib.data.sync.DataSyncSerializer;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public record LevelSyncPacket(AttachmentData<?> syncData) implements CustomPacketPayload {
    public static final Type<LevelSyncPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(NeoDataLib.MOD_ID, "level"));

    public static final StreamCodec<RegistryFriendlyByteBuf, LevelSyncPacket> CODEC = NeoDataLib.SYNC_SERIALIZER_STREAM_CODEC.map(LevelSyncPacket::new, LevelSyncPacket::syncData);

    public static <T> LevelSyncPacket of(DataSyncSerializer<T> serializer, @Nullable T data) {
        return new LevelSyncPacket(AttachmentData.of(serializer, data));
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
