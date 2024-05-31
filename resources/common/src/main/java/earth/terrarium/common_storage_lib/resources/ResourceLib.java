package earth.terrarium.common_storage_lib.resources;

import earth.terrarium.common_storage_lib.resources.entity.ingredient.EntityIngredientRegistry;
import earth.terrarium.common_storage_lib.resources.fluid.ingredient.FluidIngredientRegistry;

public class ResourceLib {
    public static final String MOD_ID = "common_storage_lib_resources";

    public static void init() {
        FluidIngredientRegistry.init();
        EntityIngredientRegistry.init();
    }
}
