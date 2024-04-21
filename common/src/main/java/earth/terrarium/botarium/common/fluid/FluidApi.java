package earth.terrarium.botarium.common.fluid;

import earth.terrarium.botarium.common.context.ItemContext;
import earth.terrarium.botarium.common.energy.EnergyProvider;
import earth.terrarium.botarium.common.lookup.BlockLookup;
import earth.terrarium.botarium.common.lookup.EntityLookup;
import earth.terrarium.botarium.common.lookup.ItemLookup;
import earth.terrarium.botarium.common.storage.base.LongContainer;
import earth.terrarium.botarium.common.storage.base.UnitContainer;
import earth.terrarium.botarium.common.transfer.impl.FluidUnit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.msrandom.multiplatform.annotations.Expect;
import org.jetbrains.annotations.Nullable;

import java.util.logging.Level;

@Expect
public class FluidApi {
    public static final BlockLookup<UnitContainer<FluidUnit>, @Nullable Direction> BLOCK;
    public static final ItemLookup<UnitContainer<FluidUnit>, ItemContext> ITEM;
    public static final EntityLookup<UnitContainer<FluidUnit>, Direction> ENTITY;
}
