package earth.terrarium.botarium.resource.entity.ingredient;

import earth.terrarium.botarium.resource.entity.EntityResource;

import java.util.List;

public interface EntityIngredient {
    boolean test(EntityResource entityResource);

    List<EntityResource> getMatchingEntities();

    boolean requiresTesting();

    EntityIngredientType<?> getType();
}
