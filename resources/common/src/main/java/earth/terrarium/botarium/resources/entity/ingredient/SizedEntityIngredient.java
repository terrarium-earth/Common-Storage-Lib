package earth.terrarium.botarium.resources.entity.ingredient;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.teamresourceful.bytecodecs.base.ByteCodec;
import com.teamresourceful.bytecodecs.base.object.ObjectByteCodec;
import earth.terrarium.botarium.resources.ResourceStack;
import earth.terrarium.botarium.resources.entity.EntityResource;
import earth.terrarium.botarium.resources.util.CodecUtils;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SizedEntityIngredient {
    public static final MapCodec<SizedEntityIngredient> FLAT_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                    EntityIngredient.MAP_CODEC.forGetter(SizedEntityIngredient::ingredient),
                    CodecUtils.optionalFieldAlwaysWrite(Codec.LONG, "amount", 1L).forGetter(SizedEntityIngredient::getAmount))
            .apply(instance, SizedEntityIngredient::new));

    public static final MapCodec<SizedEntityIngredient> NESTED_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                    EntityIngredient.CODEC.fieldOf("ingredient").forGetter(SizedEntityIngredient::ingredient),
                    CodecUtils.optionalFieldAlwaysWrite(Codec.LONG, "amount", 1L).forGetter(SizedEntityIngredient::getAmount))
            .apply(instance, SizedEntityIngredient::new));

    public static final ByteCodec<SizedEntityIngredient> BYTE_CODEC = ObjectByteCodec.create(
            EntityIngredient.BYTE_CODEC.fieldOf(SizedEntityIngredient::ingredient),
            ByteCodec.VAR_LONG.fieldOf(SizedEntityIngredient::getAmount),
            SizedEntityIngredient::new
    );

    public static SizedEntityIngredient of(EntityResource item, int count) {
        return new SizedEntityIngredient(EntityIngredient.of(item), count);
    }

    public static SizedEntityIngredient of(TagKey<EntityType<?>> tag, int count) {
        return new SizedEntityIngredient(EntityIngredient.of(tag), count);
    }

    public static SizedEntityIngredient of(ResourceStack<EntityResource> stack) {
        return new SizedEntityIngredient(EntityIngredient.of(stack.resource()), (int) stack.amount());
    }

    private final EntityIngredient ingredient;
    private final long amount;
    @Nullable
    private List<ResourceStack<EntityResource>> cachedStacks;

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

    public boolean test(ResourceStack<EntityResource> stack) {
        return ingredient.test(stack.resource()) && stack.amount() >= amount;
    }

    public List<ResourceStack<EntityResource>> getFluids() {
        if (cachedStacks == null) {
            cachedStacks = ingredient.getMatchingEntities().stream()
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
