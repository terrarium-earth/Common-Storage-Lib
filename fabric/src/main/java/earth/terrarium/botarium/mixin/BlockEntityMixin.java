package earth.terrarium.botarium.mixin;

import earth.terrarium.botarium.api.energy.EnergyBlock;
import earth.terrarium.botarium.api.fluid.FluidHoldingBlock;
import earth.terrarium.botarium.api.item.ItemContainerBlock;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.ParametersAreNonnullByDefault;

@Mixin(BlockEntity.class)
@ParametersAreNonnullByDefault
public class BlockEntityMixin {

    @Inject(method = "load", at = @At("TAIL"))
    public void deserializeData(CompoundTag compoundTag, CallbackInfo ci) {
        if (this instanceof EnergyBlock energyBlock) energyBlock.getEnergyStorage().deserialize(compoundTag);
        if (this instanceof FluidHoldingBlock fluidHoldingBlock) fluidHoldingBlock.getFluidContainer().deserialize(compoundTag);
        if (this instanceof ItemContainerBlock itemContainerBlock) itemContainerBlock.getContainer().deserialize(compoundTag);
    }

    @Inject(method = "saveAdditional", at = @At("TAIL"))
    public void serializeData(CompoundTag compoundTag, CallbackInfo ci) {
        if (this instanceof EnergyBlock energyBlock) energyBlock.getEnergyStorage().serialize(compoundTag);
        if (this instanceof FluidHoldingBlock fluidHoldingBlock) fluidHoldingBlock.getFluidContainer().serialize(compoundTag);
        if (this instanceof ItemContainerBlock itemContainerBlock) itemContainerBlock.getContainer().serialize(compoundTag);
    }
}
