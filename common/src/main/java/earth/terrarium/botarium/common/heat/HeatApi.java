package earth.terrarium.botarium.common.heat;

import earth.terrarium.botarium.Botarium;
import earth.terrarium.botarium.common.context.ItemContext;
import earth.terrarium.botarium.common.lookup.BlockLookup;
import earth.terrarium.botarium.common.lookup.EntityLookup;
import earth.terrarium.botarium.common.lookup.ItemLookup;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;

public class HeatApi {
    public static final BlockLookup<HeatContainer, Direction> BLOCK = BlockLookup.create(new ResourceLocation(Botarium.MOD_ID, "heat_block"), HeatContainer.class);
    public static final ItemLookup<HeatContainer, ItemContext> ITEM = ItemLookup.create(new ResourceLocation(Botarium.MOD_ID, "heat_item"), HeatContainer.class);
    public static final EntityLookup<HeatContainer, Void> ENTITY = EntityLookup.create(new ResourceLocation(Botarium.MOD_ID, "heat_entity"), HeatContainer.class);
}
