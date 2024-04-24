package earth.terrarium.botarium.common.data.impl;

import com.mojang.serialization.Codec;
import earth.terrarium.botarium.common.storage.util.ContainerExtras;
import earth.terrarium.botarium.common.storage.base.UnitContainer;
import earth.terrarium.botarium.common.storage.base.UnitSlot;
import earth.terrarium.botarium.common.transfer.impl.FluidUnit;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public record FluidContainerData(List<SingleFluidData> stacks) {
    public static final Codec<FluidContainerData> CODEC = SingleFluidData.CODEC.listOf().xmap(FluidContainerData::new, FluidContainerData::stacks);

    public static final FluidContainerData EMPTY = new FluidContainerData(List.of());

    public static final Supplier<FluidContainerData> DEFAULT = () -> EMPTY;

    public static final StreamCodec<RegistryFriendlyByteBuf, FluidContainerData> NETWORK_CODEC = ByteBufCodecs.collection(size -> (List<SingleFluidData>) new ArrayList<SingleFluidData>(), SingleFluidData.STREAM_CODEC).map(FluidContainerData::new, FluidContainerData::stacks);

    public static FluidContainerData of(UnitContainer<FluidUnit> container) {
        List<SingleFluidData> stacks = NonNullList.withSize(container.getSlotCount(), SingleFluidData.EMPTY);
        for (int i = 0; i < container.getSlotCount(); i++) {
            UnitSlot<FluidUnit> slot = container.getSlot(i);
            stacks.set(i, new SingleFluidData(slot.getUnit(), slot.getAmount()));
        }
        return new FluidContainerData(stacks);
    }
}
