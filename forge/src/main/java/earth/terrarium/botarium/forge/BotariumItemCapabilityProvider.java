package earth.terrarium.botarium.forge;

import earth.terrarium.botarium.common.menu.base.EnergyAttachment;
import earth.terrarium.botarium.common.fluid.base.FluidAttachment;
import earth.terrarium.botarium.common.fluid.base.ItemFluidContainer;
import earth.terrarium.botarium.forge.energy.ForgeItemEnergyContainer;
import earth.terrarium.botarium.forge.fluid.ForgeItemFluidContainer;
import net.minecraft.core.Direction;
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
        fluidHandler = getFluidContainer(item);
        energyStorage = getEnergyContainer(item);
    }

    @SuppressWarnings("unchecked")
    private static LazyOptional<ForgeItemFluidContainer> getFluidContainer(ItemStack item) {
        if(item.getItem() instanceof FluidAttachment<?, ?> fluidHoldingItem) {
            if(fluidHoldingItem.getHolderType() == ItemStack.class) {
                FluidAttachment<ItemStack, ?> fluidAttachment = (FluidAttachment<ItemStack, ?>) fluidHoldingItem;
                if(fluidAttachment.getFluidContainer() instanceof ItemFluidContainer container) {
                    return LazyOptional.of(() -> new ForgeItemFluidContainer(container, item));
                }
            }
        }
        return LazyOptional.empty();
    }

    @SuppressWarnings("unchecked")
    private static LazyOptional<ForgeItemEnergyContainer> getEnergyContainer(ItemStack item) {
        if(item.getItem() instanceof EnergyAttachment<?, ?> energyItem) {
            if(energyItem.getHolderType() == ItemStack.class) {
                EnergyAttachment<ItemStack, ?> energyAttachment = (EnergyAttachment<ItemStack, ?>) energyItem;
                return LazyOptional.of(() -> new ForgeItemEnergyContainer(energyAttachment.getEnergyStorage(), item));
            }
        }
        return LazyOptional.empty();
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
