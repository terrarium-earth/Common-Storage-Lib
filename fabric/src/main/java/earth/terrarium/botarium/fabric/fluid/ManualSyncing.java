package earth.terrarium.botarium.fabric.fluid;

import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;

import javax.annotation.ParametersAreNonnullByDefault;

@SuppressWarnings("UnstableApiUsage")
@ParametersAreNonnullByDefault
public interface ManualSyncing {
    default void setChanged(TransactionContext transaction) {}
    default void finalChange() {}
}
