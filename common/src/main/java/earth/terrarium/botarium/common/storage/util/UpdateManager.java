package earth.terrarium.botarium.common.storage.util;

import net.minecraft.core.component.DataComponentPatch;

public interface UpdateManager {
    DataComponentPatch createSnapshot();

    void readSnapshot(DataComponentPatch snapshot);

    void update();

    static void update(UpdateManager... managers) {
        for (UpdateManager manager : managers) {
            manager.update();
        }
    }
}
