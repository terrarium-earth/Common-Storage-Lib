package earth.terrarium.botarium.neoforge;

import earth.terrarium.botarium.Botarium;
import earth.terrarium.botarium.common.energy.EnergyApi;
import earth.terrarium.botarium.common.energy.base.BotariumEnergyBlock;
import earth.terrarium.botarium.common.energy.base.BotariumEnergyItem;
import earth.terrarium.botarium.common.fluid.FluidApi;
import earth.terrarium.botarium.common.fluid.base.BotariumFluidBlock;
import earth.terrarium.botarium.common.fluid.base.BotariumFluidItem;
import earth.terrarium.botarium.common.item.ItemApi;
import earth.terrarium.botarium.common.item.ItemContainerBlock;
import earth.terrarium.botarium.common.item.base.BotariumItemBlock;
import earth.terrarium.botarium.common.registry.fluid.FluidBucketItem;
import earth.terrarium.botarium.neoforge.energy.ForgeEnergyContainer;
import earth.terrarium.botarium.neoforge.fluid.ForgeFluidContainer;
import earth.terrarium.botarium.neoforge.fluid.ForgeItemFluidContainer;
import earth.terrarium.botarium.neoforge.generic.NeoForgeCapsHandler;
import earth.terrarium.botarium.neoforge.item.ForgeItemContainer;
import earth.terrarium.botarium.neoforge.item.PlatformItemContainer;
import earth.terrarium.botarium.neoforge.item.ItemContainerWrapper;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.fluids.capability.wrappers.FluidBucketWrapper;

@SuppressWarnings({"unused", "CodeBlock2Expr"})
@Mod(Botarium.MOD_ID)
public class BotariumNeoForge {
    public BotariumNeoForge(IEventBus bus) {
        Botarium.init();

        bus.addListener(BotariumNeoForge::registerEnergy);
        bus.addListener(BotariumNeoForge::registerFluid);
        bus.addListener(BotariumNeoForge::registerItem);
        bus.addListener(NeoForgeCapsHandler::registerCapabilities);
    }

    public static void registerItem(RegisterCapabilitiesEvent event) {
        ItemApi.getBlockEntityRegistry().forEach((blockEntityType, blockFluidGetter1) -> {
            event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, blockEntityType, (blockEntity, direction) -> {
                return new ForgeItemContainer<>(blockFluidGetter1.getItemContainer(blockEntity.getLevel(), blockEntity.getBlockPos(), blockEntity.getBlockState(), blockEntity, direction));
            });
        });

        ItemApi.getBlockRegistry().forEach((block, blockFluidGetter) -> {
            event.registerBlock(Capabilities.ItemHandler.BLOCK, (level, blockPos, blockState, blockEntity, direction) -> {
                return new ForgeItemContainer<>(blockFluidGetter.getItemContainer(level, blockPos, blockState, blockEntity, direction));
            }, block);
        });

        BuiltInRegistries.BLOCK_ENTITY_TYPE.forEach(blockEntityType -> {
            event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, blockEntityType, (blockEntity, object2) -> {
                if (blockEntity instanceof ItemContainerBlock itemContainerBlock) {
                    return new ItemContainerWrapper(itemContainerBlock.getContainer());
                }
                if (blockEntity instanceof BotariumItemBlock<?> itemBlock) {
                    return new ForgeItemContainer<>(itemBlock.getItemContainer(blockEntity.getLevel(), blockEntity.getBlockPos(), blockEntity.getBlockState(), blockEntity, object2));
                }
                return null;
            });
        });

        BuiltInRegistries.BLOCK.stream().filter(block -> block instanceof BotariumItemBlock<?>).forEach(block -> {
            event.registerBlock(Capabilities.ItemHandler.BLOCK, (level, blockPos, blockState, blockEntity, direction) -> {
                if (blockState.getBlock() instanceof BotariumItemBlock<?> itemContainerBlock) {
                    return new ForgeItemContainer<>(itemContainerBlock.getItemContainer(level, blockPos, blockState, blockEntity, direction));
                }
                return null;
            }, block);
        });
    }

    public static void registerEnergy(RegisterCapabilitiesEvent event) {
        EnergyApi.getBlockEntityRegistry().forEach((blockEntityType, blockEnergyGetter1) -> {
            event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, blockEntityType, (blockEntity, direction) -> {
                return new ForgeEnergyContainer<>(blockEnergyGetter1.getEnergyStorage(blockEntity.getLevel(), blockEntity.getBlockPos(), blockEntity.getBlockState(), blockEntity, direction));
            });
        });

        EnergyApi.getBlockRegistry().forEach((block, blockEnergyGetter) -> {
            event.registerBlock(Capabilities.EnergyStorage.BLOCK, (level, blockPos, blockState, blockEntity, direction) -> {
                return new ForgeEnergyContainer<>(blockEnergyGetter.getEnergyStorage(level, blockPos, blockState, blockEntity, direction));
            }, block);
        });

        EnergyApi.getItemRegistry().forEach((item, itemEnergyGetter) -> {
            event.registerItem(Capabilities.EnergyStorage.ITEM, (itemStack, unused) -> {
                var energyContainer = itemEnergyGetter.getEnergyStorage(itemStack);
                if (energyContainer != null) {
                    return new ForgeEnergyContainer<>(energyContainer);
                }
                return null;
            }, item);
        });

        BuiltInRegistries.BLOCK.stream().filter(block -> block instanceof BotariumEnergyBlock<?>).forEach(block -> {
            event.registerBlock(Capabilities.EnergyStorage.BLOCK, (level, blockPos, blockState, blockEntity, direction) -> {
                if (blockState.getBlock() instanceof BotariumEnergyBlock<?> energyBlock) {
                    return new ForgeEnergyContainer<>(energyBlock.getEnergyStorage(level, blockPos, blockState, blockEntity, direction));
                }
                return null;
            }, block);
        });

        BuiltInRegistries.BLOCK_ENTITY_TYPE.forEach(blockEntityType -> {
            event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, blockEntityType, (blockEntity, direction) -> {
                if (blockEntity instanceof BotariumEnergyBlock<?> energyBlock) {
                    return new ForgeEnergyContainer<>(energyBlock.getEnergyStorage(blockEntity.getLevel(), blockEntity.getBlockPos(), blockEntity.getBlockState(), blockEntity, direction));
                }
                return null;
            });
        });

        BuiltInRegistries.ITEM.stream().filter(item -> item instanceof BotariumEnergyItem<?>).forEach(item -> {
            event.registerItem(Capabilities.EnergyStorage.ITEM, (itemStack, unused) -> {
                BotariumEnergyItem<?> energyItem = (BotariumEnergyItem<?>) item;
                return new ForgeEnergyContainer<>(energyItem.getEnergyStorage(itemStack));
            }, item);
        });
    }

    public static void registerFluid(RegisterCapabilitiesEvent event) {
        FluidApi.getBlockEntityRegistry().forEach((blockEntityType, blockFluidGetter1) -> {
            event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, blockEntityType, (blockEntity, direction) -> {
                return new ForgeFluidContainer<>(blockFluidGetter1.getFluidContainer(blockEntity.getLevel(), blockEntity.getBlockPos(), blockEntity.getBlockState(), blockEntity, direction));
            });
        });

        FluidApi.getBlockRegistry().forEach((block, blockFluidGetter) -> {
            event.registerBlock(Capabilities.FluidHandler.BLOCK, (level, blockPos, blockState, blockEntity, direction) -> {
                return new ForgeFluidContainer<>(blockFluidGetter.getFluidContainer(level, blockPos, blockState, blockEntity, direction));
            }, block);
        });

        FluidApi.getItemRegistry().forEach((item, itemFluidGetter) -> {
            event.registerItem(Capabilities.FluidHandler.ITEM, (itemStack, unused) -> {
                var fluidContainer = itemFluidGetter.getFluidContainer(itemStack);
                if (fluidContainer != null) {
                    return new ForgeItemFluidContainer<>(fluidContainer);
                }
                return null;
            }, item);
        });

        BuiltInRegistries.BLOCK_ENTITY_TYPE.forEach(blockEntityType -> {
            event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, blockEntityType, (blockEntity, direction) -> {
                if (blockEntity instanceof BotariumFluidBlock<?> fluidBlock) {
                    return new ForgeFluidContainer<>(fluidBlock.getFluidContainer(blockEntity.getLevel(), blockEntity.getBlockPos(), blockEntity.getBlockState(), blockEntity, direction));
                }
                return null;
            });
        });

        BuiltInRegistries.BLOCK.stream().filter(block -> block instanceof BotariumFluidBlock<?>).forEach(block -> {
            event.registerBlock(Capabilities.FluidHandler.BLOCK, (level, blockPos, blockState, blockEntity, direction) -> {
                if (blockState.getBlock() instanceof BotariumFluidBlock<?> fluidBlock) {
                    return new ForgeFluidContainer<>(fluidBlock.getFluidContainer(level, blockPos, blockState, blockEntity, direction));
                }
                return null;
            }, block);
        });

        BuiltInRegistries.ITEM.stream().filter(item -> item instanceof BotariumFluidItem<?>).forEach(item -> {
            event.registerItem(Capabilities.FluidHandler.ITEM, (itemStack, unused) -> {
                BotariumFluidItem<?> fluidHoldingItem = (BotariumFluidItem<?>) item;
                return new ForgeItemFluidContainer<>(fluidHoldingItem.getFluidContainer(itemStack));
            }, item);
        });

        BuiltInRegistries.ITEM.stream().filter(item -> item instanceof FluidBucketItem).forEach(item -> {
            event.registerItem(Capabilities.FluidHandler.ITEM, (itemStack, unused) -> new FluidBucketWrapper(itemStack), item);
        });
    }
}