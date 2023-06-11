package earth.terrarium.botarium.fabric.fluid.storage;

import earth.terrarium.botarium.common.fluid.base.FluidSnapshot;
import net.fabricmc.fabric.api.transfer.v1.transaction.base.SnapshotParticipant;

public abstract class ExtendedFluidContainer extends SnapshotParticipant<FluidSnapshot> {
    @Override
    abstract public void onFinalCommit();

    @Override
    abstract public FluidSnapshot createSnapshot();

    @Override
    abstract public void readSnapshot(FluidSnapshot snapshot);
}
