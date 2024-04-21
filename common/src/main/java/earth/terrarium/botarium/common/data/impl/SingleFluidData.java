package earth.terrarium.botarium.common.data.impl;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import earth.terrarium.botarium.common.data.utils.ContainerSerializer;
import earth.terrarium.botarium.common.storage.impl.SimpleSlot;
import earth.terrarium.botarium.common.storage.impl.SingleUnitContainer;
import earth.terrarium.botarium.common.transfer.impl.FluidUnit;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.level.material.Fluid;

import java.util.function.Supplier;

public record SingleFluidData(FluidUnit fluid, long amount) {
    public static final SingleFluidData EMPTY = new SingleFluidData(FluidUnit.BLANK, 0);

    public static final Supplier<SingleFluidData> DEFAULT = () -> EMPTY;

    public static final Codec<SingleFluidData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BuiltInRegistries.FLUID.byNameCodec().fieldOf("id").forGetter(data -> data.fluid.unit()),
            DataComponentPatch.CODEC.fieldOf("components").forGetter(data -> data.fluid.components()),
            Codec.LONG.fieldOf("amount").forGetter(SingleFluidData::amount)
    ).apply(instance, SingleFluidData::of));

    public static final StreamCodec<RegistryFriendlyByteBuf, SingleFluidData> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public SingleFluidData decode(RegistryFriendlyByteBuf buf) {
            Fluid fluid = BuiltInRegistries.FLUID.byId(buf.readVarInt());
            DataComponentPatch components = DataComponentPatch.STREAM_CODEC.decode(buf);
            long amount = buf.readLong();
            return SingleFluidData.of(fluid, components, amount);
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, SingleFluidData data) {
            buf.writeVarInt(BuiltInRegistries.FLUID.getId(data.fluid.unit()));
            DataComponentPatch.STREAM_CODEC.encode(buf, data.fluid.components());
            buf.writeLong(data.amount);
        }
    };

    public static final ContainerSerializer<SimpleSlot<FluidUnit, ?>, SingleFluidData> SERIALIZER = new ContainerSerializer<>() {
        @Override
        public SingleFluidData captureData(SimpleSlot<FluidUnit, ?> container) {
            return new SingleFluidData(container.getUnit(), container.getAmount());
        }

        @Override
        public void applyData(SimpleSlot<FluidUnit, ?> container, SingleFluidData snapshot) {
            container.setUnit(snapshot.fluid);
            container.setAmount(snapshot.amount);
        }
    };

    private static SingleFluidData of(Fluid item, DataComponentPatch components, long amount) {
        return new SingleFluidData(FluidUnit.of(item, components), amount);
    }
}
