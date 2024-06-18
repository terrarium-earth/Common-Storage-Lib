package earth.terrarium.common_storage_lib.energy;


import earth.terrarium.common_storage_lib.CommonStorageLib;
import earth.terrarium.common_storage_lib.context.ItemContext;
import earth.terrarium.common_storage_lib.energy.lookup.EnergyBlockLookup;
import earth.terrarium.common_storage_lib.energy.lookup.EnergyItemLookup;
import earth.terrarium.common_storage_lib.lookup.BlockLookup;
import earth.terrarium.common_storage_lib.lookup.EntityLookup;
import earth.terrarium.common_storage_lib.lookup.ItemLookup;
import earth.terrarium.common_storage_lib.storage.base.ValueStorage;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.msrandom.multiplatform.annotations.Actual;

@Actual
public class EnergyApiActual {
    @Actual
    public static final BlockLookup<ValueStorage, Direction> BLOCK = new EnergyBlockLookup();
    @Actual
    public static final ItemLookup<ValueStorage, ItemContext> ITEM = new EnergyItemLookup();
    @Actual
    public static final EntityLookup<ValueStorage, Direction> ENTITY = EntityLookup.create(ResourceLocation.fromNamespaceAndPath(CommonStorageLib.MOD_ID, "entity_energy"), ValueStorage.class, Direction.class);
}
