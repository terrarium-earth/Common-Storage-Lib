package earth.terrarium.botarium.common.fluid;

import earth.terrarium.botarium.common.lookup.BlockLookup;
import earth.terrarium.botarium.common.lookup.EntityLookup;
import earth.terrarium.botarium.common.lookup.ItemLookup;
import earth.terrarium.botarium.common.context.ItemContext;
import earth.terrarium.botarium.common.storage.base.UnitContainer;
import earth.terrarium.botarium.common.transfer.impl.FluidUnit;
import net.minecraft.core.Direction;
import net.msrandom.multiplatform.annotations.Actual;
import org.jetbrains.annotations.Nullable;

@Actual
public class FluidApiActual {
    @Actual
    public static final BlockLookup<UnitContainer<FluidUnit>, @Nullable Direction> BLOCK = null;
    @Actual
    public static final ItemLookup<UnitContainer<FluidUnit>, ItemContext> ITEM = null;
    @Actual
    public static final EntityLookup<UnitContainer<FluidUnit>, Void> ENTITY = null;
}
