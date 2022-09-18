package earth.terrarium.botarium.api.fluid;

import java.util.List;

public class SimpleFluidSnapshot implements FluidSnapshot {
    private final List<FluidHolder> fluids;

    public SimpleFluidSnapshot(FluidContainer fluidContainer) {
        this.fluids = fluidContainer.getFluids();
    }

    @Override
    public void loadSnapshot(FluidContainer container) {
        for (int i = 0; i < Math.min(container.getSize(), fluids.size()); i++) {
            container.getFluids().set(i, fluids.get(i).copyHolder());
        }
    }
}