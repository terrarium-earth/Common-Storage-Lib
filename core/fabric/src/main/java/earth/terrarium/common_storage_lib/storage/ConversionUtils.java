package earth.terrarium.common_storage_lib.storage;

import earth.terrarium.common_storage_lib.resources.fluid.FluidResource;
import earth.terrarium.common_storage_lib.resources.item.ItemResource;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.fabricmc.fabric.impl.transfer.transaction.TransactionManagerImpl;

public class ConversionUtils {
    public static ItemResource toResource(ItemVariant variant) {
        return ItemResource.of(variant.getItem(), variant.getComponents());
    }

    public static ItemVariant toVariant(ItemResource resource) {
        return ItemVariant.of(resource.getItem(), resource.getDataPatch());
    }

    public static FluidResource toResource(FluidVariant variant) {
        return FluidResource.of(variant.getFluid(), variant.getComponents());
    }

    public static FluidVariant toVariant(FluidResource resource) {
        return FluidVariant.of(resource.getType(), resource.getDataPatch());
    }

    public static Transaction getTransaction() {
        if(!Transaction.isOpen()) return Transaction.openOuter(); // == None, we open a transaction
        if(Transaction.getLifecycle() == Transaction.Lifecycle.OPEN) return Transaction.getCurrentUnsafe().openNested();
        throw new IllegalStateException("You may not transfer anything during an update call");
    }
}
