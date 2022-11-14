package earth.terrarium.botarium.api.registry.fluid;

import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.FlowingFluid;
import net.msrandom.extensions.annotations.ImplementedByExtension;
import org.apache.commons.lang3.NotImplementedException;

public class BotariumLiquidBlock extends LiquidBlock {

    @ImplementedByExtension
    public BotariumLiquidBlock(FluidData data, Properties properties) {
        super((FlowingFluid) null, null);
        throw new NotImplementedException();
    }
}
