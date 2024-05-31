package earth.terrarium.common_storage_lib.energy;

import earth.terrarium.common_storage_lib.context.ItemContext;
import earth.terrarium.common_storage_lib.lookup.BlockLookup;
import earth.terrarium.common_storage_lib.lookup.EntityLookup;
import earth.terrarium.common_storage_lib.lookup.ItemLookup;
import earth.terrarium.common_storage_lib.storage.base.ValueStorage;
import net.minecraft.core.Direction;
import net.msrandom.multiplatform.annotations.Expect;

@Expect
public class EnergyApi {
    public static final BlockLookup<ValueStorage, Direction> BLOCK;
    public static final ItemLookup<ValueStorage, ItemContext> ITEM;
    public static final EntityLookup<ValueStorage, Direction> ENTITY;
}
