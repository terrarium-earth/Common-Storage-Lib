package earth.terrarium.botarium.storage.fabric;

import earth.terrarium.botarium.storage.base.UpdateManager;
import net.fabricmc.fabric.api.transfer.v1.transaction.base.SnapshotParticipant;
import org.jetbrains.annotations.Nullable;

public class OptionalSnapshotParticipant<T> extends SnapshotParticipant<T> {
    public final UpdateManager<T> updateManager;

    public OptionalSnapshotParticipant(UpdateManager<T> updateManager) {
        this.updateManager = updateManager;
    }

    @Nullable
    public static OptionalSnapshotParticipant<?> of(Object updateManager) {
        if (updateManager instanceof UpdateManager<?> manager) {
            return new OptionalSnapshotParticipant<>(manager);
        } else {
            return null;
        }
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
