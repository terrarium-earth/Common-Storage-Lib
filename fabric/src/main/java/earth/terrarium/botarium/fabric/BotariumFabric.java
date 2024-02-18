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
            EnergyApi.finalizeBlockRegistration();
            if (blockEntity instanceof BotariumEnergyBlock<?> attachment) {
                EnergyContainer container = attachment.getEnergyStorage().getContainer(context);
                return container == null ? null : new FabricBlockEnergyContainer(container, attachment.getEnergyStorage(), blockEntity);
            } else {
                EnergyApi.BlockEnergyGetter<?> blockEnergyGetter = EnergyApi.FINALIZED_BLOCK_LOOKUP_MAP.get(state.getBlock());
                if (blockEnergyGetter != null) {
                   var container = blockEnergyGetter.getEnergyContainer(world, pos, state, blockEntity, context);
                   if (container != null) {
                       return new FabricBlockEnergyContainer(container, container, blockEntity);
                   }
                }
                if(blockEntity != null) {
                    EnergyApi.BlockEnergyGetter<?> entityEnergyGetter = EnergyApi.FINALIZED_BLOCK_ENTITY_LOOKUP_MAP.get(blockEntity.getType());
                    if (entityEnergyGetter == null) return null;
                    var entityContainer = entityEnergyGetter.getEnergyContainer(world, pos, state, blockEntity, context);
                    return entityContainer == null ? null : new FabricBlockEnergyContainer(entityContainer, entityContainer, blockEntity);
                }
            }
            return null;
        });

        EnergyStorage.ITEM.registerFallback((itemStack, context) -> {
            EnergyApi.finalizeItemRegistration();
            if (itemStack.getItem() instanceof BotariumEnergyItem<?> attachment) {
                EnergyContainer energyStorage = attachment.getEnergyStorage(itemStack);
                return energyStorage == null ? null : new FabricItemEnergyContainer(context, energyStorage);
            } else {
                EnergyApi.ItemEnergyGetter<?> itemEnergyGetter = EnergyApi.FINALIZED_ITEM_LOOKUP_MAP.get(itemStack.getItem());
                if (itemEnergyGetter == null) return null;
                var container = itemEnergyGetter.getEnergyContainer(itemStack);
                return container == null ? null : new FabricItemEnergyContainer(context, container);
            }
        });

        FluidStorage.SIDED.registerFallback((world, pos, state, blockEntity, context) -> {
            FluidApi.finalizeBlockRegistration();
            if (blockEntity instanceof BotariumFluidBlock<?> attachment) {
                var container = attachment.getFluidContainer();
                return container == null ? null : new FabricBlockFluidContainer<>(container, blockEntity);
            } else {
                FluidApi.BlockFluidGetter<?> blockEnergyGetter = FluidApi.FINALIZED_BLOCK_LOOKUP_MAP.get(state.getBlock());
                if (blockEnergyGetter != null) {
                    var container = blockEnergyGetter.getFluidContainer(world, pos, state, blockEntity, context);
                    if (container != null) {
                        return new FabricBlockFluidContainer<>(container, blockEntity);
                    }
                }
                if (blockEntity != null) {
                    FluidApi.BlockFluidGetter<?> entityEnergyGetter = FluidApi.FINALIZED_BLOCK_ENTITY_LOOKUP_MAP.get(blockEntity.getType());
                    if (entityEnergyGetter == null) return null;
                    var entityContainer = entityEnergyGetter.getFluidContainer(world, pos, state, blockEntity, context);
                    return entityContainer == null ? null : new FabricBlockFluidContainer<>(entityContainer, blockEntity);
                }
            }
            return null;
        });

        FluidStorage.ITEM.registerFallback((itemStack, context) -> {
            FluidApi.finalizeItemRegistration();
            if (itemStack.getItem() instanceof BotariumFluidItem<?> attachment) {
                var fluidContainer = attachment.getFluidContainer(itemStack);
                return fluidContainer == null ? null : new FabricItemFluidContainer<>(context, fluidContainer);
            } else {
                FluidApi.ItemFluidGetter<?> itemFluidGetter = FluidApi.FINALIZED_ITEM_LOOKUP_MAP.get(itemStack.getItem());
                if (itemFluidGetter == null) return null;
                var container = itemFluidGetter.getFluidContainer(itemStack);
                return container == null ? null : new FabricItemFluidContainer<>(context, container);
            }
        });

        ItemStorage.SIDED.registerFallback((world, pos, state, blockEntity, context) -> {
            if (blockEntity instanceof ItemContainerBlock energyContainer) {
                return InventoryStorageImpl.of(energyContainer.getContainer(), context);
            }
            return null;
        });

    }
}