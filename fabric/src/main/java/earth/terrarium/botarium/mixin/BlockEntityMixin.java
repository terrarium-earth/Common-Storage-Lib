package earth.terrarium.botarium.mixin;

import earth.terrarium.botarium.api.EnergyMarker;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockEntity.class)
public class BlockEntityMixin {

    @Inject(method = "load", at = @At("TAIL"))
    public void deserializeEnergy(CompoundTag compoundTag, CallbackInfo ci) {
        if(this instanceof EnergyMarker<?>) {
            ((EnergyMarker<BlockEntity>) this).getEnergyStorage((BlockEntity) (Object) this).deseralize(compoundTag);
        }
    }

    @Inject(method = "saveAdditional", at = @At("TAIL"))
    public void serializeEnergy(CompoundTag compoundTag, CallbackInfo ci) {
        if(this instanceof EnergyMarker<?>) {
            ((EnergyMarker<BlockEntity>) this).getEnergyStorage((BlockEntity) (Object) this).serialize(compoundTag);
        }
    }
}
