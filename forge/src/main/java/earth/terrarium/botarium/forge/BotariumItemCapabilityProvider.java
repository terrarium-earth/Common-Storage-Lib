package earth.terrarium.botarium.forge;

import earth.terrarium.botarium.api.energy.EnergyContainer;
import earth.terrarium.botarium.api.energy.EnergyItem;
import earth.terrarium.botarium.api.fluid.FluidHoldingItem;
import earth.terrarium.botarium.forge.fluid.ForgeItemFluidContainer;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BotariumItemCapabilityProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    private final LazyOptional<ForgeItemFluidContainer> fluidHandler;
    private final LazyOptional<EnergyContainer> energyStorage;

    public BotariumItemCapabilityProvider(ItemStack item, @Nullable CompoundTag nbt) {
        if(item.getItem() instanceof FluidHoldingItem fluidHoldingItem) {
            fluidHandler = LazyOptional.of(() -> new ForgeItemFluidContainer(fluidHoldingItem.getFluidContainer(item)));
        } else {
            fluidHandler = LazyOptional.empty();
        }
        if(item.getItem() instanceof EnergyItem energyItem) {
            energyStorage = LazyOptional.of(() -> energyItem.getEnergyStorage(item));
        } else {
            energyStorage = LazyOptional.empty();
        }
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction arg) {
        if (capability == ForgeCapabilities.ENERGY) {
            return energyStorage.cast();
        } else if (capability == ForgeCapabilities.FLUID_HANDLER_ITEM) {
            return fluidHandler.cast();
        }
        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag compoundTag = new CompoundTag();
        fluidHandler.ifPresent(object -> {
            CompoundTag fluids = object.container().serialize(new CompoundTag());
            compoundTag.put("Fluids", fluids);
        });
        energyStorage.ifPresent(object -> {
            CompoundTag energy = object.serialize(new CompoundTag());
            compoundTag.put("Energy", energy);
        });
        return compoundTag;
    }

    @Override
    public void deserializeNBT(CompoundTag arg) {
        fluidHandler.ifPresent(object -> {
            CompoundTag fluids = arg.getCompound("Fluids");
            object.container().deserialize(fluids);
        });
        energyStorage.ifPresent(object -> {
            CompoundTag energy = arg.getCompound("Energy");
            object.deserialize(energy);
        });
    }
}
