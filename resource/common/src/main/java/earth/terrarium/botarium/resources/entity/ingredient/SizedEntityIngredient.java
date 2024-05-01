package earth.terrarium.botarium.resources.entity.ingredient;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import earth.terrarium.botarium.resources.ResourceStack;
import earth.terrarium.botarium.resources.fluid.FluidResource;
import earth.terrarium.botarium.resources.fluid.util.FluidAmounts;
import earth.terrarium.botarium.resources.util.CodecUtils;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SizedEntityIngredient {
    public static final MapCodec<SizedEntityIngredient> FLAT_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                    EntityIngredient.MAP_CODEC.forGetter(SizedEntityIngredient::ingredient),
                    CodecUtils.optionalFieldAlwaysWrite(Codec.LONG, "amount", FluidAmounts.BUCKET).forGetter(SizedEntityIngredient::getAmount))
            .apply(instance, SizedEntityIngredient::new));

    public static final MapCodec<SizedEntityIngredient> NESTED_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                    EntityIngredient.CODEC.fieldOf("ingredient").forGetter(SizedEntityIngredient::ingredient),
                    CodecUtils.optionalFieldAlwaysWrite(Codec.LONG, "amount", FluidAmounts.BUCKET).forGetter(SizedEntityIngredient::getAmount))
            .apply(instance, SizedEntityIngredient::new));

    public static final MapCodec<SizedEntityIngredient> FLAT_MB_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                    EntityIngredient.MAP_CODEC.forGetter(SizedEntityIngredient::ingredient),
                    CodecUtils.optionalFieldAlwaysWrite(Codec.LONG, "millibuckets", 1000L).forGetter(SizedEntityIngredient::getAmountAsMb))
            .apply(instance, (ingredient, millibuckets) -> new SizedEntityIngredient(ingredient, FluidAmounts.toPlatformAmount(millibuckets))));

    public static final MapCodec<SizedEntityIngredient> NESTED_MB_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                    EntityIngredient.CODEC.fieldOf("ingredient").forGetter(SizedEntityIngredient::ingredient),
                    CodecUtils.optionalFieldAlwaysWrite(Codec.LONG, "millibuckets", 1000L).forGetter(SizedEntityIngredient::getAmountAsMb))
            .apply(instance, (ingredient, millibuckets) -> new SizedEntityIngredient(ingredient, FluidAmounts.toPlatformAmount(millibuckets))));

    public static final StreamCodec<RegistryFriendlyByteBuf, SizedEntityIngredient> STREAM_CODEC = StreamCodec.composite(
            EntityIngredient.STREAM_CODEC,
            SizedEntityIngredient::ingredient,
            ByteBufCodecs.VAR_LONG,
            SizedEntityIngredient::getAmount,
            SizedEntityIngredient::new);

    public static SizedEntityIngredient of(FluidResource item, int count) {
        return new SizedEntityIngredient(EntityIngredient.of(item), count);
    }

    public static SizedEntityIngredient of(TagKey<Fluid> tag, int count) {
        return new SizedEntityIngredient(EntityIngredient.of(tag), count);
    }

    public static SizedEntityIngredient of(ResourceStack<FluidResource> stack) {
        return new SizedEntityIngredient(EntityIngredient.of(stack.unit()), (int) stack.amount());
    }

    private final EntityIngredient ingredient;
    private final long amount;
    @Nullable
    private List<ResourceStack<FluidResource>> cachedStacks;

    public SizedEntityIngredient(EntityIngredient ingredient, long amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Size must be positive");
        }
        this.ingredient = ingredient;
        this.amount = amount;
    }

    public EntityIngredient ingredient() {
        return ingredient;
    }

    public long getAmount() {
        return amount;
    }

    public long getAmountAsMb() {
        return FluidAmounts.toMillibuckets(amount);
    }

    public boolean test(ResourceStack<FluidResource> stack) {
        return ingredient.test(stack.unit()) && stack.amount() >= amount;
    }

    public List<ResourceStack<FluidResource>> getFluids() {
        if (cachedStacks == null) {
            cachedStacks = ingredient.getMatchingFluids().stream()
                    .map(s -> s.toStack(amount))
                    .toList();
        }
        return cachedStacks;
    }

    @Override
    public String toString() {
        return amount + "x " + ingredient;
    }
}
