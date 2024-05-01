package earth.terrarium.botarium.resources.entity.ingredient.impl;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import earth.terrarium.botarium.resources.entity.EntityResource;
import earth.terrarium.botarium.resources.entity.ingredient.EntityIngredient;
import earth.terrarium.botarium.resources.entity.ingredient.EntityIngredientType;
import net.minecraft.core.component.DataComponentPredicate;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public record ComponentEntityIngredient(EntityIngredient ingredient, DataComponentPredicate predicate) implements EntityIngredient {
    public static final MapCodec<ComponentEntityIngredient> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            EntityIngredient.CODEC.fieldOf("ingredient").forGetter(ComponentEntityIngredient::ingredient),
            DataComponentPredicate.CODEC.fieldOf("predicate").forGetter(ComponentEntityIngredient::predicate)
    ).apply(instance, ComponentEntityIngredient::new));

    public static final EntityIngredientType<ComponentEntityIngredient> TYPE = new EntityIngredientType<>(new ResourceLocation("botarium", "component"), CODEC);

    @Override
    public boolean test(EntityResource fluidResource) {
        return ingredient.test(fluidResource) && predicate.test(fluidResource.getComponents());
    }

    @Override
    public List<EntityResource> getMatchingEntities() {
        return ingredient().getMatchingEntities().stream().map(fluidResource -> fluidResource.modify(predicate.asPatch())).toList();
    }

    @Override
    public boolean requiresTesting() {
        return true;
    }

    @Override
    public EntityIngredientType<?> getType() {
        return TYPE;
    }
}
