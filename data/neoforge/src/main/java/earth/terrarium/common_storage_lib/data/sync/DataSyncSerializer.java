package earth.terrarium.common_storage_lib.data.sync;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.attachment.AttachmentType;

import java.util.function.Supplier;

@SuppressWarnings("UnstableApiUsage")
public interface DataSyncSerializer<T> {
    AttachmentType<T> getAttachmentType();
    StreamCodec<? super RegistryFriendlyByteBuf, T> getCodec();

    static <T> DataSyncSerializer<T> create(Supplier<AttachmentType<T>> attachmentType, StreamCodec<? super RegistryFriendlyByteBuf, T> codec) {
        return new SimpleDataSyncSerializer<>(attachmentType, codec);
    }

    AttachmentData<T> decode(RegistryFriendlyByteBuf buf);

    record SimpleDataSyncSerializer<T>(Supplier<AttachmentType<T>> attachmentType, StreamCodec<? super RegistryFriendlyByteBuf, T> codec) implements DataSyncSerializer<T> {

        @Override
        public AttachmentType<T> getAttachmentType() {
            return attachmentType.get();
        }

        @Override
        public StreamCodec<? super RegistryFriendlyByteBuf, T> getCodec() {
            return codec;
        }

        @Override
        public AttachmentData<T> decode(RegistryFriendlyByteBuf buf) {
            if (buf.readBoolean()) {
                return AttachmentData.of(this, codec.decode(buf));
            } else {
                return AttachmentData.of(this, null);
            }
        }
    }
}
