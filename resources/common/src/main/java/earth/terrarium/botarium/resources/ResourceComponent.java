package earth.terrarium.botarium.resources;

import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public abstract class ResourceComponent implements Resource {
    protected final CompoundTag tag;

    protected ResourceComponent(CompoundTag tag) {
        this.tag = tag;
    }

    public @NotNull CompoundTag getTag() {
        return tag;
    }

    public boolean tagsMatch(CompoundTag other) {
        return Objects.equals(tag, other);
    }
}
