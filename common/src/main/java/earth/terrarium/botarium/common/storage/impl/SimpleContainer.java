package earth.terrarium.botarium.common.storage.impl;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import earth.terrarium.botarium.common.storage.base.ContainerSlot;
import earth.terrarium.botarium.common.storage.base.UnitContainer;
import earth.terrarium.botarium.common.transfer.base.TransferUnit;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.world.item.component.CustomData;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class SimpleContainer<T extends TransferUnit<?>> implements UnitContainer<T> {
    private final NonNullList<SimpleSlot<T>> slots;
    private final Codec<T> codec;
    private final Codec<SimpleSlot<T>> slotCodec;


    public SimpleContainer(int size, T initialValue, long slotLimit, Codec<T> codec) {
        this.slots = NonNullList.<SimpleSlot<T>>withSize(size, new SimpleSlot<>(initialValue, slotLimit, codec, this::update));
        this.codec = codec;
        this.slotCodec = RecordCodecBuilder.create(instance -> instance.group(
                codec.fieldOf("unit").forGetter(SimpleSlot::getUnit),
                Codec.LONG.fieldOf("amount").forGetter(SimpleSlot::getAmount)
        ).apply(instance, (unit, amount) -> {
            SimpleSlot<T> slot = new SimpleSlot<>(initialValue, slotLimit, codec, this::update);
            slot.setAmount(amount);
            slot.setUnit(unit);
            return slot;
        }));
    }

    @Override
    public int getSlotCount() {
        return slots.size();
    }

    @Override
    public @NotNull ContainerSlot<T> getSlot(int slot) {
        return this.slots.get(slot);
    }

    @Override
    public long insert(T unit, long amount, boolean simulate) {
        long leftover = amount;
        for (ContainerSlot<T> slot : slots) {
            leftover -= slot.insert(unit, amount, simulate);
            if (leftover == 0) {
                break;
            }
        }
        return amount - leftover;
    }

    @Override
    public long extract(T unit, long amount, boolean simulate) {
        long leftover = amount;
        for (ContainerSlot<T> slot : slots) {
            leftover -= slot.extract(unit, amount, simulate);
            if (leftover == 0) {
                break;
            }
        }
        return amount - leftover;
    }

    @Override
    public DataComponentPatch createSnapshot() {
        CompoundTag tag = new CompoundTag();
        slotCodec.listOf().encode(this.slots, NbtOps.INSTANCE, tag);
        return DataComponentPatch.builder().set(DataComponents.CUSTOM_DATA, CustomData.of(tag)).build();
    }

    @Override
    public void readSnapshot(DataComponentPatch snapshot) {
        Optional<? extends CustomData> customData = snapshot.get(DataComponents.CUSTOM_DATA);
        if (customData != null) {
            customData.ifPresent(data -> {
                slotCodec.listOf().parse(NbtOps.INSTANCE, data.copyTag()).result().ifPresent(simpleSlots -> {
                    slots.clear();
                    for (int index = 0; index < simpleSlots.size(); index++) {
                        slots.set(index, simpleSlots.get(index));
                    }
                });
            });
        }
    }

    @Override
    public void update() {

    }
}
