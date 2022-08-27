package earth.terrarium.botarium.forge;

import earth.terrarium.botarium.Botarium;
import earth.terrarium.botarium.api.EnergyHoldable;
import earth.terrarium.botarium.api.EnergyMarker;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.Mod;

@Mod(Botarium.MOD_ID)
public class BotariumForge {
    public BotariumForge() {
        Botarium.init();
    }

    public static void attachBlockEnergy(AttachCapabilitiesEvent<BlockEntity> event) {
        if(event.getObject() instanceof EnergyMarker marker) {
            event.addCapability(new ResourceLocation(Botarium.MOD_ID, "energy"), (ICapabilityProvider) marker.getEnergyStorage(marker));
        }
    }

    public static void attachItemEnergy(AttachCapabilitiesEvent<ItemStack> event) {
        if(event.getObject().getItem() instanceof EnergyMarker marker) {
            event.addCapability(new ResourceLocation(Botarium.MOD_ID, "energy"), (ICapabilityProvider) marker.getEnergyStorage(marker));
        }
    }
}