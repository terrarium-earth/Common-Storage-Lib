package earth.terrarium.botarium.impl;

import earth.terrarium.botarium.Botarium;
import earth.terrarium.botarium.common.energy.EnergyApi;
import earth.terrarium.botarium.common.energy.base.BotariumEnergyBlock;
import earth.terrarium.botarium.common.energy.base.BotariumEnergyItem;
import earth.terrarium.botarium.common.fluid.FluidApi;
import earth.terrarium.botarium.common.fluid.base.BotariumFluidBlock;
import earth.terrarium.botarium.common.fluid.base.BotariumFluidItem;
import earth.terrarium.botarium.common.item.ItemContainerBlock;
import earth.terrarium.botarium.common.item.SerializableContainer;
import earth.terrarium.botarium.impl.energy.ForgeEnergyContainer;
import earth.terrarium.botarium.impl.energy.ForgeItemEnergyContainer;
import earth.terrarium.botarium.impl.fluid.ForgeFluidContainer;
import earth.terrarium.botarium.impl.fluid.ForgeItemFluidContainer;
import earth.terrarium.botarium.impl.item.ItemContainerWrapper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.event.AttachCapabilitiesEvent;

public class NewApiForge {
    public static void attachBlockCapabilities(AttachCapabilitiesEvent<BlockEntity> event) {
        if (event.getObject() instanceof BotariumEnergyBlock<?> energyBlock) {
            event.addCapability(new ResourceLocation(Botarium.MOD_ID, "energy"), new ForgeEnergyContainer(energyBlock, event.getObject()));
        }

        BotariumEnergyBlock<?> blockEnergyGetter = EnergyApi.getEnergyBlock(event.getObject().getType());
        if (blockEnergyGetter != null) {
            event.addCapability(new ResourceLocation(Botarium.MOD_ID, "energy"), new ForgeEnergyContainer(blockEnergyGetter, event.getObject()));
        }

        if (event.getObject() instanceof BotariumFluidBlock<?> fluidHoldingBlock) {
            event.addCapability(new ResourceLocation(Botarium.MOD_ID, "fluid"), new ForgeFluidContainer(fluidHoldingBlock, event.getObject()));
        }

        BotariumFluidBlock<?> blockFluidGetter = FluidApi.getFluidBlock(event.getObject().getType());
        if (blockFluidGetter != null) {
            event.addCapability(new ResourceLocation(Botarium.MOD_ID, "fluid"), new ForgeFluidContainer(blockFluidGetter, event.getObject()));
        }

        if (event.getObject() instanceof ItemContainerBlock itemContainerBlock) {
            SerializableContainer container = itemContainerBlock.getContainer();
            if (container != null) {
                event.addCapability(new ResourceLocation(Botarium.MOD_ID, "item"), new ItemContainerWrapper(container));
            }
        }
    }

    public static void attachItemCapabilities(AttachCapabilitiesEvent<ItemStack> event) {
        if (event.getObject().getItem() instanceof BotariumEnergyItem<?> energyItem) {
            var energyStorage = energyItem.getEnergyStorage(event.getObject());
            if (energyStorage != null) {
                event.addCapability(new ResourceLocation(Botarium.MOD_ID, "energy"), new ForgeItemEnergyContainer<>(energyStorage));
            }
        }

        BotariumEnergyItem<?> itemEnergyGetter = EnergyApi.getEnergyItem(event.getObject().getItem());
        if (itemEnergyGetter != null) {
            var energyContainer = itemEnergyGetter.getEnergyStorage(event.getObject());
            if (energyContainer != null) {
                event.addCapability(new ResourceLocation(Botarium.MOD_ID, "energy"), new ForgeItemEnergyContainer<>(energyContainer));
            }
        }

        if (event.getObject().getItem() instanceof BotariumFluidItem<?> fluidHoldingItem) {
            var fluidContainer = fluidHoldingItem.getFluidContainer(event.getObject());
            if (fluidContainer != null) {
                event.addCapability(new ResourceLocation(Botarium.MOD_ID, "fluid"), new ForgeItemFluidContainer<>(fluidContainer));
            }
        }

        var itemFluidGetter = FluidApi.getFluidItem(event.getObject().getItem());
        if (itemFluidGetter != null) {
            var fluidContainer = itemFluidGetter.getFluidContainer(event.getObject());
            if (fluidContainer != null) {
                event.addCapability(new ResourceLocation(Botarium.MOD_ID, "fluid"), new ForgeItemFluidContainer<>(fluidContainer));
            }
        }
    }
}