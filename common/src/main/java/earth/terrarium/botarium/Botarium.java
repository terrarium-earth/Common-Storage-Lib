package earth.terrarium.botarium;


import earth.terrarium.botarium.common.data.utils.ContainerDataManagers;
import earth.terrarium.botarium.common.energy.EnergyApi;
import earth.terrarium.botarium.common.energy.EnergyProvider;
import earth.terrarium.botarium.common.fluid.FluidApi;
import earth.terrarium.botarium.common.fluid.FluidProvider;
import earth.terrarium.botarium.common.heat.HeatApi;
import earth.terrarium.botarium.common.heat.HeatProvider;
import earth.terrarium.botarium.common.item.ItemApi;
import earth.terrarium.botarium.common.item.ItemProvider;

public class Botarium {
    public static final String MOD_ID = "botarium";

    public static void init() {
        ContainerDataManagers.REGISTRY.initialize();

        //Energy

        EnergyApi.ITEM.registerFallback((stack, context) -> {
            if (stack.getItem() instanceof EnergyProvider.Item provider) {
                return provider.getEnergy(stack, context);
            } else {
                return null;
            }
        }, item -> item instanceof EnergyProvider.Item);

        EnergyApi.BLOCK.registerFallback((level, pos, state, entity, direction) -> {
            if (state.getBlock() instanceof EnergyProvider.Block provider) {
                return provider.getEnergy(level, pos, state, entity, direction);
            } else {
                return null;
            }
        }, block -> block instanceof EnergyProvider.Block);

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

        FluidApi.BLOCK.registerFallback((level, pos, state, entity, direction) -> {
            if (state.getBlock() instanceof FluidProvider.Block provider) {
                return provider.getFluids(level, pos, state, entity, direction);
            } else {
                return null;
            }
        }, block -> block instanceof FluidProvider.Block);

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

        ItemApi.BLOCK.registerFallback((level, pos, state, entity, direction) -> {
            if (state.getBlock() instanceof ItemProvider.Block provider) {
                return provider.getItems(level, pos, state, entity, direction);
            } else {
                return null;
            }
        }, block -> block instanceof ItemProvider.Block);

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

        HeatApi.BLOCK.registerFallback((level, pos, state, entity, direction) -> {
            if (state.getBlock() instanceof HeatProvider.Block provider) {
                return provider.getHeat(level, pos, state, entity, direction);
            } else {
                return null;
            }
        }, block -> block instanceof HeatProvider.Block);

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
