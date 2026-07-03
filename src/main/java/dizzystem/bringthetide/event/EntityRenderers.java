package dizzystem.bringthetide.event;

import dizzystem.bringthetide.BringTheTide;
import dizzystem.bringthetide.client.render.CoreRenderer;
import dizzystem.bringthetide.client.render.ItemCoreRenderer;
import dizzystem.bringthetide.registration.TideBlocks;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = BringTheTide.MODID, value = Dist.CLIENT)
public class EntityRenderers {
    @SubscribeEvent
    public static void initClient(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(TideBlocks.EROSION_CORE_ENTITY.get(), CoreRenderer::new);
        event.registerBlockEntityRenderer(TideBlocks.DEPOSITION_CORE_ENTITY.get(), ItemCoreRenderer::new);
        event.registerBlockEntityRenderer(TideBlocks.SINKHOLE_CORE_ENTITY.get(), CoreRenderer::new);
        event.registerBlockEntityRenderer(TideBlocks.CURRENT_CORE_ENTITY.get(), CoreRenderer::new);
        event.registerBlockEntityRenderer(TideBlocks.VORTEX_CORE_ENTITY.get(), CoreRenderer::new);
        event.registerBlockEntityRenderer(TideBlocks.TRAWL_CORE_ENTITY.get(), CoreRenderer::new);
        event.registerBlockEntityRenderer(TideBlocks.TURBULENCE_CORE_ENTITY.get(), ItemCoreRenderer::new);
    }
}
