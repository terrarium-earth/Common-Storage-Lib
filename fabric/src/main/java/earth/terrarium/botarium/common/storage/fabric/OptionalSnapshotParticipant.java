package earth.terrarium.botarium.common.storage.fabric;

import earth.terrarium.botarium.common.storage.util.UpdateManager;
import net.fabricmc.fabric.api.transfer.v1.transaction.base.SnapshotParticipant;

public class OptionalSnapshotParticipant<T> extends SnapshotParticipant<T> {
    public final UpdateManager<T> updateManager;

    public OptionalSnapshotParticipant(UpdateManager<T> updateManager) {
        this.updateManager = updateManager;
    }

    @Override
    protected void onFinalCommit() {
        updateManager.update();
    }

    @Override
    protected T createSnapshot() {
        return updateManager.createSnapshot();
    }

    @Override
    protected void readSnapshot(T snapshot) {
        updateManager.readSnapshot(snapshot);
    }
}
