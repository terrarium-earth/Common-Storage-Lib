package earth.terrarium.botarium.forge;

import earth.terrarium.botarium.Botarium;
import earth.terrarium.botarium.api.energy.EnergyBlock;
import earth.terrarium.botarium.api.energy.EnergyItem;
import earth.terrarium.botarium.api.fluid.FluidHoldable;
import earth.terrarium.botarium.api.item.ItemContainerBlock;
import earth.terrarium.botarium.forge.fluid.ForgeBlockFluidContainer;
import earth.terrarium.botarium.forge.item.ItemContainerWrapper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
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
        if (event.getObject() instanceof EnergyBlock energyBlock) {
            event.addCapability(new ResourceLocation(Botarium.MOD_ID, "energy"), (ICapabilityProvider) energyBlock.getEnergyStorage());
        }

        if (event.getObject() instanceof FluidHoldable fluidHoldable) {
            event.addCapability(new ResourceLocation(Botarium.MOD_ID, "fluid"), new ForgeBlockFluidContainer(fluidHoldable.getFluidContainer()));
        }

        if (event.getObject() instanceof ItemContainerBlock itemContainerBlock) {
            event.addCapability(new ResourceLocation(Botarium.MOD_ID, "item"), new ItemContainerWrapper(itemContainerBlock.getContainer()));
        }
    }

    public static void attachItemCapabilities(AttachCapabilitiesEvent<ItemStack> event) {
        ItemStack item = event.getObject();
        if (item.getItem() instanceof EnergyItem energyItem) {
            event.addCapability(new ResourceLocation(Botarium.MOD_ID, "energy"), (ICapabilityProvider) energyItem.getEnergyStorage(item));
        }
    }
}