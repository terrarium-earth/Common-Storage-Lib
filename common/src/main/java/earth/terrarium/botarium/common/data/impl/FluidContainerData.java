package earth.terrarium.botarium.common.data.impl;

import com.mojang.serialization.Codec;
import earth.terrarium.botarium.common.data.utils.ContainerSerializer;
import earth.terrarium.botarium.common.storage.base.UnitSlot;
import earth.terrarium.botarium.common.storage.impl.SimpleContainer;
import earth.terrarium.botarium.common.storage.impl.SimpleSlot;
import earth.terrarium.botarium.common.transfer.impl.FluidUnit;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public record FluidContainerData(List<SingleFluidData> holders) {
    public static final Codec<FluidContainerData> CODEC = SingleFluidData.CODEC.listOf().xmap(FluidContainerData::new, FluidContainerData::holders);

    public static final FluidContainerData EMPTY = new FluidContainerData(List.of());

    public static final Supplier<FluidContainerData> DEFAULT = () -> EMPTY;

    public static final StreamCodec<RegistryFriendlyByteBuf, FluidContainerData> NETWORK_CODEC = ByteBufCodecs.collection(size -> (List<SingleFluidData>) new ArrayList<SingleFluidData>(), SingleFluidData.STREAM_CODEC).map(FluidContainerData::new, FluidContainerData::holders);

    public static final ContainerSerializer<SimpleContainer<FluidUnit, ?, ?>, FluidContainerData> SERIALIZER = new ContainerSerializer<>() {
        @Override
        public @NotNull FluidContainerData captureData(SimpleContainer<FluidUnit, ?, ?> buf) {
            List<SingleFluidData> holders = NonNullList.withSize(buf.getSlotCount(), SingleFluidData.EMPTY);
            for (int i = 0; i < buf.getSlotCount(); i++) {
                UnitSlot<FluidUnit> slot = buf.getSlot(i);
                holders.set(i, new SingleFluidData(slot.getUnit(), slot.getAmount()));
            }
            return new FluidContainerData(holders);
        }

        @Override
        public void applyData(SimpleContainer<FluidUnit, ?, ?> object, FluidContainerData data) {
            for (int i = 0; i < Math.min(object.getSlotCount(), data.holders.size()); i++) {
                SimpleSlot<FluidUnit, ?> slot = object.getSlots().get(i);
                var holder = data.holders().get(i);
                slot.setUnit(holder.fluid());
                slot.setAmount(holder.amount());
            }
        }
    };
}
