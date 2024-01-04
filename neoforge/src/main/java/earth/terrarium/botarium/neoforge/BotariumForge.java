package earth.terrarium.botarium.neoforge;

import earth.terrarium.botarium.Botarium;
import earth.terrarium.botarium.common.energy.EnergyApi;
import earth.terrarium.botarium.common.energy.base.BotariumEnergyBlock;
import earth.terrarium.botarium.common.energy.base.BotariumEnergyItem;
import earth.terrarium.botarium.common.fluid.FluidApi;
import earth.terrarium.botarium.common.fluid.base.BotariumFluidBlock;
import earth.terrarium.botarium.common.fluid.base.BotariumFluidItem;
import earth.terrarium.botarium.common.item.ItemContainerBlock;
import earth.terrarium.botarium.neoforge.energy.ForgeBlockEnergyContainer;
import earth.terrarium.botarium.neoforge.energy.ForgeEnergyContainer;
import earth.terrarium.botarium.neoforge.energy.ForgeItemEnergyContainer;
import earth.terrarium.botarium.neoforge.fluid.ForgeFluidBlockContainer;
import earth.terrarium.botarium.neoforge.fluid.ForgeFluidContainer;
import earth.terrarium.botarium.neoforge.fluid.ForgeItemFluidContainer;
import earth.terrarium.botarium.neoforge.item.ItemContainerWrapper;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.common.NeoForge;

@SuppressWarnings("unchecked")
@Mod(Botarium.MOD_ID)
public class BotariumForge {

    public BotariumForge() {
        Botarium.init();
        IEventBus bus = NeoForge.EVENT_BUS;
        bus.addListener(BotariumForge::attachCapabilities);
    }

    public static void attachCapabilities(RegisterCapabilitiesEvent event) {
        EnergyApi.finalizeBlockRegistration();
        FluidApi.finalizeBlockRegistration();

        BuiltInRegistries.BLOCK_ENTITY_TYPE.iterator().forEachRemaining(blockEntityType -> event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, blockEntityType, (blockEntity, object2) -> {
            if (blockEntity instanceof BotariumEnergyBlock<?> energyBlock) {
                return new ForgeEnergyContainer<>(energyBlock.getEnergyStorage(), blockEntity);
            }
            return null;
        }));

        EnergyApi.FINALIZED_BLOCK_ENTITY_LOOKUP_MAP.forEach((blockEntityType, blockEnergyGetter1) -> event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, blockEntityType, (blockEntity, direction) -> new ForgeEnergyContainer<>(blockEnergyGetter1.getEnergyContainer(blockEntity.getLevel(), blockEntity.getBlockPos(), blockEntity.getBlockState(), blockEntity, direction), blockEntity)));

        EnergyApi.FINALIZED_BLOCK_LOOKUP_MAP.forEach((block, blockEnergyGetter) -> event.registerBlock(Capabilities.EnergyStorage.BLOCK, (level, blockPos, blockState, blockEntity, direction) -> new ForgeBlockEnergyContainer<>(blockEnergyGetter.getEnergyContainer(level, blockPos, blockState, blockEntity, direction)), block));

        BuiltInRegistries.BLOCK_ENTITY_TYPE.iterator().forEachRemaining(blockEntityType -> event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, blockEntityType, (blockEntity, object2) -> {
            if (blockEntity instanceof BotariumFluidBlock<?> fluidBlock) {
                return new ForgeFluidContainer<>(fluidBlock.getFluidContainer(), blockEntity);
            }
            return null;
        }));

        FluidApi.FINALIZED_BLOCK_ENTITY_LOOKUP_MAP.forEach((blockEntityType, blockFluidGetter1) -> event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, blockEntityType, (blockEntity, direction) -> new ForgeFluidContainer<>(blockFluidGetter1.getFluidContainer(blockEntity.getLevel(), blockEntity.getBlockPos(), blockEntity.getBlockState(), blockEntity, direction), blockEntity)));

        FluidApi.FINALIZED_BLOCK_LOOKUP_MAP.forEach((block, blockFluidGetter) -> event.registerBlock(Capabilities.FluidHandler.BLOCK, (level, blockPos, blockState, blockEntity, direction) -> new ForgeFluidBlockContainer<>(blockFluidGetter.getFluidContainer(level, blockPos, blockState, blockEntity, direction)), block));

        BuiltInRegistries.BLOCK_ENTITY_TYPE.iterator().forEachRemaining(blockEntityType -> event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, blockEntityType, (blockEntity, object2) -> {
            if (blockEntity instanceof ItemContainerBlock itemContainerBlock) {
                return new ItemContainerWrapper(itemContainerBlock.getContainer());
            }
            return null;
        }));

        BuiltInRegistries.ITEM.stream().filter(item -> item instanceof BotariumEnergyItem<?>).forEach(item -> event.registerItem(Capabilities.EnergyStorage.ITEM, (itemStack, unused) -> {
            BotariumEnergyItem<?> energyItem = (BotariumEnergyItem<?>) item;
            return new ForgeItemEnergyContainer<>(energyItem.getEnergyStorage(itemStack), itemStack);
        }, item));

        EnergyApi.FINALIZED_ITEM_LOOKUP_MAP.forEach((item, itemEnergyGetter) -> event.registerItem(Capabilities.EnergyStorage.ITEM, (itemStack, unused) -> {
            var energyContainer = itemEnergyGetter.getEnergyContainer(itemStack);
            if (energyContainer != null) {
                return new ForgeItemEnergyContainer<>(energyContainer, itemStack);
            }
            return null;
        }, item));

        BuiltInRegistries.ITEM.stream().filter(item -> item instanceof BotariumFluidItem<?>).forEach(item -> event.registerItem(Capabilities.FluidHandler.ITEM, (itemStack, unused) -> {
            BotariumFluidItem<?> fluidHoldingItem = (BotariumFluidItem<?>) item;
            return new ForgeItemFluidContainer<>(fluidHoldingItem.getFluidContainer(itemStack), itemStack);
        }, item));

        FluidApi.FINALIZED_ITEM_LOOKUP_MAP.forEach((item, itemFluidGetter) -> event.registerItem(Capabilities.FluidHandler.ITEM, (itemStack, unused) -> {
            var fluidContainer = itemFluidGetter.getFluidContainer(itemStack);
            if (fluidContainer != null) {
                return new ForgeItemFluidContainer<>(fluidContainer, itemStack);
            }
            return null;
        }, item));
    }
}