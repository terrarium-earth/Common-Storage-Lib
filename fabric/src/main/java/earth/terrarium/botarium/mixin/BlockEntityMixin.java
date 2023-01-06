package earth.terrarium.botarium.mixin;

import earth.terrarium.botarium.common.menu.base.EnergyAttachment;
import earth.terrarium.botarium.common.fluid.base.FluidAttachment;
import earth.terrarium.botarium.common.item.ItemContainerBlock;
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
        if (this instanceof EnergyAttachment energyBlock) {
            energyBlock.getEnergyStorage(this).deserialize(compoundTag);
        }
        if (this instanceof FluidAttachment fluidHoldingBlock) {
            fluidHoldingBlock.getFluidContainer(this).deserialize(compoundTag);
        }
        if (this instanceof ItemContainerBlock itemContainerBlock) {
            itemContainerBlock.getContainer().deserialize(compoundTag);
        }
    }

    @Inject(method = "saveAdditional", at = @At("TAIL"))
    public void serializeData(CompoundTag compoundTag, CallbackInfo ci) {
        if (this instanceof EnergyAttachment energyBlock) {
            energyBlock.getEnergyStorage(this).serialize(compoundTag);
        }
        if (this instanceof FluidAttachment fluidHoldingBlock) {
            fluidHoldingBlock.getFluidContainer(this).serialize(compoundTag);
        }
        if (this instanceof ItemContainerBlock itemContainerBlock) {
            itemContainerBlock.getContainer().serialize(compoundTag);
        }
    }
}
