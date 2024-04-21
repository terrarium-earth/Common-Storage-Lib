package earth.terrarium.botarium.common.fluid.wrappers;

import earth.terrarium.botarium.common.fluid.ConversionUtils;
import earth.terrarium.botarium.common.storage.base.UnitContainer;
import earth.terrarium.botarium.common.storage.base.UnitSlot;
import earth.terrarium.botarium.common.storage.util.TransferUtil;
import earth.terrarium.botarium.common.storage.util.UpdateManager;
import earth.terrarium.botarium.common.transfer.impl.FluidUnit;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public interface AbstractNeoFluidHandler extends IFluidHandler {
    UnitContainer<FluidUnit> container();

    @Override
    default int getTanks() {
        return container().getSlotCount();
    }

    @Override
    default @NotNull FluidStack getFluidInTank(int i) {
        UnitSlot<FluidUnit> slot = container().getSlot(i);
        return ConversionUtils.convert(slot.getUnit(), slot.getAmount());
    }

    @Override
    default int getTankCapacity(int i) {
        return (int) container().getSlot(i).getLimit();
    }

    @Override
    default boolean isFluidValid(int i, FluidStack fluidStack) {
        return container().getSlot(i).isValueValid(new FluidUnit(fluidStack.getFluid(), fluidStack.getComponentsPatch()));
    }

    @Override
    default int fill(FluidStack fluidStack, FluidAction fluidAction) {
        FluidUnit unit = ConversionUtils.convert(fluidStack);
        long amount = container().insert(unit, fluidStack.getAmount(), fluidAction.simulate());
        UpdateManager.batch(container());
        return (int) amount;
    }

    @Override
    default FluidStack drain(FluidStack fluidStack, FluidAction fluidAction) {
        FluidUnit unit = ConversionUtils.convert(fluidStack);
        long amount = container().extract(unit, fluidStack.getAmount(), fluidAction.simulate());
        UpdateManager.batch(container());
        return amount > 0 ? ConversionUtils.convert(unit, amount) : FluidStack.EMPTY;
    }

    @Override
    default FluidStack drain(int i, FluidAction fluidAction) {
        Optional<FluidUnit> unit = TransferUtil.findUnit(container(), (fluidUnit) -> !fluidUnit.isBlank());
        if (unit.isPresent()) {
            long amount = container().extract(unit.get(), i, fluidAction.simulate());
            UpdateManager.batch(container());
            return amount > 0 ? ConversionUtils.convert(unit.get(), amount) : FluidStack.EMPTY;
        } else {
            return FluidStack.EMPTY;
        }
    }
}
