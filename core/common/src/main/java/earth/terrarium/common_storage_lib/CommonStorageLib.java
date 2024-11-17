package earth.terrarium.common_storage_lib;

import earth.terrarium.common_storage_lib.energy.EnergyApi;
import earth.terrarium.common_storage_lib.energy.EnergyProvider;
import earth.terrarium.common_storage_lib.fluid.FluidApi;
import earth.terrarium.common_storage_lib.fluid.util.FluidProvider;
import earth.terrarium.common_storage_lib.heat.HeatApi;
import earth.terrarium.common_storage_lib.heat.HeatProvider;
import earth.terrarium.common_storage_lib.item.ItemApi;
import earth.terrarium.common_storage_lib.item.input.ItemConsumerRegistry;
import earth.terrarium.common_storage_lib.item.util.ItemProvider;

public class CommonStorageLib {
    public static final String MOD_ID = "common_storage_lib";

    public static void init() {
        ItemConsumerRegistry.init();
    }
}
