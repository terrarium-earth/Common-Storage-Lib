package earth.terrarium.botarium.forge.fluid;

import earth.terrarium.botarium.Botarium;
import earth.terrarium.botarium.api.Serializable;
import earth.terrarium.botarium.api.fluid.FluidHolder;
import earth.terrarium.botarium.api.fluid.ItemFluidContainer;
import earth.terrarium.botarium.forge.AutoSerializable;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record ForgeItemFluidContainer(ItemFluidContainer container) implements IFluidHandlerItem, ICapabilityProvider, AutoSerializable {
    public static final ResourceLocation FLUID_KEY = new ResourceLocation(Botarium.MOD_ID, "fluid_item");

    @Override
    public @NotNull ItemStack getContainer() {
        return container.getContainerItem();
    }

    @NotNull
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction var2) {
        return capability == ForgeCapabilities.FLUID_HANDLER_ITEM ? LazyOptional.of(() -> this).cast() : LazyOptional.empty();
    }

    @Override
    public int getTanks() {
        return container.getSize();
    }

    @Override
    public @NotNull FluidStack getFluidInTank(int i) {
        return new ForgeFluidHolder(container.getFluids().get(i));
    }

    @Override
    public int getTankCapacity(int i) {
        return (int) container.getTankCapacity(i);
    }

    @Override
    public boolean isFluidValid(int i, @NotNull FluidStack fluidStack) {
        return getFluidInTank(i).isFluidEqual(fluidStack);
    }

    @Override
    public int fill(FluidStack fluidStack, FluidAction fluidAction) {
        return (int) this.container.insertFluid(new ForgeFluidHolder(fluidStack), fluidAction.simulate());
    }

    @Override
    public @NotNull FluidStack drain(FluidStack fluidStack, FluidAction fluidAction) {
        return new ForgeFluidHolder(this.container.extractFluid(new ForgeFluidHolder(fluidStack), fluidAction.simulate()));
    }

    @Override
    public @NotNull FluidStack drain(int i, FluidAction fluidAction) {
        FluidHolder fluid = this.container.getFluids().get(0).copyHolder();
        if (fluid.isEmpty()) return FluidStack.EMPTY;
        fluid.setAmount(i);
        return new ForgeFluidHolder(this.container.extractFluid(fluid, fluidAction.simulate()));
    }

    @Override
    public Serializable getSerializable() {
        return container;
    }
}
