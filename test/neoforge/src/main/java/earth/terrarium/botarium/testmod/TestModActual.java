package earth.terrarium.botarium.testmod;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.msrandom.multiplatform.annotations.Actual;

import java.util.function.Supplier;

public class TestModActual {
    @Actual
    public static <T extends BlockEntity> Supplier<BlockEntityType<T>> registerBlockEntity(String name, BlockEntityType.BlockEntitySupplier<T> supplier) {
        return NeoTestMod.BLOCK_ENTITY_TYPES.register(name, () -> BlockEntityType.Builder.of(supplier, NeoTestMod.TRANSFER_BLOCK.get()).build(null));
    }
}
