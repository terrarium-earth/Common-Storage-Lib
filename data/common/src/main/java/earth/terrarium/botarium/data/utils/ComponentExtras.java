package earth.terrarium.botarium.data.utils;

import net.minecraft.core.component.DataComponentType;

public interface ComponentExtras {
    <T> T setComponent(DataComponentType<T> componentType, T data);
    <T> T removeComponent(DataComponentType<T> componentType);
}
