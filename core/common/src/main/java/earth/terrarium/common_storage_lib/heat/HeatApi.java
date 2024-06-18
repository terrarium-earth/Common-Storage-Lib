package earth.terrarium.common_storage_lib.heat;

import earth.terrarium.common_storage_lib.CommonStorageLib;
import earth.terrarium.common_storage_lib.context.ItemContext;
import earth.terrarium.common_storage_lib.lookup.BlockLookup;
import earth.terrarium.common_storage_lib.lookup.EntityLookup;
import earth.terrarium.common_storage_lib.lookup.ItemLookup;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;

public class HeatApi {
    public static final BlockLookup<HeatContainer, Direction> BLOCK = BlockLookup.create(ResourceLocation.fromNamespaceAndPath(CommonStorageLib.MOD_ID, "heat_block"), HeatContainer.class);
    public static final ItemLookup<HeatContainer, ItemContext> ITEM = ItemLookup.create(ResourceLocation.fromNamespaceAndPath(CommonStorageLib.MOD_ID, "heat_item"), HeatContainer.class, ItemContext.class);
    public static final EntityLookup<HeatContainer, Void> ENTITY = EntityLookup.create(ResourceLocation.fromNamespaceAndPath(CommonStorageLib.MOD_ID, "heat_entity"), HeatContainer.class);
}
