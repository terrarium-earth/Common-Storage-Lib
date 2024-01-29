package earth.terrarium.botarium.forge;

import earth.terrarium.botarium.Botarium;
import earth.terrarium.botarium.client.FluidHolderParticle;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = "botarium", bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class BotariumForgeClient {

    @SubscribeEvent
    public static void registerParticleFactories(RegisterParticleProvidersEvent event) {
        event.registerSprite(Botarium.FLUID_PARTICLE.get(), (type, level, x, y, z, xSpeed, ySpeed, zSpeed) -> new FluidHolderParticle(level, type.fluid(), x, y, z, xSpeed, ySpeed, zSpeed));
    }
}
