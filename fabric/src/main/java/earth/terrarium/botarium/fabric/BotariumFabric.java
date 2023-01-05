package earth.terrarium.botarium.fabric;

import earth.terrarium.botarium.Botarium;
import earth.terrarium.botarium.common.menu.base.EnergyAttachment;
import earth.terrarium.botarium.common.energy.base.EnergyItem;
import earth.terrarium.botarium.common.menu.base.EnergyContainer;
import earth.terrarium.botarium.common.fluid.base.FluidContainer;
import earth.terrarium.botarium.common.fluid.base.FluidAttachment;
import earth.terrarium.botarium.common.fluid.base.FluidHoldingItem;
import earth.terrarium.botarium.fluid.base.UpdatingFluidContainer;
import earth.terrarium.botarium.common.item.ItemContainerBlock;
import earth.terrarium.botarium.fabric.energy.FabricBlockEnergyStorage;
import earth.terrarium.botarium.fabric.energy.FabricItemEnergyStorage;
import earth.terrarium.botarium.fabric.fluid.FabricBlockFluidContainer;
import earth.terrarium.botarium.fabric.fluid.FabricItemFluidContainer;
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
            if (blockEntity instanceof EnergyAttachment energyCapable) {
                EnergyContainer container = energyCapable.getEnergyStorage().getContainer(context);
                return container == null ? null : new FabricBlockEnergyStorage(container, blockEntity);
            }
            return null;
        });
        EnergyStorage.ITEM.registerFallback((itemStack, context) -> {
            if(itemStack.getItem() instanceof EnergyItem energyCapable) {
                return new FabricItemEnergyStorage(context, energyCapable.getEnergyStorage(itemStack));
            }
            return null;
        });
        FluidStorage.SIDED.registerFallback((world, pos, state, blockEntity, context) -> {
            if(blockEntity instanceof FluidAttachment fluidHoldingBlock) {
                FluidContainer container = fluidHoldingBlock.getFluidContainer().getContainer(context);
                return container == null ? null : new FabricBlockFluidContainer(container, blockEntity);
            }
            return null;
        });
        FluidStorage.ITEM.registerFallback((itemStack, context) -> {
            if(itemStack.getItem() instanceof FluidHoldingItem fluidHoldingBlock) {
                return new FabricItemFluidContainer(context, fluidHoldingBlock.getFluidContainer(itemStack));
            }
            return null;
        });
        ItemStorage.SIDED.registerFallback((world, pos, state, blockEntity, context) -> {
            if(blockEntity instanceof ItemContainerBlock energyContainer) {
                return InventoryStorageImpl.of(energyContainer.getContainer(), context);
            }
            return null;
        });

    }
}