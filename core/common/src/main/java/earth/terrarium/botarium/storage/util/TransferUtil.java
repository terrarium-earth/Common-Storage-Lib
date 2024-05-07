package earth.terrarium.botarium.storage.util;

import earth.terrarium.botarium.resources.TransferResource;
import earth.terrarium.botarium.resources.ResourceStack;
import earth.terrarium.botarium.resources.fluid.FluidResource;
import earth.terrarium.botarium.resources.fluid.ingredient.SizedFluidIngredient;
import earth.terrarium.botarium.resources.item.ItemResource;
import earth.terrarium.botarium.storage.base.*;
import net.minecraft.Optionull;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

public class TransferUtil {
    public static <T> Optional<T> findResource(CommonStorage<T> container, Predicate<T> predicate) {
        for (int i = 0; i < container.getSlotCount(); i++) {
            StorageSlot<T> slot = container.getSlot(i);
            if (slot.isBlank()) continue;
            T resource = slot.getResource();
            if (predicate.test(resource)) {
                return Optional.of(resource);
            }
        }
        return Optional.empty();
    }

    public static <T> Optional<T> findFirstResource(CommonStorage<T> container) {
        return findResource(container, resource -> true);
    }

    public static Predicate<ItemResource> byItemTag(TagKey<Item> tag) {
        return resource -> resource.is(tag);
    }

    public static Predicate<ItemResource> byIngredient(Ingredient ingredient) {
        return resource -> ingredient.test(resource.getCachedStack());
    }

    public static Predicate<FluidResource> byFluidTag(TagKey<Fluid> tag) {
        return resource -> resource.is(tag);
    }

    public static <T> long move(StorageIO<T> from, StorageIO<T> to, T resource, long amount, boolean simulate) {
        long extracted = from.extract(resource, amount, true);
        long inserted = to.insert(resource, extracted, true);
        if (extracted > 0 && inserted > 0) {
            if (inserted != extracted) {
                extracted = from.extract(resource, Math.min(extracted, inserted), true);
                inserted = to.insert(resource, Math.min(extracted, inserted), true);
            }
            if (extracted == inserted) {
                from.extract(resource, extracted, simulate);
                to.insert(resource, extracted, simulate);
                UpdateManager.batch(from, to);
                return extracted;
            }
        }
        return 0;
    }

    public static <T> Tuple<T, Long> moveFiltered(CommonStorage<T> from, StorageIO<T> to, Predicate<T> filter, long amount, boolean simulate) {
        Optional<T> optional = findResource(from, filter);
        if (optional.isPresent()) {
            T resource = optional.get();
            long moved = move(from, to, resource, amount, simulate);
            return new Tuple<>(resource, moved);
        }
        return new Tuple<>(null, 0L);
    }

    public static <T> Tuple<T, Long> moveAny(CommonStorage<T> from, StorageIO<T> to, long amount, boolean simulate) {
        for (int i = 0; i < from.getSlotCount(); i++) {
            StorageSlot<T> slot = from.getSlot(i);
            if (slot.isBlank()) continue;
            T resource = slot.getResource();
            long moved = move(from, to, resource, amount, simulate);
            if (moved > 0) {
                return new Tuple<>(resource, moved);
            }
        }
        return new Tuple<>(null, 0L);
    }

    public static <T> void moveAll(CommonStorage<T> from, StorageIO<T> to, boolean simulate) {
        for (int i = 0; i < from.getSlotCount(); i++) {
            StorageSlot<T> slot = from.getSlot(i);
            if (slot.isBlank()) continue;
            T resource = slot.getResource();
            move(from, to, resource, Long.MAX_VALUE, simulate);
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

    public static <T> long exchange(StorageIO<T> io, T oldresource, T newresource, long amount, boolean simulate) {
        long extracted = io.extract(oldresource, amount, false);
        if (extracted > 0) {
            long inserted = io.insert(newresource, extracted, true);
            if (extracted == inserted && !simulate) {
                io.insert(newresource, extracted, false);
            } else {
                io.insert(oldresource, extracted, false);
            }
            return extracted == inserted ? extracted : 0;
        }
        return 0;
    }

    public static <T> long insertSlots(CommonStorage<T> container, T resource, long amount, boolean simulate) {
        return insertSubset(container, 0, container.getSlotCount(), resource, amount, simulate);
    }

    public static <T> long insertSubset(CommonStorage<T> container, int start, int end, T resource, long amount, boolean simulate) {
        long inserted = 0;
        for (int i = start; i < end; i++) {
            StorageSlot<T> slot = container.getSlot(i);
            if (!slot.isBlank()) {
                inserted += slot.insert(resource, amount - inserted, simulate);
                if (inserted >= amount) {
                    return inserted;
                }
            }
        }
        for (int i = start; i < end; i++) {
            inserted += container.getSlot(i).insert(resource, amount - inserted, simulate);
            if (inserted >= amount) {
                return inserted;
            }
        }
        return inserted;
    }

    public static <T> long extractSlots(CommonStorage<T> container, T resource, long amount, boolean simulate) {
        return extractSubset(container, 0, container.getSlotCount(), resource, amount, simulate);
    }

    public static <T> long extractSubset(CommonStorage<T> container, int start, int end, T resource, long amount, boolean simulate) {
        long extracted = 0;
        for (int i = start; i < end; i++) {
            extracted += container.getSlot(i).extract(resource, amount - extracted, simulate);
            if (extracted >= amount) {
                return extracted;
            }
        }
        return extracted;
    }

    public static <T> void equalize(StorageSlot<T> slot, long amount) {
        T resource = slot.getResource();
        long current = slot.getAmount();
        if (current < amount) {
            slot.insert(resource, amount - current, false);
        } else if (current > amount) {
            slot.extract(resource, current - amount, false);
        }
    }

    public static <T extends TransferResource<?, T>> long insertStack(CommonStorage<T> container, ResourceStack<T> stack, boolean simulate) {
        return insertSlots(container, stack.resource(), stack.amount(), simulate);
    }

    public static <T extends TransferResource<?, T>> long extractStack(CommonStorage<T> container, ResourceStack<T> stack, boolean simulate) {
        return extractSlots(container, stack.resource(), stack.amount(), simulate);
    }

    @Nullable
    public static <T extends TransferResource<?, T>> ResourceStack<T> extractPredicate(CommonStorage<T> container, Predicate<T> predicate, long amount, boolean simulate) {
        Set<T> resources = new HashSet<>();
        ResourceStack<T> stack = null;
        for (int i = 0; i < container.getSlotCount(); i++) {
            StorageSlot<T> slot = container.getSlot(i);
            if (slot.isBlank()) continue;
            T resource = slot.getResource();
            if (predicate.test(resource)) {
                if (resources.contains(resource)) continue;
                resources.add(resource);
                ResourceStack<T> newStack = new ResourceStack<>(resource, slot.extract(resource, amount, simulate));
                if (stack == null || newStack.amount() > stack.amount()) {
                    stack = newStack;
                    if (stack.amount() >= amount) {
                        break;
                    }
                }
            }
        }
        return stack;
    }

    public static ResourceStack<ItemResource> extractItem(CommonStorage<ItemResource> container, Predicate<ItemResource> predicate, long amount, boolean simulate) {
        return Optional.ofNullable(extractPredicate(container, predicate, amount, simulate)).orElse(ResourceStack.EMPTY_ITEM);
    }

    public static ResourceStack<FluidResource> extractFluid(CommonStorage<FluidResource> container, Predicate<FluidResource> predicate, long amount, boolean simulate) {
        return Optional.ofNullable(extractPredicate(container, predicate, amount, simulate)).orElse(ResourceStack.EMPTY_FLUID);
    }

    public static ResourceStack<FluidResource> extractFluid(CommonStorage<FluidResource> container, SizedFluidIngredient ingredient, boolean simulate) {
        return extractFluid(container, ingredient.ingredient(), ingredient.getAmount(), simulate);
    }
}
