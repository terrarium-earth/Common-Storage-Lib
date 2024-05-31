package earth.terrarium.common_storage_lib.energy;

import earth.terrarium.common_storage_lib.context.ItemContext;
import earth.terrarium.common_storage_lib.energy.lookup.EnergyBlockLookup;
import earth.terrarium.common_storage_lib.energy.lookup.EnergyEntityLookup;
import earth.terrarium.common_storage_lib.energy.lookup.EnergyItemLookup;
import earth.terrarium.common_storage_lib.lookup.BlockLookup;
import earth.terrarium.common_storage_lib.lookup.EntityLookup;
import earth.terrarium.common_storage_lib.lookup.ItemLookup;
import earth.terrarium.common_storage_lib.storage.base.ValueStorage;
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
