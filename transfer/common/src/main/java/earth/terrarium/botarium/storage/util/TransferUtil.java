package earth.terrarium.botarium.storage.util;

import earth.terrarium.botarium.fluid.base.FluidUnit;
import earth.terrarium.botarium.item.base.ItemUnit;
import earth.terrarium.botarium.storage.base.*;
import earth.terrarium.botarium.storage.unit.TransferUnit;
import earth.terrarium.botarium.storage.unit.UnitStack;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluid;

import java.util.Optional;
import java.util.function.Predicate;

public class TransferUtil {
    public static <T> Optional<T> findUnit(CommonStorage<T> container, Predicate<T> predicate) {
        for (int i = 0; i < container.getSlotCount(); i++) {
            StorageSlot<T> slot = container.getSlot(i);
            if (slot.isBlank()) continue;
            T unit = slot.getUnit();
            if (predicate.test(unit)) {
                return Optional.of(unit);
            }
        }
        return Optional.empty();
    }

    public static <T> Optional<T> findFirstUnit(CommonStorage<T> container) {
        return findUnit(container, unit -> true);
    }

    public static Predicate<ItemUnit> byItemTag(TagKey<Item> tag) {
        return unit -> unit.getType().builtInRegistryHolder().is(tag);
    }

    public static Predicate<FluidUnit> byFluidTag(TagKey<Fluid> tag) {
        return unit -> unit.getType().builtInRegistryHolder().is(tag);
    }

    public static <T> long move(StorageIO<T> from, StorageIO<T> to, T unit, long amount, boolean simulate) {
        long extracted = from.extract(unit, amount, true);
        long inserted = to.insert(unit, extracted, true);
        if (extracted > 0 && inserted > 0) {
            if (inserted != extracted) {
                extracted = from.extract(unit, Math.min(extracted, inserted), true);
                inserted = to.insert(unit, Math.min(extracted, inserted), true);
            }
            if (extracted == inserted) {
                from.extract(unit, extracted, simulate);
                to.insert(unit, extracted, simulate);
                UpdateManager.batch(from, to);
                return extracted;
            }
        }
        return 0;
    }

    public static <T> Tuple<T, Long> moveFiltered(CommonStorage<T> from, StorageIO<T> to, Predicate<T> filter, long amount, boolean simulate) {
        Optional<T> optional = findUnit(from, filter);
        if (optional.isPresent()) {
            T unit = optional.get();
            long moved = move(from, to, unit, amount, simulate);
            return new Tuple<>(unit, moved);
        }
        return new Tuple<>(null, 0L);
    }

    public static <T> Tuple<T, Long> moveAny(CommonStorage<T> from, StorageIO<T> to, long amount, boolean simulate) {
        for (int i = 0; i < from.getSlotCount(); i++) {
            StorageSlot<T> slot = from.getSlot(i);
            if (slot.isBlank()) continue;
            T unit = slot.getUnit();
            long moved = move(from, to, unit, amount, simulate);
            if (moved > 0) {
                return new Tuple<>(unit, moved);
            }
        }
        return new Tuple<>(null, 0L);
    }

    public static <T> void moveAll(CommonStorage<T> from, StorageIO<T> to, boolean simulate) {
        for (int i = 0; i < from.getSlotCount(); i++) {
            StorageSlot<T> slot = from.getSlot(i);
            if (slot.isBlank()) continue;
            T unit = slot.getUnit();
            move(from, to, unit, Long.MAX_VALUE, simulate);
        }
    }

    public static long moveValue(ValueStorage from, ValueStorage to, long amount, boolean simulate) {
        long extracted = from.extract(amount, true);
        long inserted = to.insert(extracted, true);
        if (extracted > 0 && inserted > 0) {
            if (inserted != extracted) {
                extracted = from.extract(Math.min(extracted, inserted), true);
                inserted = to.insert(Math.min(extracted, inserted), true);
            }
            if (extracted == inserted) {
                from.extract(extracted, simulate);
                to.insert(extracted, simulate);
                UpdateManager.batch(from, to);
                return extracted;
            }
        }
        return 0;
    }

    public static <T> long exchange(StorageIO<T> io, T oldUnit, T newUnit, long amount, boolean simulate) {
        long extracted = io.extract(oldUnit, amount, false);
        if (extracted > 0) {
            long inserted = io.insert(newUnit, extracted, true);
            if (extracted == inserted && !simulate) {
                io.insert(newUnit, extracted, false);
            } else {
                io.insert(oldUnit, extracted, false);
            }
            return extracted == inserted ? extracted : 0;
        }
        return 0;
    }

    public static <T> long insertSlots(CommonStorage<T> container, T unit, long amount, boolean simulate) {
        return insertSubset(container, 0, container.getSlotCount(), unit, amount, simulate);
    }

    public static <T> long insertSubset(CommonStorage<T> container, int start, int end, T unit, long amount, boolean simulate) {
        long inserted = 0;
        for (int i = start; i < end; i++) {
            StorageSlot<T> slot = container.getSlot(i);
            if (!slot.isBlank()) {
                inserted += slot.insert(unit, amount - inserted, simulate);
                if (inserted >= amount) {
                    return inserted;
                }
            }
        }
        for (int i = start; i < end; i++) {
            inserted += container.getSlot(i).insert(unit, amount - inserted, simulate);
            if (inserted >= amount) {
                return inserted;
            }
        }
        return inserted;
    }

    public static <T> long extractSlots(CommonStorage<T> container, T unit, long amount, boolean simulate) {
        return extractSubset(container, 0, container.getSlotCount(), unit, amount, simulate);
    }

    public static <T> long extractSubset(CommonStorage<T> container, int start, int end, T unit, long amount, boolean simulate) {
        long extracted = 0;
        for (int i = start; i < end; i++) {
            extracted += container.getSlot(i).extract(unit, amount - extracted, simulate);
            if (extracted >= amount) {
                return extracted;
            }
        }
        return extracted;
    }

    public static <T> void equalize(StorageSlot<T> slot, long amount) {
        T unit = slot.getUnit();
        long current = slot.getAmount();
        if (current < amount) {
            slot.insert(unit, amount - current, false);
        } else if (current > amount) {
            slot.extract(unit, current - amount, false);
        }
    }

    public static <T extends TransferUnit<?, T>> long insertStack(CommonStorage<T> container, UnitStack<T> stack, boolean simulate) {
        return insertSlots(container, stack.unit(), stack.amount(), simulate);
    }
}
