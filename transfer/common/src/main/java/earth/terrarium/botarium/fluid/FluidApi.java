package earth.terrarium.botarium.fluid;

import earth.terrarium.botarium.context.ItemContext;
import earth.terrarium.botarium.fluid.base.FluidUnit;
import earth.terrarium.botarium.lookup.BlockLookup;
import earth.terrarium.botarium.lookup.EntityLookup;
import earth.terrarium.botarium.lookup.ItemLookup;
import earth.terrarium.botarium.storage.base.CommonStorage;
import net.minecraft.core.Direction;
import net.msrandom.multiplatform.annotations.Expect;
import org.jetbrains.annotations.Nullable;

@Expect
public class FluidApi {
    public static final BlockLookup<CommonStorage<FluidUnit>, @Nullable Direction> BLOCK;
    public static final ItemLookup<CommonStorage<FluidUnit>, ItemContext> ITEM;
    public static final EntityLookup<CommonStorage<FluidUnit>, Direction> ENTITY;
}
