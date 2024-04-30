package earth.terrarium.botarium.storage.unit;

import net.minecraft.core.component.DataComponentHolder;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponentType;

import java.util.Objects;

public interface TransferUnit<T, U extends TransferUnit<T, U>> extends DataComponentHolder {
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

    default boolean matches(U other) {
        return isOf(other.getType()) && componentsMatch(other.getComponents());
    }

    <D> U set(DataComponentType<D> type, D value);

    U modify(DataComponentPatch patch);

    UnitStack<U> toStack(long amount);

    default UnitStack<U> toStack() {
        return toStack(1);
    }
}
