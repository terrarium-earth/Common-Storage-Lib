package earth.terrarium.botarium.api.registry.fluid;

import net.msrandom.extensions.annotations.ImplementedByExtension;
import org.apache.commons.lang3.NotImplementedException;

public class FluidRegistry {

    @ImplementedByExtension
    public FluidRegistry(String modid) {
        throw new NotImplementedException();
    }

    @ImplementedByExtension
    public FluidData register(FluidProperties properties) {
        throw new NotImplementedException();
    }

    @ImplementedByExtension
    public FluidData register(String id, FluidProperties.Builder properties) {
        throw new NotImplementedException();
    }

    @ImplementedByExtension
    public void initialize() {
        throw new NotImplementedException();
    }

}
