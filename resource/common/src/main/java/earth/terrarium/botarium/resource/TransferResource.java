package earth.terrarium.botarium.resource;

import net.minecraft.core.component.DataComponentHolder;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponentType;

import java.util.Objects;

public interface TransferResource<T, R extends TransferResource<T, R>> extends DataComponentHolder {
    boolean isBlank();

    T getType();

    DataComponentPatch getDataPatch();

    default boolean hasComponents() {
        return !getComponents().isEmpty();
    }

    default boolean componentsMatch(DataComponentMap other) {
        return Objects.equals(getComponents(), other);
    }

    default boolean isOf(T unit) {
        return this.getType() == unit;
    }

    default boolean matches(R other) {
        return isOf(other.getType()) && componentsMatch(other.getComponents());
    }

    <D> R set(DataComponentType<D> type, D value);

    R modify(DataComponentPatch patch);

    ResourceStack<R> toStack(long amount);

    default ResourceStack<R> toStack() {
        return toStack(1);
    }
}
