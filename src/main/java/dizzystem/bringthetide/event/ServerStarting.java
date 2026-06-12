package dizzystem.bringthetide.event;

import com.mojang.logging.LogUtils;
import dizzystem.bringthetide.BringTheTide;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;

@Mod.EventBusSubscriber(modid = BringTheTide.MODID)
public class ServerStarting {
    private static final Logger LOGGER = LogUtils.getLogger();

    @SubscribeEvent
    public static void onServerStarting(ServerStartingEvent event){
        LOGGER.info("HELLO from server starting");
        LOGGER.info("HELLO from server starting");
        LOGGER.info("HELLO from server starting");
        LOGGER.info("HELLO from server starting");
    }
}

