package earth.terrarium.botarium;

import net.minecraft.resources.ResourceLocation;

public class Botarium {
    public static final String MOD_ID = "botarium";

    public static ResourceLocation id(String path) {
        return new ResourceLocation(MOD_ID, path);
    }

    public static void init() {}
}