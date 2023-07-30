package earth.terrarium.botarium.forge;

import earth.terrarium.botarium.Botarium;
import earth.terrarium.botarium.common.energy.EnergyApi;
import earth.terrarium.botarium.common.energy.base.BotariumEnergyBlock;
import earth.terrarium.botarium.common.energy.base.BotariumEnergyItem;
import earth.terrarium.botarium.common.fluid.FluidApi;
import earth.terrarium.botarium.common.fluid.base.BotariumFluidBlock;
import earth.terrarium.botarium.common.fluid.base.BotariumFluidItem;
import earth.terrarium.botarium.common.item.ItemContainerBlock;
import earth.terrarium.botarium.forge.energy.ForgeEnergyContainer;
import earth.terrarium.botarium.forge.energy.ForgeItemEnergyContainer;
import earth.terrarium.botarium.forge.fluid.ForgeFluidContainer;
import earth.terrarium.botarium.forge.fluid.ForgeItemFluidContainer;
import earth.terrarium.botarium.forge.item.ItemContainerWrapper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;

@Mod(Botarium.MOD_ID)
@SuppressWarnings("unchecked")
public class BotariumForge {
    public BotariumForge() {
        Botarium.init();
        IEventBus bus = MinecraftForge.EVENT_BUS;
        bus.addGenericListener(BlockEntity.class, BotariumForge::attachBlockCapabilities);
        bus.addGenericListener(ItemStack.class, BotariumForge::attachItemCapabilities);
    }

    public static void attachBlockCapabilities(AttachCapabilitiesEvent<BlockEntity> event) {
        if (event.getObject() instanceof BotariumEnergyBlock<?> energyBlock) {
            event.addCapability(new ResourceLocation(Botarium.MOD_ID, "energy"), new ForgeEnergyContainer<>(energyBlock.getEnergyStorage(), event.getObject()));
        }

        var energyContainer = EnergyApi.BLOCK_ENTITY_LOOKUP_MAP.get(event.getObject().getType()).getEnergyContainer(event.getObject().getLevel(), event.getObject().getBlockPos(), event.getObject().getBlockState(), event.getObject(), null);
        if (energyContainer != null) {
            event.addCapability(new ResourceLocation(Botarium.MOD_ID, "energy"), new ForgeEnergyContainer<>(energyContainer, event.getObject()));
        }

        var nonEntityEnergyContainer = EnergyApi.BLOCK_LOOKUP_MAP.get(event.getObject().getBlockState().getBlock()).getEnergyContainer(event.getObject().getLevel(), event.getObject().getBlockPos(), event.getObject().getBlockState(), event.getObject(), null);
        if (nonEntityEnergyContainer != null) {
            event.addCapability(new ResourceLocation(Botarium.MOD_ID, "energy"), new ForgeEnergyContainer<>(nonEntityEnergyContainer, event.getObject()));
        }

        if (event.getObject() instanceof BotariumFluidBlock<?> fluidHoldingBlock) {
            event.addCapability(new ResourceLocation(Botarium.MOD_ID, "fluid"), new ForgeFluidContainer<>(fluidHoldingBlock.getFluidContainer(), event.getObject()));
        }

        var fluidContainer = FluidApi.BLOCK_ENTITY_LOOKUP_MAP.get(event.getObject().getType()).getFluidContainer(event.getObject().getLevel(), event.getObject().getBlockPos(), event.getObject().getBlockState(), event.getObject(), null);
        if (fluidContainer != null) {
            event.addCapability(new ResourceLocation(Botarium.MOD_ID, "fluid"), new ForgeFluidContainer<>(fluidContainer, event.getObject()));
        }

        var nonEntityFluidContainer = FluidApi.BLOCK_LOOKUP_MAP.get(event.getObject().getBlockState().getBlock()).getFluidContainer(event.getObject().getLevel(), event.getObject().getBlockPos(), event.getObject().getBlockState(), event.getObject(), null);
        if (nonEntityFluidContainer != null) {
            event.addCapability(new ResourceLocation(Botarium.MOD_ID, "fluid"), new ForgeFluidContainer<>(nonEntityFluidContainer, event.getObject()));
        }

        if (event.getObject() instanceof ItemContainerBlock itemContainerBlock) {
            event.addCapability(new ResourceLocation(Botarium.MOD_ID, "item"), new ItemContainerWrapper(itemContainerBlock.getContainer()));
        }
    }

    public static void attachItemCapabilities(AttachCapabilitiesEvent<ItemStack> event) {
        if (event.getObject().getItem() instanceof BotariumEnergyItem<?> energyItem) {
            event.addCapability(new ResourceLocation(Botarium.MOD_ID, "energy"), new ForgeItemEnergyContainer<>(energyItem.getEnergyStorage(event.getObject()), event.getObject()));
        }

        var energyContainer = EnergyApi.ITEM_LOOKUP_MAP.get(event.getObject().getItem()).getEnergyContainer(event.getObject());
        if (energyContainer != null) {
            event.addCapability(new ResourceLocation(Botarium.MOD_ID, "energy"), new ForgeItemEnergyContainer<>(energyContainer, event.getObject()));
        }

        if (event.getObject().getItem() instanceof BotariumFluidItem<?> fluidHoldingItem) {
            event.addCapability(new ResourceLocation(Botarium.MOD_ID, "fluid"), new ForgeItemFluidContainer<>(fluidHoldingItem.getFluidContainer(event.getObject()), event.getObject()));
        }

        var fluidContainer = FluidApi.ITEM_LOOKUP_MAP.get(event.getObject().getItem()).getFluidContainer(event.getObject());
        if (fluidContainer != null) {
            event.addCapability(new ResourceLocation(Botarium.MOD_ID, "fluid"), new ForgeItemFluidContainer<>(fluidContainer, event.getObject()));
        }
    }
}