package earth.terrarium.botarium.fluid.util;

import com.mojang.serialization.Codec;
import earth.terrarium.botarium.fluid.base.FluidUnit;
import earth.terrarium.botarium.storage.base.CommonStorage;
import earth.terrarium.botarium.storage.unit.UnitStack;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public record FluidStorageData(List<UnitStack<FluidUnit>> stacks) {
    public static final Codec<FluidStorageData> CODEC = UnitStack.FLUID_CODEC.listOf().xmap(FluidStorageData::new, FluidStorageData::stacks);

    public static final FluidStorageData EMPTY = new FluidStorageData(List.of());

    public static final Supplier<FluidStorageData> DEFAULT = () -> EMPTY;

    public static final StreamCodec<RegistryFriendlyByteBuf, FluidStorageData> NETWORK_CODEC = ByteBufCodecs.collection(size -> (List<UnitStack<FluidUnit>>) new ArrayList<UnitStack<FluidUnit>>(), UnitStack.FLUID_STREAM_CODEC).map(FluidStorageData::new, FluidStorageData::stacks);

    public static FluidStorageData from(CommonStorage<FluidUnit> container) {
        List<UnitStack<FluidUnit>> stacks = NonNullList.withSize(container.getSlotCount(), UnitStack.EMPTY_FLUID);
        for (int i = 0; i < container.getSlotCount(); i++) {
            stacks.set(i, UnitStack.from(container.getSlot(i)));
        }
        return new FluidStorageData(stacks);
    }
}
