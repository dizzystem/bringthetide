package dizzystem.bringthetide.event;

import dizzystem.bringthetide.BringTheTide;
import dizzystem.bringthetide.util.ExplosionHandler;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = BringTheTide.MODID)
public class ExplosionEvent {
    @SubscribeEvent
    public static void onExplosion(net.minecraftforge.event.level.ExplosionEvent.Start event){
        if (ExplosionHandler.onExplosion((ServerLevel) event.getLevel(), event.getExplosion())){
            event.cancel();
        }
    }
}
