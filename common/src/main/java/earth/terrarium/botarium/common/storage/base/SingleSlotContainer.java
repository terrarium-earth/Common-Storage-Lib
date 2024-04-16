package earth.terrarium.botarium.common.storage.base;

import earth.terrarium.botarium.common.storage.impl.ContainerSlotWrapper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public interface SingleSlotContainer<T> extends BasicContainer<T> {
    T getValue();

    int getLimit();

    boolean isValueValid(T value);

    static <T> Iterator<SingleSlotContainer<T>> iterator(SlottedContainer<T> container) {
        return new Iterator<>() {
            private int slot = 0;

            @Override
            public boolean hasNext() {
                return slot < container.getSlotCount();
            }

            @Override
            public SingleSlotContainer<T> next() {
                return new ContainerSlotWrapper<>(container, slot++);
            }
        };
    }

    static <T> List<SingleSlotContainer<T>> list(SlottedContainer<T> container) {
        ArrayList<SingleSlotContainer<T>> list = new ArrayList<>();
        for (int slot = 0; slot < container.getSlotCount(); slot++) {
            list.add(new ContainerSlotWrapper<>(container, slot));
        }
        return list;
    }
}
