package earth.terrarium.botarium.common.storage.util;

import net.minecraft.core.component.DataComponentPatch;

public interface UpdateManager<T> {
    T createSnapshot();

    void readSnapshot(T snapshot);

    void update();

    static void update(Object... managers) {
        for (Object potential : managers) {
            if (potential instanceof UpdateManager) {
                ((UpdateManager<?>) potential).update();
            }
        }
    }
}
