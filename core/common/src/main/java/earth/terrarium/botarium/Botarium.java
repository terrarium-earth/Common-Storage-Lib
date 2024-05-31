package earth.terrarium.botarium;

import earth.terrarium.botarium.energy.EnergyApi;
import earth.terrarium.botarium.energy.EnergyProvider;
import earth.terrarium.botarium.fluid.FluidApi;
import earth.terrarium.botarium.fluid.util.FluidProvider;
import earth.terrarium.botarium.heat.HeatApi;
import earth.terrarium.botarium.heat.HeatProvider;
import earth.terrarium.botarium.item.input.ItemConsumerRegistry;
import earth.terrarium.botarium.item.ItemApi;
import earth.terrarium.botarium.item.util.ItemProvider;

public class Botarium {
    public static final String MOD_ID = "botarium";

    public static void init() {
        ItemConsumerRegistry.init();

        //Energy

        EnergyApi.ITEM.registerFallback((stack, context) -> {
            if (stack.getItem() instanceof EnergyProvider.Item provider) {
                return provider.getEnergy(stack, context);
            } else {
                return null;
            }
        }, item -> item instanceof EnergyProvider.Item);

        EnergyApi.BLOCK.registerFallback((entity, direction) -> {
            if (entity instanceof EnergyProvider.BlockEntity provider) {
                return provider.getEnergy(direction);
            } else {
                return null;
            }
        });

        EnergyApi.ENTITY.registerFallback((entity, direction) -> {
            if (entity instanceof EnergyProvider.Entity provider) {
                return provider.getEnergy(direction);
            } else {
                return null;
            }
        });

        //Fluid

        FluidApi.ITEM.registerFallback((stack, context) -> {
            if (stack.getItem() instanceof FluidProvider.Item provider) {
                return provider.getFluids(stack, context);
            } else {
                return null;
            }
        }, item -> item instanceof FluidProvider.Item);

        FluidApi.BLOCK.registerFallback((entity, direction) -> {
            if (entity instanceof FluidProvider.BlockEntity provider) {
                return provider.getFluids(direction);
            } else {
                return null;
            }
        });

        FluidApi.ENTITY.registerFallback((entity, direction) -> {
            if (entity instanceof FluidProvider.Entity provider) {
                return provider.getFluids(direction);
            } else {
                return null;
            }
        });

        //Item

        ItemApi.ITEM.registerFallback((stack, context) -> {
            if (stack.getItem() instanceof ItemProvider.Item provider) {
                return provider.getItems(stack, context);
            } else {
                return null;
            }
        }, item -> item instanceof ItemProvider.Item);

        ItemApi.BLOCK.registerFallback((entity, direction) -> {
            if (entity instanceof ItemProvider.BlockEntity provider) {
                return provider.getItems(direction);
            } else {
                return null;
            }
        });

        ItemApi.ENTITY.registerFallback((entity, ignored) -> {
            if (entity instanceof ItemProvider.Entity provider) {
                return provider.getItems();
            } else {
                return null;
            }
        });

        ItemApi.ENTITY_AUTOMATION.registerFallback((entity, direction) -> {
            if (entity instanceof ItemProvider.AutomationEntity provider) {
                return provider.getItems(direction);
            } else {
                return null;
            }
        });

        // Heat

        HeatApi.BLOCK.registerFallback((entity, direction) -> {
            if (entity instanceof HeatProvider.BlockEntity provider) {
                return provider.getHeat(direction);
            } else {
                return null;
            }
        });

        HeatApi.ENTITY.registerFallback((entity, ignored) -> {
            if (entity instanceof HeatProvider.Entity provider) {
                return provider.getHeat();
            } else {
                return null;
            }
        });

        HeatApi.ITEM.registerFallback((stack, context) -> {
            if (stack.getItem() instanceof HeatProvider.Item provider) {
                return provider.getHeat(stack, context);
            } else {
                return null;
            }
        }, item -> item instanceof HeatProvider.Item);
    }
}
