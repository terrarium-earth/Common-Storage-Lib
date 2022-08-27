package earth.terrarium.botarium.fabric;

import earth.terrarium.botarium.BlockEntityImpl;
import earth.terrarium.botarium.Botarium;
import earth.terrarium.botarium.api.EnergyMarker;
import net.fabricmc.api.ModInitializer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import team.reborn.energy.api.EnergyStorage;

@SuppressWarnings("UnstableApiUsage")
public class BotariumFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        Botarium.init();
        EnergyStorage.SIDED.registerFallback((world, pos, state, blockEntity, context) -> {
            if (blockEntity instanceof EnergyMarker<?> energyMarker) {
                return (EnergyStorage) ((EnergyMarker<BlockEntity>) energyMarker).getEnergyStorage(blockEntity);
            }
            return null;
        });
        EnergyStorage.ITEM.registerFallback((itemStack, context) -> {
            if(itemStack.getItem() instanceof EnergyMarker<?> energyMarker) {
                return (EnergyStorage) ((EnergyMarker<ItemStack>) energyMarker).getEnergyStorage(itemStack);
            }
            return null;
        });
    }
}