package earth.terrarium.botarium.neoforge.extensions;

import earth.terrarium.botarium.common.registry.fluid.BotariumSourceFluid;
import earth.terrarium.botarium.common.registry.fluid.FluidData;
import earth.terrarium.botarium.neoforge.regsitry.fluid.ForgeFluidData;
import net.minecraft.world.level.material.FlowingFluid;
import net.msrandom.extensions.annotations.ClassExtension;
import net.msrandom.extensions.annotations.ExtensionInjectedElement;
import net.msrandom.extensions.annotations.ImplementsBaseElement;
import net.neoforged.neoforge.fluids.FluidType;
import org.jetbrains.annotations.NotNull;

@ClassExtension(BotariumSourceFluid.class)
public abstract class BotariumSourceFluidImpl extends FlowingFluid {

    @ExtensionInjectedElement
    private final FluidData data;

    @ImplementsBaseElement
    public BotariumSourceFluidImpl(FluidData data) {
        this.data = data;
        data.setStillFluid(() -> this);
    }

    @ImplementsBaseElement
    public FluidData getData() {
        return data;
    }

    @Override
    public @NotNull FluidType getFluidType() {
        if (data instanceof ForgeFluidData forgeHolder) {
            return forgeHolder.getType().get();
        }
        return super.getFluidType();
    }
}
