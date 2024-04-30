package earth.terrarium.botarium.resource.fluid;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import earth.terrarium.botarium.resource.TransferResource;
import earth.terrarium.botarium.resource.ResourceStack;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.PatchedDataComponentMap;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.NotNull;

public class FluidResource implements TransferResource<Fluid, FluidResource> {
    public static final FluidResource BLANK = FluidResource.of(Fluids.EMPTY, DataComponentPatch.EMPTY);

    public static final Codec<FluidResource> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BuiltInRegistries.FLUID.byNameCodec().fieldOf("id").forGetter(FluidResource::getType),
            DataComponentPatch.CODEC.optionalFieldOf("components", DataComponentPatch.EMPTY).forGetter(FluidResource::getDataPatch)
    ).apply(instance, FluidResource::of));

    public static final MapCodec<FluidResource> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            BuiltInRegistries.FLUID.byNameCodec().fieldOf("id").forGetter(FluidResource::getType),
            DataComponentPatch.CODEC.optionalFieldOf("components", DataComponentPatch.EMPTY).forGetter(FluidResource::getDataPatch)
    ).apply(instance, FluidResource::of));

    public static final StreamCodec<? super RegistryFriendlyByteBuf, Holder<Fluid>> FLUID_STREAM_CODEC = ByteBufCodecs.holderRegistry(Registries.FLUID);

    public static final StreamCodec<? super RegistryFriendlyByteBuf, FluidResource> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public FluidResource decode(RegistryFriendlyByteBuf object) {
            Holder<Fluid> holder = FLUID_STREAM_CODEC.decode(object);
            DataComponentPatch dataComponentPatch = DataComponentPatch.STREAM_CODEC.decode(object);
            return of(holder.value(), dataComponentPatch);
        }

        @Override
        public void encode(RegistryFriendlyByteBuf object, FluidResource object2) {
            FLUID_STREAM_CODEC.encode(object, object2.type.builtInRegistryHolder());
            DataComponentPatch.STREAM_CODEC.encode(object, object2.getDataPatch());
        }
    };

    public static FluidResource of(Fluid fluid) {
        return new FluidResource(fluid, PatchedDataComponentMap.fromPatch(DataComponentMap.EMPTY, DataComponentPatch.EMPTY));
    }

    public static FluidResource of(Fluid fluid, DataComponentPatch components) {
        return new FluidResource(fluid, PatchedDataComponentMap.fromPatch(DataComponentMap.EMPTY, components));
    }

    private final Fluid type;
    private final PatchedDataComponentMap components;

    private FluidResource(Fluid type, PatchedDataComponentMap components) {
        this.type = type;
        this.components = components;
    }

    @Override
    public boolean isBlank() {
        return type == Fluids.EMPTY;
    }

    @Override
    public Fluid getType() {
        return type;
    }

    @Override
    public DataComponentPatch getDataPatch() {
        return components.asPatch();
    }

    @Override
    public <D> FluidResource set(DataComponentType<D> type, D value) {
        PatchedDataComponentMap copy = components.copy();
        copy.set(type, value);
        return new FluidResource(this.type, copy);
    }

    @Override
    public FluidResource modify(DataComponentPatch patch) {
        PatchedDataComponentMap copy = components.copy();
        copy.applyPatch(patch);
        return new FluidResource(this.type, copy);
    }

    @Override
    public ResourceStack<FluidResource> toStack(long amount) {
        return new ResourceStack<>(this, amount);
    }

    @Override
    public @NotNull DataComponentMap getComponents() {
        return components;
    }
}
