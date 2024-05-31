package earth.terrarium.common_storage_lib.testmod;

import earth.terrarium.common_storage_lib.testmod.blocks.TransferTestBlock;
import earth.terrarium.common_storage_lib.testmod.items.TransferTestItem;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

@Mod(TestMod.MOD_ID)
public class NeoTestMod {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, TestMod.MOD_ID);
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(BuiltInRegistries.BLOCK, TestMod.MOD_ID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(BuiltInRegistries.ITEM, TestMod.MOD_ID);

    public static final Supplier<TransferTestItem> TRANSFER_ITEM = ITEMS.register("test_item", () -> new TransferTestItem(new Item.Properties().stacksTo(1)));
    public static final Supplier<TransferTestBlock> TRANSFER_BLOCK = BLOCKS.register("test_block", () -> new TransferTestBlock(BlockBehaviour.Properties.of()));
    public static final Supplier<BlockItem> TRANSFER_BLOCK_ITEM = ITEMS.register("test_block", () -> new BlockItem(TRANSFER_BLOCK.get(), new Item.Properties().stacksTo(1)));

    public NeoTestMod(IEventBus bus) {
        TestMod.init();
        BLOCKS.register(bus);
        ITEMS.register(bus);
        BLOCK_ENTITY_TYPES.register(bus);
    }
}
