package dizzystem.bringthetide;

import com.mojang.logging.LogUtils;
import dizzystem.bringthetide.client.particle.WhirlpoolParticleProvider;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;

@Mod.EventBusSubscriber(modid = BringTheTide.MODID)
public class EventHandler {
    private static final Logger LOGGER = LogUtils.getLogger();

    @SubscribeEvent
    public static void onServerStarting(ServerStartingEvent event){
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
        LOGGER.info("HELLO from server starting");
        LOGGER.info("HELLO from server starting");
        LOGGER.info("HELLO from server starting");
    }
}

