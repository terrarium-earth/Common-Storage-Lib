package earth.terrarium.botarium.data.sync;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.fabricmc.fabric.api.attachment.v1.AttachmentTarget;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("UnstableApiUsage")
public class AutoSyncRegistry {
    private static final ResourceKey<Registry<DataSyncSerializer<?>>> SYNC_SERIALIZERS_KEY = ResourceKey.createRegistryKey(new ResourceLocation("botarium", "sync_serializers"));
    private static final Registry<DataSyncSerializer<?>> SERIALIZERS = FabricRegistryBuilder.createSimple(SYNC_SERIALIZERS_KEY).buildAndRegister();

    public static <T> void register(ResourceLocation id, DataSyncSerializer<T> serializer) {
        Registry.register(SERIALIZERS, id, serializer);
    }

    public static StreamCodec<RegistryFriendlyByteBuf, List<Pair<DataSyncSerializer<?>, ?>>> SYNC_SERIALIZER_STREAM_CODEC = new StreamCodec<>() {
        @Override
        public List<Pair<DataSyncSerializer<?>, ?>> decode(RegistryFriendlyByteBuf buf) {
            return buf.readCollection(ArrayList::new, byteBuf -> {
                ResourceLocation id = buf.readResourceLocation();
                DataSyncSerializer<?> dataSyncSerializer = SERIALIZERS.get(id);
                Object data = AutoSyncRegistry.decode(buf, dataSyncSerializer);
                return Pair.of(dataSyncSerializer, data);
            });
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, List<Pair<DataSyncSerializer<?>, ?>> value) {
            buf.writeCollection(value, (byteBuf, pair) -> {
                AutoSyncRegistry.encode(buf, pair.getFirst(), pair.getSecond());
            });
        }
    };

    public static <T> void encode(RegistryFriendlyByteBuf buf, DataSyncSerializer<T> type, Object data) {
        ResourceLocation key = SERIALIZERS.getKey(type);
        if (key == null) {
            throw new IllegalStateException("Unknown sync serializer: " + type);
        }
        buf.writeResourceLocation(key);
        if (data == null) {
            buf.writeBoolean(false);
        } else {
            buf.writeBoolean(true);
            type.getCodec().encode(buf, (T) data);
        }
    }

    public static <T> T decode(RegistryFriendlyByteBuf buf, DataSyncSerializer<T> serializer) {
        if (buf.readBoolean()) {
            return serializer.getCodec().decode(buf);
        } else {
            return null;
        }
    }

    public static void fromTargetToTag(AttachmentTarget target) {

    }

    public static void updateTarget(AttachmentTarget target, List<Pair<DataSyncSerializer<?>, ?>> data) {
        for (Pair<DataSyncSerializer<?>, ?> pair : data) {
            forceAttach(target, pair.getFirst(), pair.getSecond());
        }
    }

    public static <T> void forceAttach(AttachmentTarget target, DataSyncSerializer<T> serializer, Object data) {
        target.setAttached(serializer.getAttachmentType(), (T) data);
    }
}
