package earth.terrarium.botarium.common.fluid;

import earth.terrarium.botarium.common.fluid.base.FluidContainer;
import earth.terrarium.botarium.common.lookup.base.BlockContainerLookup;
import earth.terrarium.botarium.common.lookup.base.EntityContainerLookup;
import earth.terrarium.botarium.common.lookup.base.ItemContainerLookup;
import earth.terrarium.botarium.common.context.ItemContext;
import net.minecraft.core.Direction;
import net.msrandom.multiplatform.annotations.Actual;
import org.jetbrains.annotations.Nullable;

@Actual
public class FluidApiActual {
    @Actual
    public static final BlockContainerLookup<FluidContainer, @Nullable Direction> BLOCK = null;
    @Actual
    public static final ItemContainerLookup<FluidContainer, ItemContext> ITEM = null;
    @Actual
    public static final EntityContainerLookup<FluidContainer, Void> ENTITY = null;
}
