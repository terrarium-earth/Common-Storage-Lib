package earth.terrarium.botarium.common.fluid.item;

import earth.terrarium.botarium.common.context.ItemContext;
import earth.terrarium.botarium.common.data.impl.SingleItemData;
import earth.terrarium.botarium.common.data.utils.ContainerDataManagers;
import earth.terrarium.botarium.common.storage.base.UnitContainer;
import earth.terrarium.botarium.common.storage.base.UnitSlot;
import earth.terrarium.botarium.common.storage.util.TransferUtil;
import earth.terrarium.botarium.common.storage.util.UpdateManager;
import earth.terrarium.botarium.common.transfer.impl.FluidUnit;
import earth.terrarium.botarium.common.transfer.impl.ItemUnit;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.PatchedDataComponentMap;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class StackedFluidContainer implements UnitContainer<FluidUnit>, UnitSlot<FluidUnit>, UpdateManager<SingleItemData> {
    private final long stackLimit;
    private final ItemContext context;
    private FluidUnit storedUnit;

    public StackedFluidContainer(long stackLimit, ItemStack stack, ItemContext context) {
        this.stackLimit = stackLimit;
        this.context = context;
        this.storedUnit = ContainerDataManagers.FLUID_UNIT.getData(stack);
    }

    @Override
    public @NotNull UnitSlot<FluidUnit> getSlot(int slot) {
        return this;
    }

    @Override
    public int getSlotCount() {
        return 1;
    }

    @Override
    public long getLimit() {
        return stackLimit * context.getAmount();
    }

    @Override
    public boolean isValueValid(FluidUnit unit) {
        return true;
    }

    @Override
    public FluidUnit getUnit() {
        return storedUnit;
    }

    @Override
    public long getAmount() {
        return storedUnit.isBlank() ? 0 : context.getAmount() * stackLimit;
    }

    public ItemUnit fill(ItemUnit unit, FluidUnit fluid) {
        PatchedDataComponentMap components = PatchedDataComponentMap.fromPatch(DataComponentMap.EMPTY, unit.components());
        components.set(ContainerDataManagers.FLUID_UNIT.componentType(), fluid);
        return ItemUnit.of(unit.unit(), components.asPatch());
    }

    public ItemUnit drain(ItemUnit unit) {
        PatchedDataComponentMap components = PatchedDataComponentMap.fromPatch(DataComponentMap.EMPTY, unit.components());
        components.set(ContainerDataManagers.FLUID_UNIT.componentType(), FluidUnit.BLANK);
        return ItemUnit.of(unit.unit(), components.asPatch());
    }

    @Override
    public long insert(FluidUnit unit, long amount, boolean simulate) {
        if (this.storedUnit.isBlank()) {
            long toExchange = context.exchange(fill(context.getUnit(), unit), amount / stackLimit, simulate);
            return toExchange * stackLimit;
        }
        return 0;
    }

    @Override
    public long extract(FluidUnit unit, long amount, boolean simulate) {
        if (!this.storedUnit.isBlank()) {
            long toExchange = context.exchange(drain(context.getUnit()), amount / stackLimit, true);
            return toExchange * stackLimit;
        }
        return 0;
    }

    @Override
    public SingleItemData createSnapshot() {
        return new SingleItemData(context.getUnit(), context.getAmount());
    }

    @Override
    public void readSnapshot(SingleItemData snapshot) {
        TransferUtil.replace(context, context.getUnit(), snapshot.item(), snapshot.amount(), false);
    }

    @Override
    public void update() {
        context.updateAll();
        this.storedUnit = ContainerDataManagers.FLUID_UNIT.getData(context.getUnit().toStack());
    }
}
