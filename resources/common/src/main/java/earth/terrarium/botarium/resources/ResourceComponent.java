package earth.terrarium.botarium.resources;

import com.mojang.serialization.Codec;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public abstract class ResourceComponent implements Resource {
    @Nullable
    protected final CompoundTag tag;

    protected ResourceComponent(@Nullable CompoundTag tag) {
        this.tag = tag;
    }

    public @Nullable CompoundTag getTag() {
        return tag;
    }

    @Nullable
    public <T> T getValue(Codec<T> codec, String key) {
        return getOrDefault(codec, key, null);
    }

    public <T> T getOrDefault(Codec<T> codec, String key, T defaultValue) {
        if (tag == null) return defaultValue;
        return codec.parse(NbtOps.INSTANCE, tag.get(key)).result().orElse(defaultValue);
    }

    public boolean tagsMatch(CompoundTag other) {
        return Objects.equals(tag, other);
    }
}
