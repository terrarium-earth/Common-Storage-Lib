package earth.terrarium.botarium.mixin;

import earth.terrarium.botarium.common.energy.base.BotariumEnergyBlock;
import earth.terrarium.botarium.common.fluid.base.BotariumFluidBlock;
import earth.terrarium.botarium.common.item.ItemApi;
import earth.terrarium.botarium.common.item.ItemContainerBlock;
import earth.terrarium.botarium.common.item.base.ItemContainer;
import earth.terrarium.botarium.util.Serializable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockEntity.class)
public abstract class BlockEntityMixin {
    @Shadow
    @Nullable
    public abstract Level getLevel();

    @Shadow
    public abstract BlockPos getBlockPos();

    @Shadow
    public abstract BlockState getBlockState();

    @Inject(method = "load", at = @At("TAIL"))
    public void deserializeData(CompoundTag compoundTag, CallbackInfo ci) {
        if (this instanceof BotariumEnergyBlock<?> energyBlock) {
            energyBlock.getEnergyStorage().deserialize(compoundTag);
        }
        if (this instanceof BotariumFluidBlock<?> fluidHoldingBlock) {
            fluidHoldingBlock.getFluidContainer().deserialize(compoundTag);
        }
        ItemContainer itemContainer = ItemApi.getAPIItemContainer(this.getLevel(), this.getBlockPos(), this.getBlockState(), (BlockEntity) (Object) this, null);
        if (itemContainer instanceof Serializable container) {
            container.deserialize(compoundTag);
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
        ItemContainer itemContainer = ItemApi.getAPIItemContainer(this.getLevel(), this.getBlockPos(), this.getBlockState(), (BlockEntity) (Object) this, null);
        if (itemContainer instanceof Serializable container) {
            container.serialize(compoundTag);
        }
    }
}
