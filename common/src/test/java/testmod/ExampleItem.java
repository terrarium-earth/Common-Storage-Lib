package testmod;

import earth.terrarium.botarium.api.EnergyContainer;
import earth.terrarium.botarium.api.EnergyItem;
import earth.terrarium.botarium.api.ItemEnergyContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ExampleItem extends Item implements EnergyItem {
    public ExampleItem(Properties properties) {
        super(properties);
    }

    @Override
    public EnergyContainer getEnergyStorage(ItemStack stack) {
        return new ItemEnergyContainer(stack, 1000000);
    }
}
