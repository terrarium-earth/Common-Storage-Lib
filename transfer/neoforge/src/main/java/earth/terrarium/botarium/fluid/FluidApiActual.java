package earth.terrarium.botarium.fluid;

import earth.terrarium.botarium.context.ItemContext;
import earth.terrarium.botarium.resources.fluid.FluidResource;
import earth.terrarium.botarium.fluid.lookup.FluidBlockLookup;
import earth.terrarium.botarium.fluid.lookup.FluidEntityLookup;
import earth.terrarium.botarium.fluid.lookup.FluidItemLookup;
import earth.terrarium.botarium.lookup.BlockLookup;
import earth.terrarium.botarium.lookup.EntityLookup;
import earth.terrarium.botarium.lookup.ItemLookup;
import earth.terrarium.botarium.storage.base.CommonStorage;
import net.minecraft.core.Direction;
import net.msrandom.multiplatform.annotations.Actual;
import org.jetbrains.annotations.Nullable;

@Actual
public class FluidApiActual {
    @Actual
    public static final BlockLookup<CommonStorage<FluidResource>, @Nullable Direction> BLOCK = FluidBlockLookup.INSTANCE;
    @Actual
    public static final ItemLookup<CommonStorage<FluidResource>, ItemContext> ITEM = FluidItemLookup.INSTANCE;
    @Actual
    public static final EntityLookup<CommonStorage<FluidResource>, Direction> ENTITY = FluidEntityLookup.INSTANCE;
}
