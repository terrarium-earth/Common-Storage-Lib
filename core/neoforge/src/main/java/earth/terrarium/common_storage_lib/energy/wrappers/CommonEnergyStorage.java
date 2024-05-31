package earth.terrarium.common_storage_lib.energy.wrappers;

import net.neoforged.neoforge.energy.IEnergyStorage;

public record CommonEnergyStorage(IEnergyStorage storage) implements AbstractCommonEnergyStorage {
}
