package earth.terrarium.botarium.common.data.impl;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import earth.terrarium.botarium.common.storage.util.SlotExtras;
import earth.terrarium.botarium.common.storage.base.UnitSlot;
import earth.terrarium.botarium.common.transfer.impl.FluidUnit;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.level.material.Fluid;

import java.util.function.Supplier;

public record SingleFluidData(FluidUnit unit, long amount) {
    public static final SingleFluidData EMPTY = new SingleFluidData(FluidUnit.BLANK, 0);

    public static final Supplier<SingleFluidData> DEFAULT = () -> EMPTY;

    public static final Codec<SingleFluidData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BuiltInRegistries.FLUID.byNameCodec().fieldOf("id").forGetter(data -> data.unit.type()),
            DataComponentPatch.CODEC.fieldOf("components").forGetter(data -> data.unit.components()),
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
            buf.writeVarInt(BuiltInRegistries.FLUID.getId(data.unit.type()));
            DataComponentPatch.STREAM_CODEC.encode(buf, data.unit.components());
            buf.writeLong(data.amount);
        }
    };

    private static SingleFluidData of(Fluid item, DataComponentPatch components, long amount) {
        return new SingleFluidData(FluidUnit.of(item, components), amount);
    }

    public static SingleFluidData of(UnitSlot<FluidUnit> slot) {
        return new SingleFluidData(slot.getUnit(), slot.getAmount());
    }
}
