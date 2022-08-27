package earth.terrarium.botarium.forge.extensions;


import earth.terrarium.botarium.api.EnergyHoldable;
import earth.terrarium.botarium.api.EnergyStorage;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.Tag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.msrandom.extensions.annotations.ClassExtension;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ClassExtension(EnergyHoldable.class)
public interface EnergyExtensions extends IEnergyStorage, ICapabilityProvider, INBTSerializable<CompoundTag> {

    @Override
    default int receiveEnergy(int maxAmount, boolean bl) {
        return (int) ((EnergyHoldable) this).insertEnergy(maxAmount);
    }

    @Override
    default int extractEnergy(int maxAmount, boolean bl) {
        return (int) ((EnergyHoldable) this).extractEnergy(maxAmount);
    }

    @Override
    default int getEnergyStored() {
        return (int) ((EnergyHoldable) this).getStoredEnergy();
    }

    @Override
    default int getMaxEnergyStored() {
        return (int) ((EnergyHoldable) this).getMaxCapacity();
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
        return ((EnergyStorage) this).serialize(new CompoundTag());
    }

    @Override
    default void deserializeNBT(CompoundTag tag) {
        ((EnergyStorage) this).deseralize(tag);
    }

    @Override
    @NotNull default <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction arg) {
        return capability == ForgeCapabilities.ENERGY ? LazyOptional.of(() -> (IEnergyStorage) this).cast() : LazyOptional.empty();
    }
}
