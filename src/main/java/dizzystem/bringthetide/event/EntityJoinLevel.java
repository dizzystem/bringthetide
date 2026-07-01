package dizzystem.bringthetide.event;

import dizzystem.bringthetide.BringTheTide;
import dizzystem.bringthetide.util.DroppedItemCollector;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = BringTheTide.MODID)
public class EntityJoinLevel {
    @SubscribeEvent
    public static void onEntityJoinLevel(EntityJoinLevelEvent event){
        if (event.getEntity() instanceof ItemEntity itemEntity){
            DroppedItemCollector.itemDropped(event.getLevel(), itemEntity);
        }
    }
}
