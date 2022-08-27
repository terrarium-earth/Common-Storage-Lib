package earth.terrarium.botarium;

import earth.terrarium.botarium.api.EnergyHoldable;
import earth.terrarium.botarium.api.EnergyMarker;
import earth.terrarium.botarium.api.ItemEnergyStorage;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ExampleItem extends Item implements EnergyMarker<ItemStack> {
    public ExampleItem(Properties properties) {
        super(properties);
    }

    @Override
    public EnergyHoldable getEnergyStorage(ItemStack stack) {
        return new ItemEnergyStorage(stack, 1000000);
    }
}
