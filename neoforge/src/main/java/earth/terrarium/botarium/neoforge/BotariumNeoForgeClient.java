package earth.terrarium.botarium.neoforge;

import earth.terrarium.botarium.Botarium;
import earth.terrarium.botarium.client.util.FluidHolderParticle;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = Botarium.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class BotariumNeoForgeClient {

    @SubscribeEvent
    public static void registerParticleFactories(RegisterParticleProvidersEvent event) {
        event.registerSpecial(Botarium.FLUID_PARTICLE.get(), (type, level, x, y, z, xSpeed, ySpeed, zSpeed) -> new FluidHolderParticle(level, type.getFluid(), x, y, z, xSpeed, ySpeed, zSpeed));
    }
}
