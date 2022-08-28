package earth.terrarium.botarium.forge;

import earth.terrarium.botarium.Botarium;
import earth.terrarium.botarium.api.EnergyBlock;
import earth.terrarium.botarium.api.EnergyItem;
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
        bus.addGenericListener(BlockEntity.class, BotariumForge::attachBlockEnergy);
        bus.addGenericListener(ItemStack.class, BotariumForge::attachItemEnergy);
    }

    public static void attachBlockEnergy(AttachCapabilitiesEvent<BlockEntity> event) {
        if(event.getObject() instanceof EnergyBlock energyBlock) {
            event.addCapability(new ResourceLocation(Botarium.MOD_ID, "energy"), (ICapabilityProvider) energyBlock.getEnergyStorage());
        }
    }

    public static void attachItemEnergy(AttachCapabilitiesEvent<ItemStack> event) {
        ItemStack item = event.getObject();
        if(item.getItem() instanceof EnergyItem energyItem) {
            event.addCapability(new ResourceLocation(Botarium.MOD_ID, "energy"), (ICapabilityProvider) energyItem.getEnergyStorage(item));
        }
    }
}