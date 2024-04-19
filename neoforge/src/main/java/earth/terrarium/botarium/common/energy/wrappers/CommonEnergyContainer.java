package earth.terrarium.botarium.common.energy.wrappers;

import net.neoforged.neoforge.energy.IEnergyStorage;

public record CommonEnergyContainer(IEnergyStorage storage) implements AbstractCommonEnergyContainer {
}
