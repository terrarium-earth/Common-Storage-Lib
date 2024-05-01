package earth.terrarium.botarium.resources;

import earth.terrarium.botarium.resources.entity.ingredient.EntityIngredientRegistry;
import earth.terrarium.botarium.resources.fluid.ingredient.FluidIngredientRegistry;

public class BotariumResources {
    public static final String MOD_ID = "botarium_resources";

    public static void init() {
        FluidIngredientRegistry.init();
        EntityIngredientRegistry.init();
    }
}
