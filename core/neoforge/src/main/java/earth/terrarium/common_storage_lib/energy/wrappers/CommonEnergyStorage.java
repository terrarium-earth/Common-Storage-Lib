package earth.terrarium.common_storage_lib.energy.wrappers;

import net.minecraftforge.energy.IEnergyStorage;

public record CommonEnergyStorage(IEnergyStorage storage) implements AbstractCommonEnergyStorage {
}
