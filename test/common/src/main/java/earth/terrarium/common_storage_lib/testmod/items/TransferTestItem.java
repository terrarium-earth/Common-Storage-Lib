package earth.terrarium.common_storage_lib.testmod.items;

import earth.terrarium.common_storage_lib.context.ItemContext;
import earth.terrarium.common_storage_lib.context.impl.PlayerContext;
import earth.terrarium.common_storage_lib.energy.EnergyApi;
import earth.terrarium.common_storage_lib.energy.EnergyProvider;
import earth.terrarium.common_storage_lib.energy.impl.SimpleValueStorage;
import earth.terrarium.common_storage_lib.fluid.FluidApi;
import earth.terrarium.common_storage_lib.fluid.impl.SimpleFluidStorage;
import earth.terrarium.common_storage_lib.fluid.util.FluidProvider;
import earth.terrarium.common_storage_lib.item.ItemApi;
import earth.terrarium.common_storage_lib.item.impl.SimpleItemStorage;
import earth.terrarium.common_storage_lib.item.util.ItemProvider;
import earth.terrarium.common_storage_lib.resources.fluid.FluidResource;
import earth.terrarium.common_storage_lib.resources.fluid.util.FluidAmounts;
import earth.terrarium.common_storage_lib.resources.item.ItemResource;
import earth.terrarium.common_storage_lib.storage.base.CommonStorage;
import earth.terrarium.common_storage_lib.storage.base.ValueStorage;
import earth.terrarium.common_storage_lib.storage.util.TransferUtil;
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
