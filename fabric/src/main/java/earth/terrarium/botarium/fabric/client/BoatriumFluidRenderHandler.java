package earth.terrarium.botarium.fabric.client;

import earth.terrarium.botarium.api.registry.fluid.FluidProperties;
import net.fabricmc.fabric.api.client.render.fluid.v1.SimpleFluidRenderHandler;

public class BoatriumFluidRenderHandler extends SimpleFluidRenderHandler {

    public BoatriumFluidRenderHandler(FluidProperties properties) {
        super(properties.still(), properties.flowing(), properties.overlay(), properties.tintColor());
    }
}
