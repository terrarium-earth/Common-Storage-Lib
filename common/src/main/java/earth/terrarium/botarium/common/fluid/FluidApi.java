package earth.terrarium.botarium.common.fluid;

import earth.terrarium.botarium.common.context.ItemContext;
import earth.terrarium.botarium.common.fluid.base.FluidContainer;
import earth.terrarium.botarium.common.lookup.BlockLookup;
import earth.terrarium.botarium.common.lookup.EntityLookup;
import earth.terrarium.botarium.common.lookup.ItemLookup;
import net.minecraft.core.Direction;
import net.msrandom.multiplatform.annotations.Expect;
import org.jetbrains.annotations.Nullable;

@Expect
public class FluidApi {

    public static final BlockLookup<FluidContainer, @Nullable Direction> BLOCK;

    public static final ItemLookup<FluidContainer, ItemContext> ITEM;

    public static final EntityLookup<FluidContainer, Void> ENTITY;
}
