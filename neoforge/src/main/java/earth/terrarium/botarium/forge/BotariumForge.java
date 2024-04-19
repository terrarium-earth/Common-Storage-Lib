package earth.terrarium.botarium.forge;

import earth.terrarium.botarium.Botarium;
import earth.terrarium.botarium.common.energy.EnergyApi;
import earth.terrarium.botarium.common.fluid.FluidApi;
import earth.terrarium.botarium.common.item.ItemApi;
import earth.terrarium.botarium.common.lookup.CapabilityRegisterer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

import java.util.ArrayList;
import java.util.List;

@Mod(Botarium.MOD_ID)
public class BotariumForge {
    public static final List<CapabilityRegisterer> CAPS = new ArrayList<>();

    public BotariumForge(IEventBus bus) {
        Botarium.init();

        bus.addListener((RegisterCapabilitiesEvent event) -> {
            CapabilityRegisterer.registerAll(
                    event,
                    ItemApi.ENTITY_AUTOMATION,
                    ItemApi.ENTITY,
                    ItemApi.BLOCK,
                    ItemApi.ITEM,
                    EnergyApi.BLOCK,
                    EnergyApi.ENTITY,
                    EnergyApi.ITEM,
                    FluidApi.BLOCK,
                    FluidApi.ENTITY,
                    FluidApi.ITEM
            );

            CAPS.forEach(cap -> cap.register(event));
        });
    }
}
