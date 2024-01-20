package earth.terrarium.botarium.impl.fluid;

import earth.terrarium.botarium.common.fluid.base.BotariumFluidBlock;
import earth.terrarium.botarium.common.fluid.base.FluidContainer;
import earth.terrarium.botarium.common.fluid.base.FluidHolder;
import earth.terrarium.botarium.impl.AutoSerializable;
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

public record ForgeFluidContainer(BotariumFluidBlock<?> fluidGetter, BlockEntity entity) implements ICapabilityProvider {

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction arg) {
        var container = fluidGetter.getFluidContainer(entity.getLevel(), entity.getBlockPos(), entity.getBlockState(), entity, arg);
        return capability == ForgeCapabilities.FLUID_HANDLER && container != null ? LazyOptional.of(() -> new ForgeFluidHandler<>(container)).cast() : LazyOptional.empty();
    }

    public record ForgeFluidHandler<T extends FluidContainer & Updatable>(T container) implements IFluidHandler, AutoSerializable {

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
            return this.container.isFluidValid(i, new ForgeFluidHolder(fluidStack));
        }

        @Override
        public int fill(FluidStack fluidStack, FluidAction fluidAction) {
            int i = (int) this.container.insertFluid(new ForgeFluidHolder(fluidStack), fluidAction.simulate());
            if (i > 0 && fluidAction.execute()) this.container.update();
            return i;
        }

        @Override
        public @NotNull FluidStack drain(FluidStack fluidStack, FluidAction fluidAction) {
            FluidStack fluidStack1 = new ForgeFluidHolder(this.container.extractFluid(new ForgeFluidHolder(fluidStack), fluidAction.simulate())).getFluidStack();
            if (!fluidStack1.isEmpty() && fluidAction.execute()) this.container.update();
            return fluidStack1;
        }

        @Override
        public @NotNull FluidStack drain(int i, FluidAction fluidAction) {
            FluidHolder fluid = this.container.getFluids().get(0).copyHolder();
            if (fluid.isEmpty()) return FluidStack.EMPTY;
            fluid.setAmount(i);
            FluidStack fluidStack = new ForgeFluidHolder(this.container.extractFluid(fluid, fluidAction.simulate())).getFluidStack();
            if (!fluidStack.isEmpty() && fluidAction.execute()) this.container.update();
            return fluidStack;
        }

        @Override
        public Serializable getSerializable() {
            return container;
        }
    }
}
