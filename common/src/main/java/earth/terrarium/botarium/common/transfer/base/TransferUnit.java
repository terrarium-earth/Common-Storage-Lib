package earth.terrarium.botarium.common.transfer.base;

import net.minecraft.core.component.DataComponentPatch;

import java.util.Objects;

public interface TransferUnit<T> {
    boolean isBlank();

    T unit();

    DataComponentPatch components();

    default boolean hasComponents() {
        return !components().isEmpty();
    }

    default boolean componentsMatch(DataComponentPatch other) {
        return Objects.equals(this.components(), other);
    }

    default boolean isOf(T unit) {
        return this.unit() == unit;
    }

    default boolean matches(TransferUnit<T> other) {
        return isOf(other.unit()) && componentsMatch(other.components());
    }
}
