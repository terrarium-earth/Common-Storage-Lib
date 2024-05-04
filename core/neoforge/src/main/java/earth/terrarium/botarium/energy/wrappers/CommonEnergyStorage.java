package earth.terrarium.botarium.energy.wrappers;

import net.neoforged.neoforge.energy.IEnergyStorage;

public record CommonEnergyStorage(IEnergyStorage storage) implements AbstractCommonEnergyStorage {
}
