package earth.terrarium.botarium.energy;

import earth.terrarium.botarium.context.ItemContext;
import earth.terrarium.botarium.lookup.BlockLookup;
import earth.terrarium.botarium.lookup.EntityLookup;
import earth.terrarium.botarium.lookup.ItemLookup;
import earth.terrarium.botarium.storage.base.ValueStorage;
import net.minecraft.core.Direction;
import net.msrandom.multiplatform.annotations.Expect;

@Expect
public class EnergyApi {
    public static final BlockLookup<ValueStorage, Direction> BLOCK;
    public static final ItemLookup<ValueStorage, ItemContext> ITEM;
    public static final EntityLookup<ValueStorage, Direction> ENTITY;
}
