package earth.terrarium.botarium.fabric.fluid;

import earth.terrarium.botarium.api.fluid.FluidSnapshot;
import net.fabricmc.fabric.api.transfer.v1.transaction.base.SnapshotParticipant;

public abstract class ExtendedFluidContainer extends SnapshotParticipant<FluidSnapshot> {
    @Override
    abstract public void onFinalCommit();

    @Override
    abstract public FluidSnapshot createSnapshot();

    @Override
    abstract public void readSnapshot(FluidSnapshot snapshot);
}
