package dizzystem.bringthetide.event;

import com.mojang.logging.LogUtils;
import dizzystem.bringthetide.BringTheTide;
import dizzystem.bringthetide.registration.TideEntities;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = BringTheTide.MODID)
public class EntityAttributeCreation {
    @SubscribeEvent
    public static void onEntityAttributeCreation(EntityAttributeCreationEvent event) {
        event.put(TideEntities.EFFIGY.get(), LivingEntity.createLivingAttributes()
                .add(Attributes.MAX_HEALTH)
                .add(Attributes.ATTACK_SPEED)
                .build());
    }
}
