package earth.terrarium.botarium.fluid.base;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import earth.terrarium.botarium.storage.unit.TransferUnit;
import earth.terrarium.botarium.storage.unit.UnitStack;
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

public class FluidUnit implements TransferUnit<Fluid, FluidUnit> {
    public static final FluidUnit BLANK = FluidUnit.of(Fluids.EMPTY, DataComponentPatch.EMPTY);

    public static final Codec<FluidUnit> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BuiltInRegistries.FLUID.byNameCodec().fieldOf("id").forGetter(FluidUnit::getType),
            DataComponentPatch.CODEC.optionalFieldOf("components", DataComponentPatch.EMPTY).forGetter(FluidUnit::getDataPatch)
    ).apply(instance, FluidUnit::of));

    public static final MapCodec<FluidUnit> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            BuiltInRegistries.FLUID.byNameCodec().fieldOf("id").forGetter(FluidUnit::getType),
            DataComponentPatch.CODEC.optionalFieldOf("components", DataComponentPatch.EMPTY).forGetter(FluidUnit::getDataPatch)
    ).apply(instance, FluidUnit::of));

    public static final StreamCodec<? super RegistryFriendlyByteBuf, Holder<Fluid>> FLUID_STREAM_CODEC = ByteBufCodecs.holderRegistry(Registries.FLUID);

    public static final StreamCodec<? super RegistryFriendlyByteBuf, FluidUnit> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public FluidUnit decode(RegistryFriendlyByteBuf object) {
            Holder<Fluid> holder = FLUID_STREAM_CODEC.decode(object);
            DataComponentPatch dataComponentPatch = DataComponentPatch.STREAM_CODEC.decode(object);
            return of(holder.value(), dataComponentPatch);
        }

        @Override
        public void encode(RegistryFriendlyByteBuf object, FluidUnit object2) {
            FLUID_STREAM_CODEC.encode(object, object2.type.builtInRegistryHolder());
            DataComponentPatch.STREAM_CODEC.encode(object, object2.getDataPatch());
        }
    };

    private final Fluid type;
    private final PatchedDataComponentMap components;

    private FluidUnit(Fluid type, PatchedDataComponentMap components) {
        this.type = type;
        this.components = components;
    }

    public static FluidUnit of(Fluid fluid) {
        return new FluidUnit(fluid, PatchedDataComponentMap.fromPatch(DataComponentMap.EMPTY, DataComponentPatch.EMPTY));
    }

    public static FluidUnit of(Fluid fluid, DataComponentPatch components) {
        return new FluidUnit(fluid, PatchedDataComponentMap.fromPatch(DataComponentMap.EMPTY, components));
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
    public <D> FluidUnit set(DataComponentType<D> type, D value) {
        PatchedDataComponentMap copy = components.copy();
        copy.set(type, value);
        return new FluidUnit(this.type, copy);
    }

    @Override
    public FluidUnit modify(DataComponentPatch patch) {
        PatchedDataComponentMap copy = components.copy();
        copy.applyPatch(patch);
        return new FluidUnit(this.type, copy);
    }

    @Override
    public UnitStack<FluidUnit> toStack(long amount) {
        return new UnitStack<>(this, amount);
    }

    @Override
    public @NotNull DataComponentMap getComponents() {
        return components;
    }
}
