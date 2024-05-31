package earth.terrarium.botarium.resources.fluid;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import earth.terrarium.botarium.resources.ResourceComponent;
import earth.terrarium.botarium.resources.ResourceStack;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

public final class FluidResource extends ResourceComponent {
    public static final FluidResource BLANK = FluidResource.of(Fluids.EMPTY, new CompoundTag());

    public static final MapCodec<FluidResource> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            BuiltInRegistries.FLUID.byNameCodec().fieldOf("id").forGetter(FluidResource::getType),
            CompoundTag.CODEC.fieldOf("tag").forGetter(FluidResource::getTag)
    ).apply(instance, FluidResource::of));

    public static final Codec<FluidResource> CODEC = MAP_CODEC.codec();

    public static final Codec<FluidResource> SIMPLE_CODEC = BuiltInRegistries.FLUID.byNameCodec().xmap(FluidResource::of, FluidResource::getType);

    public static FluidResource of(Fluid fluid) {
        return new FluidResource(fluid, new CompoundTag());
    }

    public static FluidResource of(Holder<Fluid> holder) {
        return of(holder.value());
    }

    public static FluidResource of(Fluid fluid, CompoundTag tag) {
        return new FluidResource(fluid, tag);
    }

    public static FluidResource of(Holder<Fluid> holder, CompoundTag tag) {
        return of(holder.value(), tag);
    }

    private final Fluid type;

    public FluidResource(Fluid type, CompoundTag tag) {
        super(tag);
        this.type = type;
    }

    public <T> FluidResource set(Codec<T> codec, String key, T value) {
        CompoundTag tag = getTag();

        if (tag == null) {
            tag = new CompoundTag();
        } else {
            tag = tag.copy();
        }

        tag.put(key, codec.encodeStart(NbtOps.INSTANCE, value).result().orElseThrow());
        return new FluidResource(type, tag);
    }

    public FluidResource remove(String key) {
        CompoundTag tag = getTag();
        if (tag == null) return this;
        tag = tag.copy();
        tag.remove(key);
        return new FluidResource(type, tag);
    }

    public Fluid getType() {
        return type;
    }

    public boolean isOf(Fluid type) {
        return getType() == type;
    }

    @Override
    public boolean isBlank() {
        return getType() == Fluids.EMPTY;
    }

    public ResourceStack<FluidResource> toStack(long amount) {
        return new ResourceStack<>(this, amount);
    }

    public Holder<Fluid> asHolder() {
        return type.builtInRegistryHolder();
    }

    public boolean is(TagKey<Fluid> tag) {
        return type.is(tag);
    }
}
