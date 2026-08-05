package dizzystem.bringthetide.event;

import dizzystem.bringthetide.BringTheTide;
import dizzystem.bringthetide.util.PoolHandler;
import dizzystem.bringthetide.util.PrimedTntHandler;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = BringTheTide.MODID)
public class LevelTick {
    @SubscribeEvent
    public static void onLevelTick(TickEvent.LevelTickEvent event){
        PrimedTntHandler.levelTick(event.level);
    }

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event){
        PoolHandler.serverTick();
    }
}
