package earth.terrarium.botarium.testmod;

import earth.terrarium.botarium.testmod.blockentities.TransferTestBlockEntity;
import earth.terrarium.botarium.testmod.blocks.TransferTestBlock;
import earth.terrarium.botarium.testmod.items.TransferTestItem;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.msrandom.multiplatform.annotations.Actual;
import net.msrandom.multiplatform.annotations.Expect;

import java.util.function.Supplier;

public class TestModActual {
    @Actual
    public static <T extends BlockEntity> Supplier<BlockEntityType<T>> registerBlockEntity(String name, BlockEntityType.BlockEntitySupplier<T> supplier) {
        BlockEntityType<T> register = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, new ResourceLocation(TestMod.MOD_ID, name), BlockEntityType.Builder.of(supplier, FabricTestMod.TRANSFER_BLOCK).build(null));
        return () -> register;
    }
}
