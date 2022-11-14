package earth.terrarium.botarium.forge.extensions;

import earth.terrarium.botarium.api.registry.fluid.BotariumLiquidBlock;
import earth.terrarium.botarium.api.registry.fluid.FluidData;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.msrandom.extensions.annotations.ClassExtension;
import net.msrandom.extensions.annotations.ImplementsBaseElement;

@ClassExtension(BotariumLiquidBlock.class)
public class BoatariumLiquidBlockImpl extends LiquidBlock {

    @ImplementsBaseElement
    public BoatariumLiquidBlockImpl(FluidData data, BlockBehaviour.Properties properties) {
        super(() -> data.getFlowingFluid().get(), properties);
        data.setBlock(() -> this);
    }
}

