package earth.terrarium.botarium.common.fluid;

import earth.terrarium.botarium.common.context.ItemContext;
import earth.terrarium.botarium.common.lookup.BlockLookup;
import earth.terrarium.botarium.common.lookup.EntityLookup;
import earth.terrarium.botarium.common.lookup.ItemLookup;
import earth.terrarium.botarium.common.storage.base.UnitContainer;
import earth.terrarium.botarium.common.transfer.impl.FluidUnit;
import net.minecraft.core.Direction;
import net.msrandom.multiplatform.annotations.Expect;
import org.jetbrains.annotations.Nullable;

@Expect
public class FluidApi {
    public static final BlockLookup<UnitContainer<FluidUnit>, @Nullable Direction> BLOCK;
    public static final ItemLookup<UnitContainer<FluidUnit>, ItemContext> ITEM;
    public static final EntityLookup<UnitContainer<FluidUnit>, Void> ENTITY;
}
