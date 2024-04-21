package earth.terrarium.botarium.common.storage;

import earth.terrarium.botarium.common.context.ItemContext;
import earth.terrarium.botarium.common.context.impl.PlayerContext;
import earth.terrarium.botarium.common.energy.EnergyApi;
import earth.terrarium.botarium.common.energy.EnergyProvider;
import earth.terrarium.botarium.common.fluid.FluidProvider;
import earth.terrarium.botarium.common.item.ItemApi;
import earth.terrarium.botarium.common.item.ItemProvider;
import earth.terrarium.botarium.common.storage.base.LongContainer;
import earth.terrarium.botarium.common.storage.base.UnitContainer;
import earth.terrarium.botarium.common.storage.impl.SimpleContainer;
import earth.terrarium.botarium.common.storage.util.TransferUtil;
import earth.terrarium.botarium.common.transfer.impl.FluidUnit;
import earth.terrarium.botarium.common.transfer.impl.ItemUnit;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class TestItem extends Item implements FluidProvider.Item, ItemProvider.Item, EnergyProvider.Item {

    public TestItem(Properties properties) {
        super(properties);
    }

    @Override
    public UnitContainer<FluidUnit> getFluids(ItemStack stack, ItemContext context) {
        return new SimpleContainer.Fluid(10, 10, context);
    }

    @Override
    public LongContainer getEnergy(ItemStack stack, ItemContext context) {
        return null;
    }

    @Override
    public UnitContainer<ItemUnit> getItems(ItemStack stack, ItemContext context) {
        return null;
    }

    public static void findAndTransfer(Player player, int fromSlot, int toSlot, ItemUnit transferUnit, long transferAmount) {
        PlayerContext fromContext = PlayerContext.ofSlot(player.getInventory(), fromSlot);
        PlayerContext toContext = PlayerContext.ofSlot(player.getInventory(), toSlot);

        UnitContainer<ItemUnit> fromContainer = fromContext.find(ItemApi.ITEM);
        UnitContainer<ItemUnit> toContainer = toContext.find(ItemApi.ITEM);

        TransferUtil.move(fromContainer, toContainer, transferUnit, transferAmount, false);
    }
}
