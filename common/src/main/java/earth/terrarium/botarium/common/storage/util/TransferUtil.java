package earth.terrarium.botarium.common.storage.util;

import earth.terrarium.botarium.common.storage.base.UnitContainer;
import earth.terrarium.botarium.common.storage.base.UnitIO;
import earth.terrarium.botarium.common.storage.base.UnitSlot;
import earth.terrarium.botarium.common.storage.base.LongContainer;
import earth.terrarium.botarium.common.transfer.base.TransferUnit;
import earth.terrarium.botarium.common.transfer.impl.FluidUnit;
import earth.terrarium.botarium.common.transfer.impl.ItemUnit;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluid;

import java.util.Optional;
import java.util.function.Predicate;

public class TransferUtil {
    public static <T extends TransferUnit<?>> Optional<T> findUnit(UnitContainer<T> container, Predicate<T> predicate) {
        for (int i = 0; i < container.getSlotCount(); i++) {
            T unit = container.getSlot(i).getUnit();
            if (predicate.test(unit)) {
                return Optional.of(unit);
            }
        }
        return Optional.empty();
    }

    public static <T extends TransferUnit<?>> Optional<T> findFirstUnit(UnitContainer<T> container) {
        return findUnit(container, unit -> !unit.isBlank());
    }

    public static Predicate<ItemUnit> byItemTag(TagKey<Item> tag) {
        return unit -> unit.unit().builtInRegistryHolder().is(tag);
    }

    public static Predicate<FluidUnit> byFluidTag(TagKey<Fluid> tag) {
        return unit -> unit.unit().builtInRegistryHolder().is(tag);
    }

    public static <T extends TransferUnit<?>> long move(UnitIO<T> from, UnitIO<T> to, T unit, long amount, boolean simulate) {
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

    public static <T extends TransferUnit<?>> Tuple<T, Long> moveFiltered(UnitContainer<T> from, UnitIO<T> to, Predicate<T> filter, long amount, boolean simulate) {
        Optional<T> optional = findUnit(from, filter);
        if (optional.isPresent()) {
            T unit = optional.get();
            long moved = move(from, to, unit, amount, simulate);
            return new Tuple<>(unit, moved);
        }
        return new Tuple<>(null, 0L);
    }

    public static <T extends TransferUnit<?>> Tuple<T, Long> moveAny(UnitContainer<T> from, UnitIO<T> to, long amount, boolean simulate) {
        for (int i = 0; i < from.getSlotCount(); i++) {
            T unit = from.getSlot(i).getUnit();
            if (unit.isBlank()) continue;
            long moved = move(from, to, unit, amount, simulate);
            if (moved > 0) {
                return new Tuple<>(unit, moved);
            }
        }
        return new Tuple<>(null, 0L);
    }

    public static <T extends TransferUnit<?>> void moveAll(UnitContainer<T> from, UnitIO<T> to, boolean simulate) {
        for (int i = 0; i < from.getSlotCount(); i++) {
            T unit = from.getSlot(i).getUnit();
            if (unit.isBlank()) continue;
            move(from, to, unit, Long.MAX_VALUE, simulate);
        }
    }

    public static long moveValue(LongContainer from, LongContainer to, long amount, boolean simulate) {
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

    public static <T extends TransferUnit<?>> long exchange(UnitIO<T> io, T oldUnit, T newUnit, long amount, boolean simulate) {
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

    public static <T extends TransferUnit<?>> long replace(UnitIO<T> io, T oldUnit, T newUnit, long amount, boolean simulate) {
        long extracted = io.extract(oldUnit, amount, false);
        if (extracted > 0) {
            long inserted = io.insert(newUnit, amount, true);
            if (!simulate) {
                io.insert(newUnit, amount, false);
            } else {
                io.insert(oldUnit, extracted, false);
            }
        }
        return 0;
    }

    public static <T extends TransferUnit<?>> long insertSlots(UnitContainer<T> container, T unit, long amount, boolean simulate) {
        return insertSubset(container, 0, container.getSlotCount(), unit, amount, simulate);
    }

    public static <T extends TransferUnit<?>> long insertSubset(UnitContainer<T> container, int start, int end, T unit, long amount, boolean simulate) {
        long inserted = 0;
        for (int i = start; i < end; i++) {
            UnitSlot<T> slot = container.getSlot(i);
            if (!slot.getUnit().isBlank()) {
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

    public static <T extends TransferUnit<?>> long extractSlots(UnitContainer<T> container, T unit, long amount, boolean simulate) {
        return extractSubset(container, 0, container.getSlotCount(), unit, amount, simulate);
    }

    public static <T extends TransferUnit<?>> long extractSubset(UnitContainer<T> container, int start, int end, T unit, long amount, boolean simulate) {
        long extracted = 0;
        for (int i = start; i < end; i++) {
            extracted += container.getSlot(i).extract(unit, amount - extracted, simulate);
            if (extracted >= amount) {
                return extracted;
            }
        }
        return extracted;
    }

    public static <T extends TransferUnit<?>> void equalize(UnitSlot<T> slot, long amount) {
        T unit = slot.getUnit();
        long current = slot.getAmount();
        if (current < amount) {
            slot.insert(unit, amount - current, false);
        } else if (current > amount) {
            slot.extract(unit, current - amount, false);
        }
    }
}
