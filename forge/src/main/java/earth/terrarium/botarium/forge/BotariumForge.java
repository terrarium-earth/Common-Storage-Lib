package earth.terrarium.botarium.forge;

import earth.terrarium.botarium.Botarium;
import earth.terrarium.botarium.common.energy.EnergyApi;
import earth.terrarium.botarium.common.energy.base.BotariumEnergyBlock;
import earth.terrarium.botarium.common.energy.base.BotariumEnergyItem;
import earth.terrarium.botarium.common.fluid.FluidApi;
import earth.terrarium.botarium.common.fluid.base.BotariumFluidBlock;
import earth.terrarium.botarium.common.fluid.base.BotariumFluidItem;
import earth.terrarium.botarium.common.item.ItemContainerBlock;
import earth.terrarium.botarium.forge.item.ItemContainerWrapper;
import earth.terrarium.botarium.forge.energy.ForgeEnergyContainer;
import earth.terrarium.botarium.forge.energy.ForgeItemEnergyContainer;
import earth.terrarium.botarium.forge.fluid.ForgeFluidContainer;
import earth.terrarium.botarium.forge.fluid.ForgeItemFluidContainer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;

@Mod(Botarium.MOD_ID)
@SuppressWarnings("unused")
public class BotariumForge {

    public BotariumForge() {
        Botarium.init();
        IEventBus bus = MinecraftForge.EVENT_BUS;
        bus.addGenericListener(BlockEntity.class, BotariumForge::attachBlockCapabilities);
        bus.addGenericListener(ItemStack.class, BotariumForge::attachItemCapabilities);
    }

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
            event.addCapability(new ResourceLocation(Botarium.MOD_ID, "item"), new ItemContainerWrapper(itemContainerBlock.getContainer()));
        }
    }

    public static void attachItemCapabilities(AttachCapabilitiesEvent<ItemStack> event) {
        if (event.getObject().getItem() instanceof BotariumEnergyItem<?> energyItem) {
            event.addCapability(new ResourceLocation(Botarium.MOD_ID, "energy"), new ForgeItemEnergyContainer<>(energyItem.getEnergyStorage(event.getObject())));
        }

        BotariumEnergyItem<?> itemEnergyGetter = EnergyApi.getEnergyItem(event.getObject().getItem());
        if (itemEnergyGetter != null) {
            var energyContainer = itemEnergyGetter.getEnergyStorage(event.getObject());
            if (energyContainer != null) {
                event.addCapability(new ResourceLocation(Botarium.MOD_ID, "energy"), new ForgeItemEnergyContainer<>(energyContainer));
            }
        }

        if (event.getObject().getItem() instanceof BotariumFluidItem<?> fluidHoldingItem) {
            event.addCapability(new ResourceLocation(Botarium.MOD_ID, "fluid"), new ForgeItemFluidContainer<>(fluidHoldingItem.getFluidContainer(event.getObject())));
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