package testmod;

import earth.terrarium.botarium.common.registry.RegistryHelpers;
import earth.terrarium.botarium.common.registry.RegistryHolder;
import earth.terrarium.botarium.common.registry.fluid.*;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Fluid;

import java.util.function.Supplier;

public class TestMod {
    public static final String MOD_ID = "testmod";

    public static final RegistryHolder<BlockEntityType<?>> BLOCK_ENTITIES = new RegistryHolder<>(Registry.BLOCK_ENTITY_TYPE, MOD_ID);
    public static final RegistryHolder<Block> BLOCKS = new RegistryHolder<>(Registry.BLOCK, MOD_ID);
    public static final RegistryHolder<Item> ITEMS = new RegistryHolder<>(Registry.ITEM, MOD_ID);

    public static final RegistryHolder<Fluid> FLUIDS = new RegistryHolder<>(Registry.FLUID, MOD_ID);
    public static final FluidRegistry FLUID_TYPES = new FluidRegistry(MOD_ID);


    public static final Supplier<Block> EXAMPLE_BLOCK = BLOCKS.register("block", () -> new TestBlock(BlockBehaviour.Properties.copy(Blocks.STONE)));
    public static final Supplier<BlockEntityType<?>> EXAMPLE_BLOCK_ENTITY = BLOCK_ENTITIES.register("block", () -> RegistryHelpers.createBlockEntityType(TestBlockEntity::new, EXAMPLE_BLOCK.get()));
    public static final Supplier<BlockItem> EXAMPLE_BLOCK_ITEM = ITEMS.register("block", () -> new BlockItem(EXAMPLE_BLOCK.get(), new Item.Properties()));
    public static final Supplier<Item> EXAMPLE_ITEM = ITEMS.register("item", () -> new TestItem(new Item.Properties().stacksTo(1)));

    public static final Supplier<Block> EXAMPLE_PIPE = BLOCKS.register("pipe", () -> new ExamplePipe(BlockBehaviour.Properties.copy(Blocks.STONE)));
    public static final Supplier<BlockEntityType<?>> EXAMPLE_PIPE_ENTITY = BLOCK_ENTITIES.register("pipe", () -> RegistryHelpers.createBlockEntityType(ExampleN2SPipeBlockEntity::new, EXAMPLE_PIPE.get()));
    public static final Supplier<BlockItem> EXAMPLE_PIPE_ITEM = ITEMS.register("pipe", () -> new BlockItem(EXAMPLE_PIPE.get(), new Item.Properties()));

    public static final FluidData TEST_FLUID = FLUID_TYPES.register("test", FluidProperties.create()
            .still(new ResourceLocation("minecraft:block/water_still"))
            .flowing(new ResourceLocation("minecraft:block/water_flow"))
            .overlay(new ResourceLocation("minecraft:block/water_overlay"))
            .screenOverlay(new ResourceLocation("textures/misc/underwater.png"))
            .tintColor(0x00FF00)
            .sounds("bucket_empty", SoundEvents.AXE_STRIP)
            .sounds("bucket_fill", SoundEvents.GLASS_BREAK)
    );

    public static final Supplier<Fluid> TEST_FLUID_SOURCE = FLUIDS.register("test_fluid", () -> new BotariumSourceFluid(TEST_FLUID));
    public static final Supplier<Fluid> TEST_FLUID_FLOWING = FLUIDS.register("test_fluid_flowing", () -> new BotariumFlowingFluid(TEST_FLUID));
    public static final Supplier<Block> TEST_FLUID_BLOCK = BLOCKS.register("test_fluid_block", () -> new BotariumLiquidBlock(TEST_FLUID, BlockBehaviour.Properties.copy(Blocks.WATER)));
    public static final Supplier<FluidBucketItem> TEST_BUCKET = ITEMS.register("test_bucket", () -> new FluidBucketItem(TEST_FLUID, new Item.Properties()));

    public static void init() {
        BLOCK_ENTITIES.initialize();
        BLOCKS.initialize();
        ITEMS.initialize();

        FLUID_TYPES.initialize();
        FLUIDS.initialize();
    }
}
