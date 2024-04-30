package earth.terrarium.botarium.resource.item.ingredient;

import com.mojang.serialization.MapCodec;
import earth.terrarium.botarium.resource.item.ItemResource;
import net.minecraft.world.item.crafting.Ingredient;
import net.msrandom.multiplatform.annotations.Expect;

import java.util.List;

public interface ItemIngredient {
    @Expect
    MapCodec<Ingredient> MAP_CODEC = null;

    boolean test(ItemResource itemResource);

    List<ItemResource> getMatchingItems();

    boolean requiresTesting();

    ItemIngredientType<?> getType();

    Ingredient toVanilla();
}
