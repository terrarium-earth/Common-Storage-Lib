package earth.terrarium.botarium.common.fluid;

import earth.terrarium.botarium.common.fluid.base.FluidContainer;
import earth.terrarium.botarium.common.lookup.BlockLookup;
import earth.terrarium.botarium.common.lookup.EntityLookup;
import earth.terrarium.botarium.common.lookup.ItemLookup;
import earth.terrarium.botarium.common.context.ItemContext;
import net.minecraft.core.Direction;
import net.msrandom.multiplatform.annotations.Actual;
import org.jetbrains.annotations.Nullable;

@Actual
public class FluidApiActual {
    @Actual
    public static final BlockLookup<FluidContainer, @Nullable Direction> BLOCK = null;
    @Actual
    public static final ItemLookup<FluidContainer, ItemContext> ITEM = null;
    @Actual
    public static final EntityLookup<FluidContainer, Void> ENTITY = null;
}
