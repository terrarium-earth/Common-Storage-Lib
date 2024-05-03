package earth.terrarium.botarium.testmod;

import earth.terrarium.botarium.testmod.blockentities.TransferTestBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.msrandom.multiplatform.annotations.Expect;

import java.util.function.Supplier;

public class TestMod {
    public static final String MOD_ID = "botarium_test_mod";

    public static final Supplier<BlockEntityType<TransferTestBlockEntity>> TRANSFER_BLOCK_ENTITY = registerBlockEntity("transfer_block_entity", TransferTestBlockEntity::new);

    @Expect
    public static <T extends BlockEntity> Supplier<BlockEntityType<T>> registerBlockEntity(String name, BlockEntityType.BlockEntitySupplier<T> supplier);

    public static void init() {
    }
}
