package earth.terrarium.botarium.testmod.items;

import earth.terrarium.botarium.context.ItemContext;
import earth.terrarium.botarium.context.impl.PlayerContext;
import earth.terrarium.botarium.energy.EnergyApi;
import earth.terrarium.botarium.energy.EnergyProvider;
import earth.terrarium.botarium.energy.impl.SimpleValueStorage;
import earth.terrarium.botarium.fluid.FluidApi;
import earth.terrarium.botarium.fluid.impl.SimpleFluidStorage;
import earth.terrarium.botarium.fluid.util.FluidProvider;
import earth.terrarium.botarium.item.ItemApi;
import earth.terrarium.botarium.item.impl.SimpleItemStorage;
import earth.terrarium.botarium.item.util.ItemProvider;
import earth.terrarium.botarium.resources.fluid.FluidResource;
import earth.terrarium.botarium.resources.fluid.util.FluidAmounts;
import earth.terrarium.botarium.resources.item.ItemResource;
import earth.terrarium.botarium.storage.base.CommonStorage;
import earth.terrarium.botarium.storage.base.ValueStorage;
import earth.terrarium.botarium.storage.util.TransferUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class TransferTestItem extends Item implements EnergyProvider.Item, FluidProvider.Item, ItemProvider.Item {
    public TransferTestItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();

        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        BlockPos blockPos = context.getClickedPos();
        Direction direction = context.getClickedFace();
        Player player = context.getPlayer();
        InteractionHand hand = context.getHand();

        if (player != null) {
            PlayerContext playerContext = PlayerContext.ofHand(player, hand);

            CommonStorage<ItemResource> foundItems = ItemApi.BLOCK.find(level, blockPos, direction);
            if (foundItems != null) {
                CommonStorage<ItemResource> itemStorage = playerContext.find(ItemApi.ITEM);
                if (itemStorage != null) {
                    TransferUtil.moveAny(foundItems, itemStorage, 1, false);
                }
            }

            CommonStorage<FluidResource> foundFluids = FluidApi.BLOCK.find(level, blockPos, direction);
            if (foundFluids != null) {
                CommonStorage<FluidResource> fluidStorage = playerContext.find(FluidApi.ITEM);
                if (fluidStorage != null) {
                    TransferUtil.moveAny(foundFluids, fluidStorage, FluidAmounts.BOTTLE, false);
                }
            }

            ValueStorage foundEnergy = EnergyApi.BLOCK.find(level, blockPos, direction);
            if (foundEnergy != null) {
                ValueStorage energyStorage = playerContext.find(EnergyApi.ITEM);
                if (energyStorage != null) {
                    TransferUtil.moveValue(foundEnergy, energyStorage, 50, false);
                }
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public ValueStorage getEnergy(ItemStack stack, ItemContext context) {
        return new SimpleValueStorage(1000, context);
    }

    @Override
    public CommonStorage<FluidResource> getFluids(ItemStack stack, ItemContext context) {
        return new SimpleFluidStorage(1, FluidAmounts.BUCKET, context);
    }

    @Override
    public CommonStorage<ItemResource> getItems(ItemStack stack, ItemContext context) {
        return new SimpleItemStorage(1, context);
    }
}
