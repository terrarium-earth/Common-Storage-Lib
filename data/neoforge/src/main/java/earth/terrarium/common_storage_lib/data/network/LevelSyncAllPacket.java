package earth.terrarium.common_storage_lib.data.network;

import earth.terrarium.common_storage_lib.data.NeoDataLib;
import earth.terrarium.common_storage_lib.data.sync.AttachmentData;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;

import java.util.ArrayList;
import java.util.List;

public record LevelSyncAllPacket(List<AttachmentData<?>> syncData) implements CustomPacketPayload {
    public static final Type<LevelSyncAllPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(NeoDataLib.MOD_ID, "level_all"));

    public static final StreamCodec<RegistryFriendlyByteBuf, LevelSyncAllPacket> CODEC = ByteBufCodecs.collection(ArrayList::new, NeoDataLib.SYNC_SERIALIZER_STREAM_CODEC)
            .map(LevelSyncAllPacket::new, levelSyncAllPacket -> new ArrayList<>(levelSyncAllPacket.syncData));

    public static LevelSyncAllPacket of(ServerLevel level) {
        return new LevelSyncAllPacket(AttachmentData.getAllSyncData(level));
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
