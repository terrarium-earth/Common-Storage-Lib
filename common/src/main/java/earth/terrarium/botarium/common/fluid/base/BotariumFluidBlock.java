package earth.terrarium.botarium.common.fluid.base;

import earth.terrarium.botarium.util.Updatable;
import net.minecraft.world.level.block.entity.BlockEntity;

public interface BotariumFluidBlock<T extends FluidContainer & Updatable<BlockEntity>> {

    /**
     * @return The {@link ItemFluidContainer} for the block.
     */
    T getFluidContainer();
}