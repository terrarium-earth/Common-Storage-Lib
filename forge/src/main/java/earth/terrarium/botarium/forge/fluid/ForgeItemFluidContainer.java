package earth.terrarium.botarium.forge.fluid;

import earth.terrarium.botarium.common.fluid.base.FluidHolder;
import earth.terrarium.botarium.common.fluid.base.ItemFluidContainer;
import earth.terrarium.botarium.util.Updatable;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public record ForgeItemFluidContainer<T extends ItemFluidContainer & Updatable<ItemStack>>(T container, ItemStack itemStack) implements IFluidHandlerItem, ICapabilityProvider {

    @Override
    public @NotNull ItemStack getContainer() {
        return container.getContainerItem();
    }

    @Override
    public int getTanks() {
        return container.getSize();
    }

    @Override
    public @NotNull FluidStack getFluidInTank(int i) {
        return new ForgeFluidHolder(container.getFluids().get(i)).getFluidStack();
    }

    @Override
    public int getTankCapacity(int i) {
        return (int) container.getTankCapacity(i);
    }

    @Override
    public boolean isFluidValid(int i, @NotNull FluidStack fluidStack) {
        return container.isFluidValid(i, new ForgeFluidHolder(fluidStack));
    }

    @Override
    public int fill(FluidStack fluidStack, FluidAction fluidAction) {
        long filled = this.container.insertFluid(new ForgeFluidHolder(fluidStack), fluidAction.simulate());
        container.update(itemStack);
        return (int) filled;
    }

    @Override
    public @NotNull FluidStack drain(FluidStack fluidStack, FluidAction fluidAction) {
        FluidStack drained = new ForgeFluidHolder(this.container.extractFluid(new ForgeFluidHolder(fluidStack), fluidAction.simulate())).getFluidStack();
        container.update(itemStack);
        return drained;
    }

    @Override
    public @NotNull FluidStack drain(int i, FluidAction fluidAction) {
        FluidHolder holder = this.container.getFluids().stream().filter(Predicate.not(FluidHolder::isEmpty)).findFirst().orElse(FluidHolder.empty());
        if (i <= 0 || holder.isEmpty()) return FluidStack.EMPTY;
        FluidHolder fluid = this.container.getFirstFluid().copyWithAmount(i);
        FluidStack fluidStack = new ForgeFluidHolder(this.container.extractFluid(fluid, fluidAction.simulate())).getFluidStack();
        if(!fluidStack.isEmpty() && fluidAction.execute()) this.container.update(itemStack);
        return fluidStack;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction arg) {
        LazyOptional<IFluidHandlerItem> of = LazyOptional.of(container.getContainer(arg) != null ? () -> this : null);
        return capability.orEmpty(ForgeCapabilities.FLUID_HANDLER_ITEM, of.cast()).cast();
    }
}
