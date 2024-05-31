package earth.terrarium.common_storage_lib.resources.fluid.ingredient;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.teamresourceful.bytecodecs.base.ByteCodec;
import com.teamresourceful.bytecodecs.base.object.ObjectByteCodec;
import earth.terrarium.common_storage_lib.resources.ResourceStack;
import earth.terrarium.common_storage_lib.resources.fluid.FluidResource;
import earth.terrarium.common_storage_lib.resources.fluid.util.FluidAmounts;
import earth.terrarium.common_storage_lib.resources.util.CodecUtils;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SizedFluidIngredient {
    public static final MapCodec<SizedFluidIngredient> FLAT_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                    FluidIngredient.MAP_CODEC.forGetter(SizedFluidIngredient::ingredient),
                    CodecUtils.optionalFieldAlwaysWrite(Codec.LONG, "amount", FluidAmounts.BUCKET).forGetter(SizedFluidIngredient::getAmount))
            .apply(instance, SizedFluidIngredient::new));

    public static final MapCodec<SizedFluidIngredient> NESTED_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                    FluidIngredient.CODEC.fieldOf("ingredient").forGetter(SizedFluidIngredient::ingredient),
                    CodecUtils.optionalFieldAlwaysWrite(Codec.LONG, "amount", FluidAmounts.BUCKET).forGetter(SizedFluidIngredient::getAmount))
            .apply(instance, SizedFluidIngredient::new));

    public static final MapCodec<SizedFluidIngredient> FLAT_MB_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                    FluidIngredient.MAP_CODEC.forGetter(SizedFluidIngredient::ingredient),
                    CodecUtils.optionalFieldAlwaysWrite(Codec.LONG, "millibuckets", 1000L).forGetter(SizedFluidIngredient::getAmountAsMb))
            .apply(instance, (ingredient, millibuckets) -> new SizedFluidIngredient(ingredient, FluidAmounts.toPlatformAmount(millibuckets))));

    public static final MapCodec<SizedFluidIngredient> NESTED_MB_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                    FluidIngredient.CODEC.fieldOf("ingredient").forGetter(SizedFluidIngredient::ingredient),
                    CodecUtils.optionalFieldAlwaysWrite(Codec.LONG, "millibuckets", 1000L).forGetter(SizedFluidIngredient::getAmountAsMb))
            .apply(instance, (ingredient, millibuckets) -> new SizedFluidIngredient(ingredient, FluidAmounts.toPlatformAmount(millibuckets))));

    public static final ByteCodec<SizedFluidIngredient> BYTE_CODEC = ObjectByteCodec.create(
            FluidIngredient.BYTE_CODEC.fieldOf(SizedFluidIngredient::ingredient),
            ByteCodec.LONG.fieldOf(SizedFluidIngredient::getAmount),
            SizedFluidIngredient::new
    );

    public static SizedFluidIngredient of(FluidResource item, int count) {
        return new SizedFluidIngredient(FluidIngredient.of(item), count);
    }

    public static SizedFluidIngredient of(TagKey<Fluid> tag, int count) {
        return new SizedFluidIngredient(FluidIngredient.of(tag), count);
    }

    public static SizedFluidIngredient of(ResourceStack<FluidResource> stack) {
        return new SizedFluidIngredient(FluidIngredient.of(stack.resource()), (int) stack.amount());
    }

    private final FluidIngredient ingredient;
    private final long amount;
    @Nullable
    private List<ResourceStack<FluidResource>> cachedStacks;

    public SizedFluidIngredient(FluidIngredient ingredient, long amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Size must be positive");
        }
        this.ingredient = ingredient;
        this.amount = amount;
    }

    public FluidIngredient ingredient() {
        return ingredient;
    }

    public long getAmount() {
        return amount;
    }

    public long getAmountAsMb() {
        return FluidAmounts.toMillibuckets(amount);
    }

    public boolean test(ResourceStack<FluidResource> stack) {
        return ingredient.test(stack.resource()) && stack.amount() >= amount;
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
