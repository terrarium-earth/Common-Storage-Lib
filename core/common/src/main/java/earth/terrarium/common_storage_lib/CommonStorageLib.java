package earth.terrarium.common_storage_lib;

import com.mojang.serialization.Codec;
import earth.terrarium.common_storage_lib.data.DataManager;
import earth.terrarium.common_storage_lib.data.DataManagerRegistry;
import earth.terrarium.common_storage_lib.energy.EnergyApi;
import earth.terrarium.common_storage_lib.energy.EnergyProvider;
import earth.terrarium.common_storage_lib.fluid.FluidApi;
import earth.terrarium.common_storage_lib.fluid.util.FluidProvider;
import earth.terrarium.common_storage_lib.heat.HeatApi;
import earth.terrarium.common_storage_lib.heat.HeatProvider;
import earth.terrarium.common_storage_lib.fluid.util.FluidStorageData;
import earth.terrarium.common_storage_lib.item.input.ItemConsumerRegistry;
import earth.terrarium.common_storage_lib.item.util.ItemStorageData;
import earth.terrarium.common_storage_lib.item.ItemApi;
import earth.terrarium.common_storage_lib.item.util.ItemProvider;
import earth.terrarium.common_storage_lib.resources.fluid.FluidResource;
import earth.terrarium.common_storage_lib.resources.item.ItemResource;
import earth.terrarium.common_storage_lib.resources.ResourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.world.level.block.Blocks;

public class CommonStorageLib {
    public static final String MOD_ID = "common_storage_lib";

    public static void init() {
        ItemConsumerRegistry.init();
        //Energy

        EnergyApi.ITEM.registerFallback((stack, context) -> {
            if (stack.getItem() instanceof EnergyProvider.Item provider) {
                return provider.getEnergy(stack, context);
            } else {
                return null;
            }
        }, item -> item instanceof EnergyProvider.Item);

        EnergyApi.BLOCK.registerFallback((level, pos, state, entity, direction) -> {
            if (state.getBlock() instanceof EnergyProvider.Block provider) {
                return provider.getEnergy(level, pos, state, entity, direction);
            } else {
                return null;
            }
        }, block -> block instanceof EnergyProvider.Block);

        EnergyApi.BLOCK.registerFallback((entity, direction) -> {
            if (entity instanceof EnergyProvider.BlockEntity provider) {
                return provider.getEnergy(direction);
            } else {
                return null;
            }
        }, entityType -> entityType.create(BlockPos.ZERO, entityType.validBlocks.stream().findFirst().orElse(Blocks.AIR).defaultBlockState()) instanceof EnergyProvider.BlockEntity);

        EnergyApi.ENTITY.registerFallback((entity, direction) -> {
            if (entity instanceof EnergyProvider.Entity provider) {
                return provider.getEnergy(direction);
            } else {
                return null;
            }
        });

        //Fluid

        FluidApi.ITEM.registerFallback((stack, context) -> {
            if (stack.getItem() instanceof FluidProvider.Item provider) {
                return provider.getFluids(stack, context);
            } else {
                return null;
            }
        }, item -> item instanceof FluidProvider.Item);

        FluidApi.BLOCK.registerFallback((level, pos, state, entity, direction) -> {
            if (state.getBlock() instanceof FluidProvider.Block provider) {
                return provider.getFluids(level, pos, state, entity, direction);
            } else {
                return null;
            }
        }, block -> block instanceof FluidProvider.Block);

        FluidApi.BLOCK.registerFallback((entity, direction) -> {
            if (entity instanceof FluidProvider.BlockEntity provider) {
                return provider.getFluids(direction);
            } else {
                return null;
            }
        }, entityType -> entityType.create(BlockPos.ZERO, entityType.validBlocks.stream().findFirst().orElse(Blocks.AIR).defaultBlockState()) instanceof FluidProvider.BlockEntity);

        FluidApi.ENTITY.registerFallback((entity, direction) -> {
            if (entity instanceof FluidProvider.Entity provider) {
                return provider.getFluids(direction);
            } else {
                return null;
            }
        });

        //Item

        ItemApi.ITEM.registerFallback((stack, context) -> {
            if (stack.getItem() instanceof ItemProvider.Item provider) {
                return provider.getItems(stack, context);
            } else {
                return null;
            }
        }, item -> item instanceof ItemProvider.Item);

        ItemApi.BLOCK.registerFallback((level, pos, state, entity, direction) -> {
            if (state.getBlock() instanceof ItemProvider.Block provider) {
                return provider.getItems(level, pos, state, entity, direction);
            } else {
                return null;
            }
        }, block -> block instanceof ItemProvider.Block);

        ItemApi.BLOCK.registerFallback((entity, direction) -> {
            if (entity instanceof ItemProvider.BlockEntity provider) {
                return provider.getItems(direction);
            } else {
                return null;
            }
        }, entityType -> entityType.create(BlockPos.ZERO, entityType.validBlocks.stream().findFirst().orElse(Blocks.AIR).defaultBlockState()) instanceof ItemProvider.BlockEntity);

        ItemApi.ENTITY.registerFallback((entity, ignored) -> {
            if (entity instanceof ItemProvider.Entity provider) {
                return provider.getItems();
            } else {
                return null;
            }
        });

        ItemApi.ENTITY_AUTOMATION.registerFallback((entity, direction) -> {
            if (entity instanceof ItemProvider.AutomationEntity provider) {
                return provider.getItems(direction);
            } else {
                return null;
            }
        });

        // Heat

        HeatApi.BLOCK.registerFallback((level, pos, state, entity, direction) -> {
            if (state.getBlock() instanceof HeatProvider.Block provider) {
                return provider.getHeat(level, pos, state, entity, direction);
            } else {
                return null;
            }
        }, block -> block instanceof HeatProvider.Block);

        HeatApi.BLOCK.registerFallback((entity, direction) -> {
            if (entity instanceof HeatProvider.BlockEntity provider) {
                return provider.getHeat(direction);
            } else {
                return null;
            }
        }, entityType -> entityType.create(BlockPos.ZERO, entityType.validBlocks.stream().findFirst().orElse(Blocks.AIR).defaultBlockState()) instanceof HeatProvider.BlockEntity);

        HeatApi.ENTITY.registerFallback((entity, ignored) -> {
            if (entity instanceof HeatProvider.Entity provider) {
                return provider.getHeat();
            } else {
                return null;
            }
        });

        HeatApi.ITEM.registerFallback((stack, context) -> {
            if (stack.getItem() instanceof HeatProvider.Item provider) {
                return provider.getHeat(stack, context);
            } else {
                return null;
            }
        }, item -> item instanceof HeatProvider.Item);
    }
}
