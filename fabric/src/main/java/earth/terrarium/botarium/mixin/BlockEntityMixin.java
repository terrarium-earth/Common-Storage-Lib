package earth.terrarium.botarium.mixin;

import earth.terrarium.botarium.api.energy.EnergyHoldable;
import earth.terrarium.botarium.api.fluid.FluidHoldable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockEntity.class)
public class BlockEntityMixin {

    @Inject(method = "load", at = @At("TAIL"))
    public void deserializeData(CompoundTag compoundTag, CallbackInfo ci) {
        if(this instanceof EnergyHoldable energyHoldable) {
            energyHoldable.getEnergyStorage().deseralize(compoundTag);
        }
        if(this instanceof FluidHoldable fluidHoldable) {
            fluidHoldable.getFluidContainer().deseralize(compoundTag);
        }
    }

    @Inject(method = "saveAdditional", at = @At("TAIL"))
    public void serializeData(CompoundTag compoundTag, CallbackInfo ci) {
        if(this instanceof EnergyHoldable energyHoldable) {
            energyHoldable.getEnergyStorage().serialize(compoundTag);
        }
        if(this instanceof FluidHoldable fluidHoldable) {
            fluidHoldable.getFluidContainer().serialize(compoundTag);
        }
    }
}
