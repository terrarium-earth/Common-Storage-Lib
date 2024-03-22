package earth.terrarium.botarium.mixin;

import earth.terrarium.botarium.common.energy.EnergyApi;
import earth.terrarium.botarium.common.energy.base.EnergyContainer;
import earth.terrarium.botarium.common.fluid.FluidApi;
import earth.terrarium.botarium.common.fluid.base.FluidContainer;
import earth.terrarium.botarium.common.item.ItemContainerBlock;
import earth.terrarium.botarium.common.item.base.BotariumItemBlock;
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
        FluidContainer fluidContainer = FluidApi.getAPIFluidContainer(this.getLevel(), this.getBlockPos(), this.getBlockState(), (BlockEntity) (Object) this, null);
        if (fluidContainer != null) {
            fluidContainer.deserialize(compoundTag);
        }
        EnergyContainer energyContainer = EnergyApi.getAPIEnergyContainer(this.getLevel(), this.getBlockPos(), this.getBlockState(), (BlockEntity) (Object) this, null);
        if (energyContainer != null) {
            energyContainer.deserialize(compoundTag);
        }
        if (this instanceof ItemContainerBlock itemContainerBlock) {
            itemContainerBlock.getContainer().deserialize(compoundTag);
        }
        if (this instanceof BotariumItemBlock<?> botariumItemBlock) {
            ItemContainer itemContainer = botariumItemBlock.getItemContainer(this.getLevel(), this.getBlockPos(), this.getBlockState(), (BlockEntity) (Object) this, null);
            if (itemContainer instanceof Serializable serializable) {
                serializable.deserialize(compoundTag);
            }
        }
    }

    @Inject(method = "saveAdditional", at = @At("TAIL"))
    public void serializeData(CompoundTag compoundTag, CallbackInfo ci) {
        FluidContainer fluidContainer = FluidApi.getAPIFluidContainer(this.getLevel(), this.getBlockPos(), this.getBlockState(), (BlockEntity) (Object) this, null);
        if (fluidContainer != null) {
            fluidContainer.serialize(compoundTag);
        }
        EnergyContainer energyContainer = EnergyApi.getAPIEnergyContainer(this.getLevel(), this.getBlockPos(), this.getBlockState(), (BlockEntity) (Object) this, null);
        if (energyContainer != null) {
            energyContainer.serialize(compoundTag);
        }
        if (this instanceof ItemContainerBlock itemContainerBlock) {
            itemContainerBlock.getContainer().serialize(compoundTag);
        }
        if (this instanceof BotariumItemBlock<?> botariumItemBlock) {
            ItemContainer itemContainer = botariumItemBlock.getItemContainer(this.getLevel(), this.getBlockPos(), this.getBlockState(), (BlockEntity) (Object) this, null);
            if (itemContainer instanceof Serializable serializable) {
                serializable.serialize(compoundTag);
            }
        }
    }
}
