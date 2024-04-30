package earth.terrarium.botarium.energy;

import earth.terrarium.botarium.context.ItemContext;
import earth.terrarium.botarium.energy.lookup.EnergyBlockLookup;
import earth.terrarium.botarium.energy.lookup.EnergyEntityLookup;
import earth.terrarium.botarium.energy.lookup.EnergyItemLookup;
import earth.terrarium.botarium.lookup.BlockLookup;
import earth.terrarium.botarium.lookup.EntityLookup;
import earth.terrarium.botarium.lookup.ItemLookup;
import earth.terrarium.botarium.storage.base.ValueStorage;
import net.minecraft.core.Direction;
import net.msrandom.multiplatform.annotations.Actual;

@Actual
public class EnergyApiActual {
    @Actual
    public static final BlockLookup<ValueStorage, Direction> BLOCK = EnergyBlockLookup.INSTANCE;
    @Actual
    public static final ItemLookup<ValueStorage, ItemContext> ITEM = EnergyItemLookup.INSTANCE;
    @Actual
    public static final EntityLookup<ValueStorage, Direction> ENTITY = EnergyEntityLookup.INSTANCE;
}
