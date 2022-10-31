package earth.terrarium.botarium.fabric.fluid;

import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;

@SuppressWarnings("UnstableApiUsage")
public interface ManualSyncing {
    default void setChanged(TransactionContext transaction) {}
}
