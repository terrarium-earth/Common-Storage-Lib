package earth.terrarium.botarium.mixin;

import earth.terrarium.botarium.common.energy.base.BotariumEnergyBlock;
import earth.terrarium.botarium.common.fluid.base.BotariumFluidBlock;
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
        if (this instanceof BotariumEnergyBlock<?> energyBlock) {
            energyBlock.getEnergyStorage().deserialize(compoundTag);
        }
        if (this instanceof BotariumFluidBlock<?> fluidHoldingBlock) {
            fluidHoldingBlock.getFluidContainer().deserialize(compoundTag);
        }
        if (this instanceof ItemContainerBlock itemContainerBlock) {
            itemContainerBlock.getContainer().deserialize(compoundTag);
        }
    }

    @Inject(method = "saveAdditional", at = @At("TAIL"))
    public void serializeData(CompoundTag compoundTag, CallbackInfo ci) {
        if (this instanceof BotariumEnergyBlock<?> energyBlock) {
            energyBlock.getEnergyStorage().serialize(compoundTag);
        }
        if (this instanceof BotariumFluidBlock<?> fluidHoldingBlock) {
            fluidHoldingBlock.getFluidContainer().serialize(compoundTag);
        }
        if (this instanceof ItemContainerBlock itemContainerBlock) {
            itemContainerBlock.getContainer().serialize(compoundTag);
        }
    }
}
