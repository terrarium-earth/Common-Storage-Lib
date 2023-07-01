package earth.terrarium.botarium.common.registry.fluid;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluid;
import net.msrandom.extensions.annotations.ImplementedByExtension;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FluidBucketItem extends BucketItem {

    @ImplementedByExtension
    public FluidBucketItem(FluidData data, Properties properties) {
        super((Fluid) null, null); //Need to declare the type for null or will crash when compiling forge.
        throw new NotImplementedException();
    }

    @ImplementedByExtension
    public FluidData getData() {
        throw new NotImplementedException();
    }

    @Override
    public InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        if (getData().getInformation().canPlace()) {
            return super.use(level, player, hand);
        }
        return InteractionResultHolder.fail(player.getItemInHand(hand));
    }

    @Override
    protected void playEmptySound(@Nullable Player player, @NotNull LevelAccessor level, @NotNull BlockPos pos) {
        SoundEvent event = getData().getInformation().sounds().getSound("bucket_empty");
        if (event == null) event = SoundEvents.BUCKET_EMPTY;
        level.playSound(player, pos, event, SoundSource.BLOCKS, 1.0F, 1.0F);
        level.gameEvent(player, GameEvent.FLUID_PLACE, pos);
    }
}
