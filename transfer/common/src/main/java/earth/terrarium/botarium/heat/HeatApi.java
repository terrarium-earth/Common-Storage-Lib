package earth.terrarium.botarium.heat;

import earth.terrarium.botarium.BotariumStorage;
import earth.terrarium.botarium.context.ItemContext;
import earth.terrarium.botarium.lookup.BlockLookup;
import earth.terrarium.botarium.lookup.EntityLookup;
import earth.terrarium.botarium.lookup.ItemLookup;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;

public class HeatApi {
    public static final BlockLookup<HeatContainer, Direction> BLOCK = BlockLookup.create(new ResourceLocation(BotariumStorage.MOD_ID, "heat_block"), HeatContainer.class);
    public static final ItemLookup<HeatContainer, ItemContext> ITEM = ItemLookup.create(new ResourceLocation(BotariumStorage.MOD_ID, "heat_item"), HeatContainer.class, ItemContext.class);
    public static final EntityLookup<HeatContainer, Void> ENTITY = EntityLookup.create(new ResourceLocation(BotariumStorage.MOD_ID, "heat_entity"), HeatContainer.class);
}
