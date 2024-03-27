package earth.terrarium.botarium.mixin;

import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.fabricmc.fabric.impl.attachment.AttachmentSerializingImpl;
import net.fabricmc.fabric.impl.attachment.AttachmentTargetImpl;
import net.fabricmc.fabric.impl.transfer.item.ItemVariantImpl;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Objects;

@SuppressWarnings("UnstableApiUsage")
@Mixin({ItemStack.class, ItemVariantImpl.class})
public class ItemAttachmentHolderMixin implements AttachmentTargetImpl {
    @Unique
    @Nullable
    private IdentityHashMap<AttachmentType<?>, Object> fabric_dataAttachments = null;

    @SuppressWarnings("unchecked")
    @Override
    @Nullable
    public <T> T getAttached(AttachmentType<T> type) {
        return fabric_dataAttachments == null ? null : (T) fabric_dataAttachments.get(type);
    }

    @SuppressWarnings("unchecked")
    @Override
    @Nullable
    public <T> T setAttached(AttachmentType<T> type, @Nullable T value) {
        // Extremely inelegant, but the only alternative is separating out these two mixins and duplicating code
        if (value == null) {
            if (fabric_dataAttachments == null) {
                return null;
            }

            T removed = (T) fabric_dataAttachments.remove(type);

            if (fabric_dataAttachments.isEmpty()) {
                fabric_dataAttachments = null;
            }

            return removed;
        } else {
            if (fabric_dataAttachments == null) {
                fabric_dataAttachments = new IdentityHashMap<>();
            }

            return (T) fabric_dataAttachments.put(type, value);
        }
    }

    @Override
    public boolean hasAttached(AttachmentType<?> type) {
        return fabric_dataAttachments != null && fabric_dataAttachments.containsKey(type);
    }

    @Override
    public void fabric_writeAttachmentsToNbt(CompoundTag nbt) {
        AttachmentSerializingImpl.serializeAttachmentData(nbt, fabric_dataAttachments);
    }

    @Override
    public void fabric_readAttachmentsFromNbt(CompoundTag nbt) {
        fabric_dataAttachments = AttachmentSerializingImpl.deserializeAttachmentData(nbt);
    }

    @Override
    public boolean fabric_hasPersistentAttachments() {
        return AttachmentSerializingImpl.hasPersistentAttachments(fabric_dataAttachments);
    }

    @Override
    public Map<AttachmentType<?>, ?> fabric_getAttachments() {
        return fabric_dataAttachments;
    }

}
