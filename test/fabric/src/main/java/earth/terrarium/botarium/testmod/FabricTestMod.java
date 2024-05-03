package earth.terrarium.botarium.testmod;

import earth.terrarium.botarium.testmod.blocks.TransferTestBlock;
import earth.terrarium.botarium.testmod.items.TransferTestItem;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class FabricTestMod {
    public static final TransferTestBlock TRANSFER_BLOCK = Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(TestMod.MOD_ID, "test_block"), new TransferTestBlock(BlockBehaviour.Properties.of()));
    public static final TransferTestItem TRANSFER_ITEM = Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(TestMod.MOD_ID, "test_item"), new TransferTestItem(new Item.Properties().stacksTo(1)));
    public static final BlockItem TRANSFER_BLOCK_ITEM = Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(TestMod.MOD_ID, "test_block"), new BlockItem(TRANSFER_BLOCK, new Item.Properties()));

    public static void init() {
        TestMod.init();
    }
}
