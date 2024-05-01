package earth.terrarium.botarium.resources.entity.ingredient.impl;

import earth.terrarium.botarium.resources.entity.ingredient.EntityIngredient;

import java.util.List;

public interface ListEntityIngredient extends EntityIngredient {
    List<EntityIngredient> children();

    @Override
    default boolean requiresTesting() {
        for (EntityIngredient ingredient : children()) {
            if (ingredient.requiresTesting()) {
                return true;
            }
        }
        return false;
    }
}
