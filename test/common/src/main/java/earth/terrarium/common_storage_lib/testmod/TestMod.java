package earth.terrarium.common_storage_lib.testmod;

import com.mojang.serialization.Codec;
import earth.terrarium.common_storage_lib.data.DataManager;
import earth.terrarium.common_storage_lib.data.DataManagerRegistry;
import earth.terrarium.common_storage_lib.fluid.util.FluidStorageData;
import earth.terrarium.common_storage_lib.item.util.ItemStorageData;
import earth.terrarium.common_storage_lib.resources.ResourceStack;
import earth.terrarium.common_storage_lib.resources.fluid.FluidResource;
import earth.terrarium.common_storage_lib.resources.item.ItemResource;
import earth.terrarium.common_storage_lib.testmod.blockentities.TransferTestBlockEntity;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.msrandom.multiplatform.annotations.Expect;

import java.util.function.Supplier;

public class TestMod {
    public static final String MOD_ID = "test_mod";

    public static final DataManagerRegistry REGISTRY = new DataManagerRegistry(MOD_ID);

    public static final DataManager<FluidStorageData> FLUID_CONTENTS = REGISTRY.builder(FluidStorageData.DEFAULT).serialize(FluidStorageData.CODEC).networkSerializer(FluidStorageData.NETWORK_CODEC).withDataComponent().copyOnDeath().buildAndRegister("fluids");
    public static final DataManager<ItemStorageData> ITEM_CONTENTS = REGISTRY.builder(ItemStorageData.DEFAULT).serialize(ItemStorageData.CODEC).networkSerializer(ItemStorageData.NETWORK_CODEC).withDataComponent().copyOnDeath().buildAndRegister("items");
    public static final DataManager<Long> VALUE_CONTENT = REGISTRY.builder(() -> 0L).serialize(Codec.LONG).networkSerializer(ByteBufCodecs.VAR_LONG).withDataComponent().copyOnDeath().buildAndRegister("energy");

    public static final Supplier<BlockEntityType<TransferTestBlockEntity>> TRANSFER_BLOCK_ENTITY = registerBlockEntity("transfer_block_entity", TransferTestBlockEntity::new);

    @Expect
    public static <T extends BlockEntity> Supplier<BlockEntityType<T>> registerBlockEntity(String name, BlockEntityType.BlockEntitySupplier<T> supplier);

    public static void init() {
        REGISTRY.init();
    }
}
