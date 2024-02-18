package earth.terrarium.botarium.forge.fluid;

import earth.terrarium.botarium.common.fluid.base.FluidContainer;
import earth.terrarium.botarium.common.fluid.base.FluidHolder;
import earth.terrarium.botarium.forge.AutoSerializable;
import earth.terrarium.botarium.util.Serializable;
import earth.terrarium.botarium.util.Updatable;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public record ForgeFluidContainer<T extends FluidContainer & Updatable<BlockEntity>>(
        T container, BlockEntity entity) implements IFluidHandler, ICapabilityProvider, AutoSerializable {

    @Override
    public int getTanks() {
        return container.getSize();
    }

    @Override
    public @NotNull FluidStack getFluidInTank(int i) {
        return new ForgeFluidHolder(container.getFluids().get(i)).fluidStack;
    }

    @Override
    public int getTankCapacity(int i) {
        return (int) this.container.getTankCapacity(i);
    }

    @Override
    public boolean isFluidValid(int i, @NotNull FluidStack fluidStack) {
        return container.isFluidValid(i, new ForgeFluidHolder(fluidStack));
    }

    @Override
    public int fill(FluidStack fluidStack, FluidAction fluidAction) {
        int i = (int) this.container.insertFluid(new ForgeFluidHolder(fluidStack), fluidAction.simulate());
        if (i > 0 && fluidAction.execute()) this.container.update(entity);
        return i;
    }

    @Override
    public @NotNull FluidStack drain(FluidStack fluidStack, FluidAction fluidAction) {
        FluidStack fluidStack1 = new ForgeFluidHolder(this.container.extractFluid(new ForgeFluidHolder(fluidStack), fluidAction.simulate())).getFluidStack();
        if(!fluidStack1.isEmpty() && fluidAction.execute()) this.container.update(entity);
        return fluidStack1;
    }

    @Override
    public @NotNull FluidStack drain(int i, FluidAction fluidAction) {
        FluidHolder holder = this.container.getFluids().stream().filter(Predicate.not(FluidHolder::isEmpty)).findFirst().orElse(FluidHolder.empty());
        if (i <= 0 || holder.isEmpty()) return FluidStack.EMPTY;
        FluidStack fluidStack = new ForgeFluidHolder(this.container.extractFluid(holder, fluidAction.simulate())).getFluidStack();
        if(!fluidStack.isEmpty() && fluidAction.execute()) this.container.update(entity);
        return fluidStack;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction arg) {
        return capability == ForgeCapabilities.FLUID_HANDLER && container.getContainer(arg) != null ? LazyOptional.of(() -> this).cast() : LazyOptional.empty();
    }

    @Override
    public Serializable getSerializable() {
        return container;
    }
}
