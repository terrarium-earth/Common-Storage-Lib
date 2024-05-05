package earth.terrarium.botarium.data.sync;

import earth.terrarium.botarium.data.BotariumData;
import net.fabricmc.fabric.api.attachment.v1.AttachmentTarget;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("UnstableApiUsage")
public record AttachmentData<T>(DataSyncSerializer<T> serializer, @Nullable T data) {
    public static <T> AttachmentData<T> of(DataSyncSerializer<T> serializer, T data) {
        return new AttachmentData<>(serializer, data);
    }

    public static <T> AttachmentData<T> of(AttachmentTarget target, DataSyncSerializer<T> serializer) {
        return new AttachmentData<>(serializer, target.getAttached(serializer.getAttachmentType()));
    }

    public void encode(RegistryFriendlyByteBuf buf) {
        ResourceLocation key = BotariumData.SYNC_SERIALIZERS.getKey(serializer);
        if (key == null) {
            throw new IllegalStateException("Unknown sync serializer: " + serializer);
        }
        buf.writeResourceLocation(key);
        if (data == null) {
            buf.writeBoolean(false);
        } else {
            buf.writeBoolean(true);
            serializer.getCodec().encode(buf, (T) data);
        }
    }

    public void updateTarget(AttachmentTarget target) {
        if (data == null) {
            target.removeAttached(serializer.getAttachmentType());
        } else {
            target.setAttached(serializer.getAttachmentType(), data);
        }
    }

    public static List<AttachmentData<?>> getAllSyncData(AttachmentTarget target) {
        List<AttachmentData<?>> syncData = new ArrayList<>();
        for (DataSyncSerializer<?> serializer : BotariumData.SYNC_SERIALIZERS) {
            syncData.add(AttachmentData.of(target, serializer));
        }
        return syncData;
    }
}