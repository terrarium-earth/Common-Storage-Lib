package earth.terrarium.botarium.fabric;

import earth.terrarium.botarium.Botarium;
import earth.terrarium.botarium.common.energy.EnergyApi;
import earth.terrarium.botarium.common.energy.base.BotariumEnergyBlock;
import earth.terrarium.botarium.common.energy.base.BotariumEnergyItem;
import earth.terrarium.botarium.common.energy.base.EnergyContainer;
import earth.terrarium.botarium.common.fluid.FluidApi;
import earth.terrarium.botarium.common.fluid.base.BotariumFluidBlock;
import earth.terrarium.botarium.common.fluid.base.BotariumFluidItem;
import earth.terrarium.botarium.common.fluid.base.FluidContainer;
import earth.terrarium.botarium.common.item.ItemContainerBlock;
import earth.terrarium.botarium.fabric.energy.FabricBlockEnergyContainer;
import earth.terrarium.botarium.fabric.energy.FabricItemEnergyContainer;
import earth.terrarium.botarium.fabric.fluid.storage.FabricBlockFluidContainer;
import earth.terrarium.botarium.fabric.fluid.storage.FabricItemFluidContainer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.fabricmc.fabric.impl.transfer.item.InventoryStorageImpl;
import team.reborn.energy.api.EnergyStorage;

@SuppressWarnings("UnstableApiUsage")
public class BotariumFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        Botarium.init();
        EnergyStorage.SIDED.registerFallback((world, pos, state, blockEntity, context) -> {
            if (blockEntity instanceof BotariumEnergyBlock<?> attachment) {
                EnergyContainer container = attachment.getEnergyStorage().getContainer(context);
                return container == null ? null : new FabricBlockEnergyContainer(container, attachment.getEnergyStorage(), blockEntity);
            } else if (blockEntity != null) {
                EnergyApi.BlockEnergyGetter<?> blockEnergyGetter = EnergyApi.BLOCK_LOOKUP_MAP.get(state.getBlock());
                if (blockEnergyGetter == null) return null;
                var container = blockEnergyGetter.getEnergyContainer(world, pos, state, blockEntity, context);
                return container == null ? null : new FabricBlockEnergyContainer(container, container, blockEntity);
            }
            return null;
        });

        EnergyApi.BLOCK_ENTITY_LOOKUP_MAP.forEach((block, getter) -> {
            EnergyStorage.SIDED.registerForBlockEntity((entity, direction) -> {
                var energyContainer = getter.getEnergyContainer(entity.getLevel(), entity.getBlockPos(), entity.getBlockState(), entity, direction);
                return new FabricBlockEnergyContainer(energyContainer, energyContainer, entity);
            }, block);
        });

        EnergyStorage.ITEM.registerFallback((itemStack, context) -> {
            if (itemStack.getItem() instanceof BotariumEnergyItem<?> attachment) {
                return new FabricItemEnergyContainer(context, attachment.getEnergyStorage(itemStack));
            } else {
                EnergyApi.ItemEnergyGetter<?> itemEnergyGetter = EnergyApi.ITEM_LOOKUP_MAP.get(itemStack.getItem());
                var container = itemEnergyGetter.getEnergyContainer(itemStack);
                return container == null ? null : new FabricItemEnergyContainer(context, container);
            }
        });

        FluidStorage.SIDED.registerFallback((world, pos, state, blockEntity, context) -> {
            if (blockEntity instanceof BotariumFluidBlock<?> attachment) {
                FluidContainer container = attachment.getFluidContainer().getContainer(context);
                return container == null ? null : new FabricBlockFluidContainer(container, attachment.getFluidContainer(), blockEntity);
            } else if (blockEntity != null) {
                var fluidContainerGetter = FluidApi.BLOCK_LOOKUP_MAP.get(state.getBlock());
                if (fluidContainerGetter == null) return null;
                var container = fluidContainerGetter.getFluidContainer(world, pos, state, blockEntity, context);
                return container == null ? null : new FabricBlockFluidContainer(container, container, blockEntity);
            }
            return null;
        });

        FluidApi.BLOCK_ENTITY_LOOKUP_MAP.forEach((block, getter) -> {
            FluidStorage.SIDED.registerForBlockEntity((entity, direction) -> {
                var energyContainer = getter.getFluidContainer(entity.getLevel(), entity.getBlockPos(), entity.getBlockState(), entity, direction);
                return new FabricBlockFluidContainer(energyContainer, energyContainer, entity);
            }, block);
        });

        FluidStorage.ITEM.registerFallback((itemStack, context) -> {
            if (itemStack.getItem() instanceof BotariumFluidItem<?> attachment) {
                return new FabricItemFluidContainer(context, attachment.getFluidContainer(itemStack));
            }
            return null;
        });

        ItemStorage.SIDED.registerFallback((world, pos, state, blockEntity, context) -> {
            if (blockEntity instanceof ItemContainerBlock energyContainer) {
                return InventoryStorageImpl.of(energyContainer.getContainer(), context);
            }
            return null;
        });

    }
}