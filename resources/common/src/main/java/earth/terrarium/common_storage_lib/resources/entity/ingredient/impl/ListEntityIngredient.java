package earth.terrarium.common_storage_lib.resources.entity.ingredient.impl;

import earth.terrarium.common_storage_lib.resources.entity.ingredient.EntityIngredient;

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
