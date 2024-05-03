package earth.terrarium.botarium.testmod;

import earth.terrarium.botarium.testmod.blocks.TransferTestBlock;
import earth.terrarium.botarium.testmod.items.TransferTestItem;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.msrandom.multiplatform.annotations.Actual;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class TestModActual {
    @Actual
    public static <T extends BlockEntity> Supplier<BlockEntityType<T>> registerBlockEntity(String name, BlockEntityType.BlockEntitySupplier<T> supplier) {
        return NeoTestMod.BLOCK_ENTITY_TYPES.register(name, () -> BlockEntityType.Builder.of(supplier, NeoTestMod.TRANSFER_BLOCK.get()).build(null));
    }
}
