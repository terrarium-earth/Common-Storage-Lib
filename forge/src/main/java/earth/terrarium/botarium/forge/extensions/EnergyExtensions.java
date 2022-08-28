package earth.terrarium.botarium.forge.extensions;


import earth.terrarium.botarium.api.EnergyContainer;
import earth.terrarium.botarium.api.BlockEnergyContainer;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.msrandom.extensions.annotations.ClassExtension;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ClassExtension(EnergyContainer.class)
public interface EnergyExtensions extends IEnergyStorage, ICapabilityProvider, INBTSerializable<CompoundTag> {

    @Override
    default int receiveEnergy(int maxAmount, boolean bl) {
        return (int) ((EnergyContainer) this).insertEnergy(maxAmount);
    }

    @Override
    default int extractEnergy(int maxAmount, boolean bl) {
        return (int) ((EnergyContainer) this).extractEnergy(maxAmount);
    }

    @Override
    default int getEnergyStored() {
        return (int) ((EnergyContainer) this).getStoredEnergy();
    }

    @Override
    default int getMaxEnergyStored() {
        return (int) ((EnergyContainer) this).getMaxCapacity();
    }

    @Override
    default boolean canExtract() {
        return false;
    }

    @Override
    default boolean canReceive() {
        return true;
    }

    @Override
    default CompoundTag serializeNBT() {
        return ((EnergyContainer) this).serialize(new CompoundTag());
    }

    @Override
    default void deserializeNBT(CompoundTag tag) {
        ((EnergyContainer) this).deseralize(tag);
    }

    @Override
    @NotNull default <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction arg) {
        return capability == ForgeCapabilities.ENERGY ? LazyOptional.of(() -> (IEnergyStorage) this).cast() : LazyOptional.empty();
    }
}
