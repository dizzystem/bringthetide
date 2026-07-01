package dizzystem.bringthetide.event;

import com.mojang.logging.LogUtils;
import dizzystem.bringthetide.BringTheTide;
import dizzystem.bringthetide.client.particle.BubbleParticleProvider;
import dizzystem.bringthetide.client.particle.SparkleParticleProvider;
import dizzystem.bringthetide.client.particle.SplashParticleProvider;
import dizzystem.bringthetide.client.particle.WhirlpoolParticleProvider;
import dizzystem.bringthetide.registration.TideParticles;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = BringTheTide.MODID)
public class RegisterParticleProviders {
    @SubscribeEvent
    public static void registerParticleProviders(RegisterParticleProvidersEvent event){
        event.registerSpriteSet(TideParticles.WHIRLPOOL.get(), WhirlpoolParticleProvider::new);
        event.registerSpriteSet(TideParticles.SPLASH.get(), SplashParticleProvider::new);
        event.registerSpriteSet(TideParticles.BUBBLE.get(), BubbleParticleProvider::new);
        event.registerSpriteSet(TideParticles.SPARKLE.get(), SparkleParticleProvider::new);
    }
}
