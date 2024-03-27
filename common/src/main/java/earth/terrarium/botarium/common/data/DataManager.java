package earth.terrarium.botarium.common.data;

import com.mojang.serialization.Codec;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.lang3.NotImplementedException;

import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public interface DataManager<T> {
    T getData(Object dataHolder);

    T getDataOrThrow(Object dataHolder);

    T getDataOrInit(Object dataHolder);

    T getDataOrInit(Object dataHolder, T data);

    T setData(Object dataHolder, T data);

    boolean hasData(Object dataHolder);

    default T modifyData(Object dataHolder, UnaryOperator<T> modifier) {
        T data = getData(dataHolder);
        T modifiedData = modifier.apply(data);
        setData(dataHolder, modifiedData);
        return modifiedData;
    }
}
