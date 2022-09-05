package earth.terrarium.botarium.forge.extensions;


import earth.terrarium.botarium.api.energy.EnergyContainer;
import earth.terrarium.botarium.api.energy.UpdatingEnergyContainer;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
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
        int inserted = (int) ((EnergyContainer) this).insertEnergy(Mth.clamp(maxAmount, 0, ((EnergyContainer) this).maxInsert()), bl);
        if(!bl && this instanceof UpdatingEnergyContainer container) container.update();
        return inserted;
    }

    @Override
    default int extractEnergy(int maxAmount, boolean bl) {
        int extracted = (int) ((EnergyContainer) this).extractEnergy(Mth.clamp(maxAmount, 0, ((EnergyContainer) this).maxExtract()), bl);
        if(!bl && this instanceof UpdatingEnergyContainer container) container.update();
        return extracted;
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
        return ((EnergyContainer) this).allowsExtraction();
    }

    @Override
    default boolean canReceive() {
        return ((EnergyContainer) this).allowsInsertion();
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
