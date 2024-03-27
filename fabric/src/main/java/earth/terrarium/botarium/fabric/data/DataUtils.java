package earth.terrarium.botarium.fabric.data;

import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.fabricmc.fabric.impl.attachment.AttachmentTargetImpl;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;
import java.util.function.Supplier;

@SuppressWarnings({"UnstableApiUsage", "unchecked"})
public class DataUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger("botarium-item-data-attachment-impl");

    public static <H extends AttachmentTargetImpl> void copyAttachments(H from, H to, Predicate<AttachmentType<?>> filter) {
        Map<AttachmentType<?>, ?> attachments = from.fabric_getAttachments();
        if (attachments == null) return;
        for (var entry : attachments.entrySet()) {
            AttachmentType<Object> type = (AttachmentType<Object>) entry.getKey();
            var serializer = type.persistenceCodec();
            if (serializer != null && filter.test(type)) {
                Tag tag = write(type, entry.getValue());
                Object data = read(type, tag);
                to.setAttached(type, data);
            }
        }
    }

    public static <H extends AttachmentTargetImpl> boolean areAttachmentsCompatible(H first, H second) {
        Map<AttachmentType<?>, ?> firstAttachments = first.fabric_getAttachments();
        Map<AttachmentType<?>, ?> secondAttachments = second.fabric_getAttachments();
        if (firstAttachments == null && secondAttachments == null) return true;
        if (secondAttachments == null || firstAttachments == null) return false;
        for (var entry : firstAttachments.entrySet()) {
            AttachmentType<Object> type = (AttachmentType<Object>) entry.getKey();
            if (type.persistenceCodec() != null) {
                var otherData = secondAttachments.get(type);
                Supplier<Object> initializer = type.initializer();
                if (otherData == null && initializer != null)
                    otherData = initializer.get();
                if (!Objects.equals(write(type, entry.getValue()), write(type, otherData)))
                    return false;
            }
        }
        for (var entry : secondAttachments.entrySet()) {
            AttachmentType<Object> type = (AttachmentType<Object>) entry.getKey();
            if (type.persistenceCodec() != null) {
                var data = firstAttachments.get(type);
                if (data != null)
                    continue; // already checked in the first loop
                data = type.initializer().get();
                if(!Objects.equals(write(type, entry.getValue()), write(type, data))) return false;
            }
        }
        return true;
    }

    public static <T> Tag write(AttachmentType<T> type, T value) {
        AtomicReference<Tag> tag = new AtomicReference<>(new CompoundTag());
        Codec<T> codec = type.persistenceCodec();
        if (codec == null) return tag.get();
        codec.encodeStart(NbtOps.INSTANCE, value)
            .get()
            .ifRight(partial -> {
                LOGGER.warn("Couldn't serialize attachment " + type.identifier() + ", skipping. Error:");
                LOGGER.warn(partial.message());
            })
            .ifLeft(tag::set);
        return tag.get();
    }

    public static <T> T read(AttachmentType<T> type, Tag tag) {
        AtomicReference<T> value = new AtomicReference<>(null);
        Codec<T> codec = type.persistenceCodec();
        if (codec == null) return value.get();
        codec.decode(NbtOps.INSTANCE, tag)
            .get()
            .ifRight(partial -> {
                LOGGER.warn("Couldn't deserialize attachment " + type.identifier() + ", skipping. Error:");
                LOGGER.warn(partial.message());
            })
            .ifLeft(parsed -> value.set(parsed.getFirst()));

        return value.get();
    }

    public static <T> T copy(AttachmentType<T> type, T value) {
        return read(type, write(type, value));
    }
}
