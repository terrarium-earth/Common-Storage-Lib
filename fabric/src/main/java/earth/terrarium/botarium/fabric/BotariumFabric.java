package earth.terrarium.botarium.fabric;

import earth.terrarium.botarium.Botarium;
import earth.terrarium.botarium.api.EnergyBlock;
import earth.terrarium.botarium.api.EnergyContainer;
import earth.terrarium.botarium.api.EnergyItem;
import net.fabricmc.api.ModInitializer;
import net.minecraft.world.item.ItemStack;
import team.reborn.energy.api.EnergyStorage;
import team.reborn.energy.api.base.SimpleBatteryItem;

@SuppressWarnings("UnstableApiUsage")
public class BotariumFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        Botarium.init();
        EnergyStorage.SIDED.registerFallback((world, pos, state, blockEntity, context) -> {
            if (blockEntity instanceof EnergyBlock energyCapable) {
                return (EnergyStorage) energyCapable.getEnergyStorage();
            }
            return null;
        });
        EnergyStorage.ITEM.registerFallback((itemStack, context) -> {
            if(itemStack.getItem() instanceof EnergyItem energyCapable) {
                EnergyContainer energyStorage = energyCapable.getEnergyStorage(itemStack);
                return new FabricEnergyStorage(context, energyStorage);
            }
            return null;
        });
    }
}