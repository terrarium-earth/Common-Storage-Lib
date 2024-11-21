package earth.terrarium.common_storage_lib.resources.fluid.ingredient;

import earth.terrarium.common_storage_lib.resources.fluid.ingredient.impl.*;

public class FluidIngredientRegistry {

    public static void init() {
        register(BaseFluidIngredient.TYPE);
        register(ComponentFluidIngredient.TYPE);
        register(DifferenceFluidIngredient.TYPE);
        register(AllMatchFluidIngredient.TYPE);
        register(AnyMatchFluidIngredient.TYPE);
    }

    public static void register(FluidIngredientType<?> type) {
        FluidIngredientType.INGREDIENT_TYPES.put(type.id(), type);
    }
}
