package earth.terrarium.botarium.common.fluid.wrappers;

import earth.terrarium.botarium.common.context.ItemContext;
import earth.terrarium.botarium.common.fluid.ConversionUtils;
import earth.terrarium.botarium.common.storage.base.UnitSlot;
import earth.terrarium.botarium.common.storage.util.TransferUtil;
import earth.terrarium.botarium.common.transfer.impl.FluidUnit;
import earth.terrarium.botarium.common.transfer.impl.ItemUnit;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class CommonFluidItemContainer implements AbstractCommonFluidContainer {
    private final IFluidHandlerItem handler;
    private final ItemContext context;
    private ItemStack lastContainer;

    public CommonFluidItemContainer(IFluidHandlerItem handler, ItemContext context) {
        this.handler = handler;
        this.context = context;
        this.lastContainer = handler.getContainer().copy();
    }

    @Override
    public int getSlotCount() {
        return handler.getTanks();
    }

    @Override
    public long insert(FluidUnit unit, long amount, boolean simulate) {
        int fill = handler.fill(ConversionUtils.convert(unit, amount), simulate ? IFluidHandler.FluidAction.SIMULATE : IFluidHandler.FluidAction.EXECUTE);
        if (!simulate) {
            updateContext();
        }
        return fill;
    }

    @Override
    public long extract(FluidUnit unit, long amount, boolean simulate) {
        int drain = handler.drain(ConversionUtils.convert(unit, amount), simulate ? IFluidHandler.FluidAction.SIMULATE : IFluidHandler.FluidAction.EXECUTE).getAmount();
        if (!simulate) {
            updateContext();
        }
        return drain;
    }

    @Override
    public @NotNull UnitSlot<FluidUnit> getSlot(int slot) {
        return new DelegatingFluidHandlerSlot(this, slot);
    }

    public void updateContext() {
        if(ItemStack.matches(lastContainer, handler.getContainer())) {
            return;
        }
        lastContainer = handler.getContainer().copy();
        if (context.getUnit().matches(lastContainer)) {
            TransferUtil.equalize(context.mainSlot(), lastContainer.getCount());
        } else {
            if (context.exchange(ItemUnit.of(lastContainer), lastContainer.getCount(), false) != lastContainer.getCount()) {
                context.extract(ItemUnit.of(lastContainer), lastContainer.getCount(), false);
                context.mainSlot().insert(ItemUnit.of(lastContainer), lastContainer.getCount(), false);
            }
        }
    }

    @Override
    public IFluidHandlerItem handler() {
        return handler;
    }
}