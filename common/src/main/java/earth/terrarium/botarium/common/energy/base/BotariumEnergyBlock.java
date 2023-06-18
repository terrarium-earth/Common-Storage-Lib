package earth.terrarium.botarium.common.energy.base;

import earth.terrarium.botarium.util.Updatable;
import net.minecraft.world.level.block.entity.BlockEntity;

public interface BotariumEnergyBlock<T extends EnergyContainer & Updatable<BlockEntity>> {
    T getEnergyStorage();
}
