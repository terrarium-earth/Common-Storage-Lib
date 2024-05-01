package earth.terrarium.botarium.resources.item.ingredient;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.component.DataComponentPredicate;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.msrandom.multiplatform.annotations.Expect;

public class ItemIngredient {
    @Expect
    public static final MapCodec<Ingredient> NON_EMPTY_MAP_CODEC;

    @Expect
    public static Ingredient all(Ingredient... ingredients);

    @Expect
    public static Ingredient any(Ingredient... ingredients);

    @Expect
    public static Ingredient difference(Ingredient base, Ingredient subtracted);

    @Expect
    public static Ingredient components(Ingredient base, DataComponentPredicate components);

    @Expect
    public static Ingredient components(ItemStack stack);

    public static SizedItemIngredient sized(Ingredient ingredient, int size) {
        return new SizedItemIngredient(ingredient, size);
    }
}
