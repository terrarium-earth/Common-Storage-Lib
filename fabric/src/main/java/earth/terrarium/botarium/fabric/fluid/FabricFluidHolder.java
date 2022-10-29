package earth.terrarium.botarium.fabric.fluid;

import earth.terrarium.botarium.api.fluid.FluidHolder;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.fabricmc.fabric.api.transfer.v1.transaction.base.SnapshotParticipant;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

@SuppressWarnings("UnstableApiUsage")
public class FabricFluidHolder extends SnapshotParticipant<FabricFluidHolder> implements FluidHolder, StorageView<FluidVariant> {
    public static final FabricFluidHolder EMPTY = FabricFluidHolder.of(Fluids.EMPTY, null, 0);
    private FluidVariant fluidVariant;
    private long amount;

    private FabricFluidHolder(FluidVariant variant, long amount) {
        this.fluidVariant = variant;
        this.amount = amount;
    }

    public static FabricFluidHolder of(FluidVariant variant, long amount) {
        return new FabricFluidHolder(variant, amount);
    }

    public static FabricFluidHolder of(Fluid variant, CompoundTag compoundTag, long amount) {
        return new FabricFluidHolder(FluidVariant.of(variant, compoundTag), amount);
    }

    public static FabricFluidHolder of(FluidHolder fluidHolder) {
        return new FabricFluidHolder(FluidVariant.of(fluidHolder.getFluid(), fluidHolder.getCompound()), fluidHolder.getFluidAmount());
    }

    public FluidVariant toVariant() {
        return FluidVariant.of(this.getFluid(), this.getCompound());
    }

    @Override
    public Fluid getFluid() {
        return fluidVariant.getFluid();
    }

    @Override
    public void setFluid(Fluid fluid) {
        fluidVariant = FluidVariant.of(fluid);
    }

    @Override
    public long extract(FluidVariant resource, long maxAmount, TransactionContext transaction) {
        if (this.fluidVariant.nbtMatches(resource.getNbt()) && this.fluidVariant.isOf(resource.getFluid())) {
            long extracted = Mth.clamp(maxAmount, 0, this.getFluidAmount());
            this.updateSnapshots(transaction);
            this.amount -= extracted;
            return extracted;
        }
        return 0;
    }

    @Override
    public boolean isResourceBlank() {
        return fluidVariant.isBlank();
    }

    @Override
    public FluidVariant getResource() {
        return this.toVariant();
    }

    @Override
    public long getAmount() {
        return getFluidAmount();
    }

    @Override
    public long getFluidAmount() {
        return amount;
    }

    @Override
    public long getCapacity() {
        return Integer.MAX_VALUE;
    }

    @Override
    public void setAmount(long amount) {
        this.amount = amount;
    }

    @Override
    public CompoundTag getCompound() {
        return fluidVariant.getNbt();
    }

    @Override
    public void setCompound(CompoundTag tag) {
        this.fluidVariant = FluidVariant.of(fluidVariant.getFluid(), tag);
    }

    @Override
    public boolean isEmpty() {
        return this.fluidVariant.isBlank() || amount == 0;
    }

    @Override
    public boolean matches(FluidHolder fluidHolder) {
        return this.fluidVariant.isOf(fluidHolder.getFluid()) && this.fluidVariant.nbtMatches(fluidHolder.getCompound());
    }

    @Override
    public FabricFluidHolder copyHolder() {
        return FabricFluidHolder.of(getFluid(), getCompound() == null ? null : getCompound().copy(), getFluidAmount());
    }

    @Override
    public CompoundTag serialize() {
        CompoundTag compoundTag = new CompoundTag();
        compoundTag.putString("Fluid", Registry.FLUID.getKey(getFluid()).toString());
        compoundTag.putLong("Amount", getFluidAmount());
        if(this.getCompound() != null) {
            compoundTag.put("Nbt", getCompound());
        }
        return compoundTag;
    }

    @Override
    public void deserialize(CompoundTag compound) {
        this.amount = compound.getLong("Amount");
        CompoundTag tag = null;
        if(compound.contains("Nbt")) {
            tag = compound.getCompound("Nbt");
        }
        this.fluidVariant = FluidVariant.of(Registry.FLUID.get(new ResourceLocation(compound.getString("Fluid"))), tag);
    }

    @Override
    protected FabricFluidHolder createSnapshot() {
        return this.copyHolder();
    }

    @Override
    protected void readSnapshot(FabricFluidHolder snapshot) {
        this.fluidVariant = FluidVariant.of(snapshot.getFluid(), snapshot.getCompound());
        this.setAmount(snapshot.getFluidAmount());
    }
}
