package dizzystem.bringthetide;

import com.mojang.logging.LogUtils;
import dizzystem.bringthetide.client.particle.SplashParticleProvider;
import dizzystem.bringthetide.client.particle.WhirlpoolParticleProvider;
import dizzystem.bringthetide.registration.TideParticles;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = BringTheTide.MODID)
public class ModBusEventHandler {
    private static final Logger LOGGER = LogUtils.getLogger();

    @SubscribeEvent
    public static void registerParticleProviders(RegisterParticleProvidersEvent event){
        LOGGER.info("Particles registered!");
        event.registerSpriteSet(TideParticles.WHIRLPOOL.get(), WhirlpoolParticleProvider::new);
        event.registerSpriteSet(TideParticles.SPLASH.get(), SplashParticleProvider::new);
    }
}
