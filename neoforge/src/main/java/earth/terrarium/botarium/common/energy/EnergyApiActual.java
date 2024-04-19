package earth.terrarium.botarium.common.energy;

import earth.terrarium.botarium.common.context.ItemContext;
import earth.terrarium.botarium.common.lookup.BlockLookup;
import earth.terrarium.botarium.common.lookup.EntityLookup;
import earth.terrarium.botarium.common.lookup.ItemLookup;
import earth.terrarium.botarium.common.storage.base.LongContainer;
import net.minecraft.core.Direction;
import net.msrandom.multiplatform.annotations.Actual;

@Actual
public class EnergyApiActual {
    @Actual
    public static final BlockLookup<LongContainer, Direction> BLOCK;
    @Actual
    public static final ItemLookup<LongContainer, ItemContext> ITEM;
    @Actual
    public static final EntityLookup<LongContainer, Void> ENTITY;
}