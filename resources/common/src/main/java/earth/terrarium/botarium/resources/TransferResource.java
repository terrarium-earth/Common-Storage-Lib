package earth.terrarium.botarium.resources;

import net.minecraft.core.component.*;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Predicate;

public abstract class TransferResource<T, R extends TransferResource<T, R>> implements DataComponentHolder, Predicate<R> {
    protected final T type;
    protected final PatchedDataComponentMap components;
    private DataComponentPatch dataPatch;

    protected TransferResource(T type, PatchedDataComponentMap components) {
        this.type = type;
        this.components = components;
    }

    public abstract boolean isBlank();

    public T getType() {
        return type;
    }

    public DataComponentPatch getDataPatch() {
        DataComponentPatch patch = dataPatch;
        if (patch == null) {
            dataPatch = patch = components.isEmpty() ? DataComponentPatch.EMPTY : components.asPatch();
        }
        return patch;
    }

    public boolean isEmpty() {
        return components.isEmpty();
    }

    @Override
    public @NotNull DataComponentMap getComponents() {
        return components.isEmpty() ? DataComponentMap.EMPTY : components;
    }

    public boolean componentsMatch(DataComponentPatch other) {
        return Objects.equals(getDataPatch(), other);
    }

    public boolean isOf(T resource) {
        return this.getType() == resource;
    }

    @Override
    public boolean test(R other) {
        return isOf(other.getType()) && componentsMatch(other.getDataPatch());
    }

    public abstract <D> R set(DataComponentType<D> type, D value);

    public abstract R modify(DataComponentPatch patch);

    public abstract ResourceStack<R> toStack(long amount);

    public ResourceStack<R> toStack() {
        return toStack(1);
    }
}
