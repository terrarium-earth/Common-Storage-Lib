package earth.terrarium.botarium.forge.extensions;

import earth.terrarium.botarium.common.registry.fluid.BotariumFlowingFluid;
import earth.terrarium.botarium.common.registry.fluid.FluidData;
import earth.terrarium.botarium.forge.regsitry.fluid.ForgeFluidAttributesHandler;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.msrandom.extensions.annotations.ClassExtension;
import net.msrandom.extensions.annotations.ExtensionInjectedElement;
import net.msrandom.extensions.annotations.ImplementsBaseElement;
import org.jetbrains.annotations.NotNull;

@ClassExtension(BotariumFlowingFluid.class)
public abstract class BotariumFlowingFluidImpl extends ForgeFlowingFluid.Flowing {

    @ExtensionInjectedElement
    private final FluidData data;

    @ImplementsBaseElement
    public BotariumFlowingFluidImpl(FluidData data) {
        super(ForgeFluidAttributesHandler.propertiesFromFluidProperties(data));
        this.data = data;
        registerDefaultState(getStateDefinition().any().setValue(LEVEL, 7));
        data.setFlowingFluid(() -> this);
    }

    @ImplementsBaseElement
    public FluidData getData() {
        return data;
    }
}
