package earth.terrarium.botarium.common.data;

import net.minecraft.core.component.DataComponentType;
import org.jetbrains.annotations.Nullable;

import java.util.function.UnaryOperator;

public interface DataManager<T> {
    T getData(Object dataHolder);

    T getDataOrThrow(Object dataHolder);

    T getDataOrInit(Object dataHolder, T data);

    T setData(Object dataHolder, T data);

    T removeData(Object dataHolder);

    boolean hasData(Object dataHolder);

    @Nullable
    DataComponentType<T> componentType();

    default T modifyData(Object dataHolder, UnaryOperator<T> modifier) {
        T data = getData(dataHolder);
        T modifiedData = modifier.apply(data);
        setData(dataHolder, modifiedData);
        return modifiedData;
    }
}
