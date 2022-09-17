package testmod;

import earth.terrarium.botarium.api.energy.EnergyContainer;
import earth.terrarium.botarium.api.energy.EnergyItem;
import earth.terrarium.botarium.api.energy.ItemEnergyContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ExampleItem extends Item implements EnergyItem {
    public ExampleItem(Properties properties) {
        super(properties);
    }

    @Override
    public EnergyContainer getEnergyStorage(ItemStack stack) {
        return new ItemEnergyContainer(1000000);
    }
}
