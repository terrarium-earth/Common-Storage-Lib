package earth.terrarium.botarium.fabric;

import earth.terrarium.botarium.Botarium;
import earth.terrarium.botarium.common.energy.base.BotariumEnergyBlock;
import earth.terrarium.botarium.common.energy.base.BotariumEnergyItem;
import earth.terrarium.botarium.common.energy.base.EnergyContainer;
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
            }
            return null;
        });
        EnergyStorage.ITEM.registerFallback((itemStack, context) -> {
            if (itemStack.getItem() instanceof BotariumEnergyItem<?> attachment) {
                return new FabricItemEnergyContainer(context, attachment.getEnergyStorage(itemStack));
            }
            return null;
        });
        FluidStorage.SIDED.registerFallback((world, pos, state, blockEntity, context) -> {
            if (blockEntity instanceof BotariumFluidBlock<?> attachment) {
                FluidContainer container = attachment.getFluidContainer().getContainer(context);
                return container == null ? null : new FabricBlockFluidContainer(container, attachment.getFluidContainer(), blockEntity);
            }
            return null;
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