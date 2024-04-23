package earth.terrarium.botarium.common.data.utils;

import com.mojang.serialization.Codec;
import earth.terrarium.botarium.Botarium;
import earth.terrarium.botarium.common.data.DataManager;
import earth.terrarium.botarium.common.data.DataManagerRegistry;
import earth.terrarium.botarium.common.data.impl.FluidContainerData;
import earth.terrarium.botarium.common.data.impl.ItemContainerData;
import earth.terrarium.botarium.common.data.impl.SingleFluidData;
import earth.terrarium.botarium.common.data.impl.SingleItemData;
import earth.terrarium.botarium.common.transfer.impl.FluidUnit;
import earth.terrarium.botarium.common.transfer.impl.ItemUnit;
import net.minecraft.network.codec.ByteBufCodecs;

public class ContainerDataManagers {
    public static final DataManagerRegistry REGISTRY = DataManagerRegistry.create(Botarium.MOD_ID);

    public static final DataManager<FluidContainerData> FLUID_CONTENTS = REGISTRY.builder(FluidContainerData.DEFAULT).serialize(FluidContainerData.CODEC).syncToClient(FluidContainerData.NETWORK_CODEC).withDataComponent().copyOnDeath().buildAndRegister("fluid_contents");

    public static final DataManager<ItemContainerData> ITEM_CONTENTS = REGISTRY.builder(ItemContainerData.DEFAULT).serialize(ItemContainerData.CODEC).syncToClient(ItemContainerData.NETWORK_CODEC).withDataComponent().copyOnDeath().buildAndRegister("item_contents");

    public static final DataManager<SingleItemData> SINGLE_ITEM_CONTENTS = REGISTRY.builder(SingleItemData.DEFAULT).serialize(SingleItemData.CODEC).syncToClient(SingleItemData.STREAM_CODEC).withDataComponent().copyOnDeath().buildAndRegister("single_item_contents");

    public static final DataManager<SingleFluidData> SINGLE_FLUID_CONTENTS = REGISTRY.builder(SingleFluidData.DEFAULT).serialize(SingleFluidData.CODEC).syncToClient(SingleFluidData.STREAM_CODEC).withDataComponent().copyOnDeath().buildAndRegister("single_fluid_contents");

    public static final DataManager<FluidUnit> FLUID_UNIT = REGISTRY.builder(() -> FluidUnit.BLANK).serialize(FluidUnit.CODEC).syncToClient(FluidUnit.STREAM_CODEC).withDataComponent().copyOnDeath().buildAndRegister("fluid_unit");

    public static final DataManager<ItemUnit> ITEM_UNIT = REGISTRY.builder(() -> ItemUnit.BLANK).serialize(ItemUnit.CODEC).syncToClient(ItemUnit.STREAM_CODEC).withDataComponent().copyOnDeath().buildAndRegister("item_unit");

    public static final DataManager<Long> LONG_CONTENTS = REGISTRY.builder(() -> 0L).serialize(Codec.LONG).syncToClient(ByteBufCodecs.VAR_LONG).withDataComponent().copyOnDeath().buildAndRegister("energy_contents");
}
