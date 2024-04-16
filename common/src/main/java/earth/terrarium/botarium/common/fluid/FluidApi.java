package earth.terrarium.botarium.common.fluid;

import earth.terrarium.botarium.common.context.ItemContext;
import earth.terrarium.botarium.common.fluid.base.FluidContainer;
import earth.terrarium.botarium.common.lookup.base.BlockContainerLookup;
import earth.terrarium.botarium.common.lookup.base.EntityContainerLookup;
import earth.terrarium.botarium.common.lookup.base.ItemContainerLookup;
import net.minecraft.core.Direction;
import net.msrandom.multiplatform.annotations.Expect;
import org.jetbrains.annotations.Nullable;

@Expect
public class FluidApi {
    public static final BlockContainerLookup<FluidContainer, @Nullable Direction> BLOCK;

    public static final ItemContainerLookup<FluidContainer, ItemContext> ITEM;

    public static final EntityContainerLookup<FluidContainer, Void> ENTITY;
}
