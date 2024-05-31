package earth.terrarium.botarium.energy.wrappers;

import net.minecraftforge.energy.IEnergyStorage;

public record CommonEnergyStorage(IEnergyStorage storage) implements AbstractCommonEnergyStorage {
}
