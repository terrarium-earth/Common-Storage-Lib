package earth.terrarium.botarium.fabric.extensions;

import earth.terrarium.botarium.common.registry.fluid.BotariumLiquidBlock;
import earth.terrarium.botarium.common.registry.fluid.FluidData;
import net.minecraft.world.level.block.LiquidBlock;
import net.msrandom.extensions.annotations.ClassExtension;
import net.msrandom.extensions.annotations.ImplementsBaseElement;

@ClassExtension(BotariumLiquidBlock.class)
public class BoatariumLiquidBlockImpl extends LiquidBlock {

    @ImplementsBaseElement
    public BoatariumLiquidBlockImpl(FluidData data, Properties properties) {
        super(data.getFlowingFluid().get(), properties);
        data.setBlock(() -> this);
    }
}

