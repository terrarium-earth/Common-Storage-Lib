package earth.terrarium.botarium.api.fluid.utils;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import earth.terrarium.botarium.api.fluid.FluidHolder;
import earth.terrarium.botarium.api.fluid.FluidHooks;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FluidIngredient implements Predicate<FluidHolder> {
    public static final Codec<FluidIngredient> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.either(FluidValue.CODEC, TagValue.CODEC).listOf().fieldOf("fluids").forGetter(FluidIngredient::getRawValues)
    ).apply(instance, FluidIngredient::new));

    private final List<Either<FluidValue, TagValue>> values;
    private List<FluidHolder> cachedFluids;

    private FluidIngredient(List<Either<FluidValue, TagValue>> stream) {
        this.values = stream;
    }

    @Override
    public boolean test(FluidHolder fluidHolder) {
        if (this.values.isEmpty()) {
            return fluidHolder.isEmpty();
        } else {
            for (FluidHolder value : getFluids()) {
                if (fluidHolder.getFluid().isSame(value.getFluid())) {
                    return true;
                }
            }
            return false;
        }
    }

    public List<FluidHolder> getFluids() {
        dissolve();
        return cachedFluids;
    }

    public void dissolve() {
        if (this.cachedFluids == null) {
            this.cachedFluids = this.values.stream().flatMap(either -> either.map(
                    fluidValue -> Stream.of(fluidValue.fluid()),
                    tagValue -> tagValue.getFluids().stream()
            )).collect(Collectors.toList());
        }
    }

    public List<Either<FluidValue, TagValue>> getRawValues() {
        return values;
    }

    public interface Value {
        Collection<FluidHolder> getFluids();
    }

    public record FluidValue(FluidHolder fluid) implements Value {
        public static final Codec<FluidValue> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                FluidHolder.CODEC.fieldOf("fluid").orElse(FluidHooks.emptyFluid()).forGetter(FluidValue::fluid)
        ).apply(instance, FluidValue::new));

        @Override
        public Collection<FluidHolder> getFluids() {
            return Collections.singleton(fluid);
        }
    }

    public record TagValue(TagKey<Fluid> tag) implements Value {
        public static final Codec<TagValue> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                TagKey.codec(Registry.FLUID.key()).fieldOf("tag").forGetter(TagValue::tag)
        ).apply(instance, TagValue::new));

        @Override
        public Collection<FluidHolder> getFluids() {
            List<FluidHolder> list = Lists.newArrayList();

            for (Holder<Fluid> holder : Registry.FLUID.getTagOrEmpty(this.tag)) {
                list.add(FluidHooks.newFluidHolder(holder.value(), 1, null));
            }

            return list;
        }
    }
}
