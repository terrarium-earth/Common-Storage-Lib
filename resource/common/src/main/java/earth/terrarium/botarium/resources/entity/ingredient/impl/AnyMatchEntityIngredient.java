package earth.terrarium.botarium.resources.entity.ingredient.impl;

import com.mojang.serialization.MapCodec;
import earth.terrarium.botarium.resources.entity.EntityResource;
import earth.terrarium.botarium.resources.entity.ingredient.EntityIngredient;
import earth.terrarium.botarium.resources.entity.ingredient.EntityIngredientType;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public record AnyMatchEntityIngredient(List<EntityIngredient> children) implements ListEntityIngredient {
    public static final MapCodec<AnyMatchEntityIngredient> CODEC = EntityIngredient.CODEC.listOf().fieldOf("children").xmap(AnyMatchEntityIngredient::new, AnyMatchEntityIngredient::children);
    public static final EntityIngredientType<AnyMatchEntityIngredient> TYPE = new EntityIngredientType<>(new ResourceLocation("botarium", "any_match"), CODEC);

    @Override
    public boolean test(EntityResource fluidResource) {
        for (EntityIngredient fluidIngredient : children) {
            if (fluidIngredient.test(fluidResource)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<EntityResource> getMatchingEntities() {
        return children.stream()
                .flatMap(child -> child.getMatchingEntities().stream())
                .filter(this::test)
                .toList();
    }

    @Override
    public EntityIngredientType<?> getType() {
        return TYPE;
    }
}
