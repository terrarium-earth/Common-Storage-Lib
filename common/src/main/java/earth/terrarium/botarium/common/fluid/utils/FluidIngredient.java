package earth.terrarium.botarium.common.fluid.utils;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import earth.terrarium.botarium.common.fluid.base.FluidHolder;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FluidIngredient implements Predicate<FluidHolder> {
    private static final Codec<FluidIngredient> NEW_CODEC = listAndObjectCodec(Codec.either(FluidValue.CODEC, TagValue.CODEC), FluidIngredient::getRawValues, FluidIngredient::new);
    
    private static final Codec<FluidIngredient> OLD_CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.either(FluidValue.CODEC, TagValue.CODEC).listOf().fieldOf("fluids").forGetter(FluidIngredient::getRawValues)
    ).apply(instance, FluidIngredient::new));
    public static final Codec<FluidIngredient> CODEC = Codec.either(NEW_CODEC, OLD_CODEC).xmap(p -> p.map(a -> a, b -> b), Either::left);

    private final List<Either<FluidValue, TagValue>> values;
    private List<FluidHolder> cachedFluids;

    public static <A, B> Codec<B> listAndObjectCodec(Codec<A> codec, Function<B, List<A>> from, Function<List<A>, B> to) {
        return Codec.either(codec, codec.listOf()).xmap(eitherListEither -> eitherListEither.map(
                either -> to.apply(List.of(either)),
                to
        ), a -> from.apply(a).size() == 1 ? Either.left(from.apply(a).get(0)) : Either.right(from.apply(a)));
    }

    protected FluidIngredient(List<Either<FluidValue, TagValue>> stream) {
        this.values = stream;
    }

    public static FluidIngredient of() {
        return new FluidIngredient(List.of());
    }

    public static FluidIngredient of(Fluid... fluids) {
        return FluidIngredient.of(Arrays.stream(fluids).map(FluidHolder::of));
    }

    public static FluidIngredient of(FluidHolder... fluids) {
        return FluidIngredient.of(Arrays.stream(fluids));
    }

    public static FluidIngredient of(Stream<FluidHolder> fluids) {
        List<Either<FluidValue, TagValue>> values = new ArrayList<>();
        for (FluidHolder fluid : fluids.filter(Predicate.not(FluidHolder::isEmpty)).toList()) {
            values.add(Either.left(new FluidValue(fluid)));
        }
        return new FluidIngredient(values);
    }

    public static FluidIngredient of(TagKey<Fluid> tag) {
        return new FluidIngredient(List.of(Either.right(new TagValue(tag))));
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
        private static final Codec<FluidValue> NEW_CODEC = FluidHolder.CODEC.orElse(FluidHooks.emptyFluid())
            .xmap(FluidValue::new, FluidValue::fluid);
        private static final Codec<FluidValue> OLD_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            FluidHolder.CODEC.fieldOf("fluid").orElse(FluidHooks.emptyFluid()).forGetter(FluidValue::fluid)
        ).apply(instance, FluidValue::new));
        public static final Codec<FluidValue> CODEC = Codec.either(NEW_CODEC, OLD_CODEC).xmap(p -> p.map(a -> a, b -> b), Either::left);

        @Override
        public Collection<FluidHolder> getFluids() {
            return Collections.singleton(fluid);
        }
    }

    public record TagValue(TagKey<Fluid> tag) implements Value {
        public static final Codec<TagValue> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            TagKey.codec(BuiltInRegistries.FLUID.key()).fieldOf("tag").forGetter(TagValue::tag)
        ).apply(instance, TagValue::new));

        @Override
        public Collection<FluidHolder> getFluids() {
            List<FluidHolder> list = Lists.newArrayList();

            for (Holder<Fluid> holder : BuiltInRegistries.FLUID.getTagOrEmpty(this.tag)) {
                list.add(FluidHolder.of(holder.value()));
            }

            return list;
        }
    }
}
