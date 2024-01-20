package earth.terrarium.botarium.impl.extensions;

import earth.terrarium.botarium.common.registry.fluid.BotariumFlowingFluid;
import earth.terrarium.botarium.common.registry.fluid.FluidData;
import net.minecraft.world.level.material.FlowingFluid;
import net.msrandom.extensions.annotations.ClassExtension;
import net.msrandom.extensions.annotations.ExtensionInjectedElement;
import net.msrandom.extensions.annotations.ImplementsBaseElement;

@ClassExtension(BotariumFlowingFluid.class)
public abstract class BotariumFlowingFluidImpl extends FlowingFluid {

    @ExtensionInjectedElement
    private final FluidData data;

    @ImplementsBaseElement
    public BotariumFlowingFluidImpl(FluidData data) {
        this.data = data;
        registerDefaultState(getStateDefinition().any().setValue(LEVEL, 7));
        data.setFlowingFluid(() -> this);
    }

    @ImplementsBaseElement
    public FluidData getData() {
        return data;
    }

}
