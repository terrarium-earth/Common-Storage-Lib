package earth.terrarium.botarium.forge.extensions;

import earth.terrarium.botarium.common.registry.fluid.BotariumSourceFluid;
import earth.terrarium.botarium.common.registry.fluid.FluidData;
import earth.terrarium.botarium.forge.regsitry.fluid.ForgeFluidAttributesHandler;
import earth.terrarium.botarium.forge.regsitry.fluid.ForgeFluidData;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.msrandom.extensions.annotations.ClassExtension;
import net.msrandom.extensions.annotations.ExtensionInjectedElement;
import net.msrandom.extensions.annotations.ImplementsBaseElement;
import org.jetbrains.annotations.NotNull;

@ClassExtension(BotariumSourceFluid.class)
public abstract class BotariumSourceFluidImpl extends ForgeFlowingFluid.Source {

    @ExtensionInjectedElement
    private final FluidData data;

    @ImplementsBaseElement
    public BotariumSourceFluidImpl(FluidData data) {
        super(ForgeFluidAttributesHandler.propertiesFromFluidProperties(data));
        this.data = data;
        data.setStillFluid(() -> this);
    }

    @ImplementsBaseElement
    public FluidData getData() {
        return data;
    }
}
