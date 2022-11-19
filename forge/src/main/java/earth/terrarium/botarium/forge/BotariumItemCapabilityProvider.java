package earth.terrarium.botarium.forge;

import earth.terrarium.botarium.api.energy.EnergyContainer;
import earth.terrarium.botarium.api.energy.EnergyItem;
import earth.terrarium.botarium.api.fluid.FluidHoldingItem;
import earth.terrarium.botarium.forge.energy.ForgeItemEnergyContainer;
import earth.terrarium.botarium.forge.fluid.ForgeItemFluidContainer;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BotariumItemCapabilityProvider implements ICapabilityProvider {
    private final LazyOptional<ForgeItemFluidContainer> fluidHandler;
    private final LazyOptional<ForgeItemEnergyContainer> energyStorage;

    public BotariumItemCapabilityProvider(ItemStack item) {
        if(item.getItem() instanceof FluidHoldingItem fluidHoldingItem) {
            fluidHandler = LazyOptional.of(() -> new ForgeItemFluidContainer(fluidHoldingItem.getFluidContainer(item), item));
        } else {
            fluidHandler = LazyOptional.empty();
        }
        if(item.getItem() instanceof EnergyItem energyItem) {
            energyStorage = LazyOptional.of(() -> new ForgeItemEnergyContainer(energyItem.getEnergyStorage(item), item));
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
}
