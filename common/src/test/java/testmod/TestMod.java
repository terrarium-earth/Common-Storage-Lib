package testmod;

import earth.terrarium.botarium.api.registry.RegistryHelpers;
import earth.terrarium.botarium.api.registry.RegistryHolder;
import net.minecraft.core.Registry;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.Supplier;

public class TestMod {
    public static final String MODID = "testmod";

    public static final RegistryHolder<BlockEntityType<?>> BLOCK_ENTITIES = new RegistryHolder<>(Registry.BLOCK_ENTITY_TYPE, MODID);
    public static final RegistryHolder<Block> BLOCKS = new RegistryHolder<>(Registry.BLOCK, MODID);
    public static final RegistryHolder<Item> ITEMS = new RegistryHolder<>(Registry.ITEM, MODID);

    public static final Supplier<Block> EXAMPLE_BLOCK = BLOCKS.register("block", () -> new ExampleBlock(BlockBehaviour.Properties.copy(Blocks.STONE)));
    public static final Supplier<BlockEntityType<?>> EXAMPLE_BLOCK_ENTITY = BLOCK_ENTITIES.register("block", () -> RegistryHelpers.createBlockEntityType(ExampleBlockEntity::new, EXAMPLE_BLOCK.get()));
    public static final Supplier<BlockItem> EXAMPLE_BLOCK_ITEM = ITEMS.register("block", () -> new BlockItem(EXAMPLE_BLOCK.get(), new Item.Properties()));

    public static final Supplier<Block> EXAMPLE_PIPE = BLOCKS.register("north_to_south_pipe", () -> new ExamplePipe(BlockBehaviour.Properties.copy(Blocks.STONE)));
    public static final Supplier<BlockEntityType<?>> EXAMPLE_PIPE_BLOCK_ENTITY = BLOCK_ENTITIES.register("north_to_south_pipe", () -> RegistryHelpers.createBlockEntityType(ExampleN2SPipeBlockEntity::new, EXAMPLE_PIPE.get()));
    public static final Supplier<BlockItem> EXAMPLE_PIPE_ITEM = ITEMS.register("north_to_south_pipe", () -> new BlockItem(EXAMPLE_PIPE.get(), new Item.Properties()));

    public static final Supplier<Item> EXAMPLE_ITEM = ITEMS.register("item", () -> new ExampleItem(new Item.Properties().stacksTo(1)));

    public static void init() {
        BLOCK_ENTITIES.initialize();
        BLOCKS.initialize();
        ITEMS.initialize();
    }
}
