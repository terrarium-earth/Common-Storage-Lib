package earth.terrarium.common_storage_lib.resources.entity.ingredient.impl;

import com.mojang.serialization.MapCodec;
import earth.terrarium.common_storage_lib.resources.ResourceLib;
import earth.terrarium.common_storage_lib.resources.entity.EntityResource;
import earth.terrarium.common_storage_lib.resources.entity.ingredient.EntityIngredient;
import earth.terrarium.common_storage_lib.resources.entity.ingredient.EntityIngredientType;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public record AllMatchEntityIngredient(List<EntityIngredient> children) implements ListEntityIngredient {
    public static final MapCodec<AllMatchEntityIngredient> CODEC = EntityIngredient.CODEC.listOf().fieldOf("children").xmap(AllMatchEntityIngredient::new, AllMatchEntityIngredient::children);
    public static final EntityIngredientType<AllMatchEntityIngredient> TYPE = new EntityIngredientType<>(new ResourceLocation(ResourceLib.MOD_ID, "all_match"), CODEC);

    @Override
    public boolean test(EntityResource fluidResource) {
        for (EntityIngredient fluidIngredient : children) {
            if (!fluidIngredient.test(fluidResource)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public List<EntityResource> getMatchingEntities() {
        List<EntityResource> previewStacks = new ArrayList<>();

        for (EntityIngredient ingredient : children) {
            previewStacks.addAll(ingredient.getMatchingEntities());
        }

        return previewStacks;
    }

    @Override
    public EntityIngredientType<?> getType() {
        return TYPE;
    }
}
