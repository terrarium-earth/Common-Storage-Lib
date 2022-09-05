package earth.terrarium.botarium.fabric;

import earth.terrarium.botarium.Botarium;
import earth.terrarium.botarium.api.energy.EnergyBlock;
import earth.terrarium.botarium.api.energy.EnergyItem;
import earth.terrarium.botarium.api.fluid.FluidHoldable;
import earth.terrarium.botarium.fabric.energy.FabricBlockEnergyStorage;
import earth.terrarium.botarium.fabric.energy.FabricItemEnergyStorage;
import earth.terrarium.botarium.fabric.fluid.FabricBlockFluidContainer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import team.reborn.energy.api.EnergyStorage;

@SuppressWarnings("UnstableApiUsage")
public class BotariumFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        Botarium.init();
        EnergyStorage.SIDED.registerFallback((world, pos, state, blockEntity, context) -> {
            if (blockEntity instanceof EnergyBlock energyCapable) {
                return new FabricBlockEnergyStorage(energyCapable.getEnergyStorage());
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
            if(blockEntity instanceof FluidHoldable fluidHoldable) {
                return new FabricBlockFluidContainer(fluidHoldable.getFluidContainer());
            }
            return null;
        });
    }
}