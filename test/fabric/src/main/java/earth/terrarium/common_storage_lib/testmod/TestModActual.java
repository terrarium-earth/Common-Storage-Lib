package earth.terrarium.common_storage_lib.testmod;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.msrandom.multiplatform.annotations.Actual;

import java.util.function.Supplier;

public class TestModActual {
    @Actual
    public static <T extends BlockEntity> Supplier<BlockEntityType<T>> registerBlockEntity(String name, BlockEntityType.BlockEntitySupplier<T> supplier) {
        final BlockEntityType<T> register = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(TestMod.MOD_ID, name), BlockEntityType.Builder.of(supplier, FabricTestMod.TRANSFER_BLOCK).build(null));
        return () -> register;
    }
}
