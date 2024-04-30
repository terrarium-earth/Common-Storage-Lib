package earth.terrarium.botarium.data.sync;

import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.attachment.v1.AttachmentTarget;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

@SuppressWarnings("UnstableApiUsage")
public interface DataSyncSerializer<T> {
    AttachmentType<T> getAttachmentType();
    StreamCodec<? super RegistryFriendlyByteBuf, T> getCodec();

    static <T> DataSyncSerializer<T> create(AttachmentType<T> attachmentType, StreamCodec<? super RegistryFriendlyByteBuf, T> codec) {
        return new SimpleDataSyncSerializer<>(attachmentType, codec);
    }

    static <T> DataSyncSerializer<T> create(AttachmentType<T> attachmentType) {
        Codec<T> codec = attachmentType.persistenceCodec();
        if (codec == null) {
            throw new IllegalArgumentException("Cannot create a DataSyncSerializer for an AttachmentType without a persistence codec");
        }
        return new SimpleDataSyncSerializer<>(attachmentType, ByteBufCodecs.fromCodecWithRegistries(codec));
    }

    record SimpleDataSyncSerializer<T>(AttachmentType<T> attachmentType, StreamCodec<? super RegistryFriendlyByteBuf, T> codec) implements DataSyncSerializer<T> {
        @Override
        public AttachmentType<T> getAttachmentType() {
            return attachmentType;
        }

        @Override
        public StreamCodec<? super RegistryFriendlyByteBuf, T> getCodec() {
            return codec;
        }
    }
}
