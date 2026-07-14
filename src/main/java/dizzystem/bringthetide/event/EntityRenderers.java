package dizzystem.bringthetide.event;

import dizzystem.bringthetide.BringTheTide;
import dizzystem.bringthetide.client.render.CoreRenderer;
import dizzystem.bringthetide.client.render.FluidCoreRenderer;
import dizzystem.bringthetide.client.render.ItemCoreRenderer;
import dizzystem.bringthetide.client.render.TankRenderer;
import dizzystem.bringthetide.registration.TideBlocks;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = BringTheTide.MODID, value = Dist.CLIENT)
public class EntityRenderers {
    @SubscribeEvent
    public static void initClient(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(TideBlocks.TANK_ENTITY.get(), TankRenderer::new);

        event.registerBlockEntityRenderer(TideBlocks.BASIN_CORE_ENTITY.get(), ItemCoreRenderer::new);
        event.registerBlockEntityRenderer(TideBlocks.CURRENT_CORE_ENTITY.get(), CoreRenderer::new);
        event.registerBlockEntityRenderer(TideBlocks.EROSION_CORE_ENTITY.get(), FluidCoreRenderer::new);
        event.registerBlockEntityRenderer(TideBlocks.REEF_CORE_ENTITY.get(), ItemCoreRenderer::new);
        event.registerBlockEntityRenderer(TideBlocks.PELAGIC_CORE_ENTITY.get(), CoreRenderer::new);
        event.registerBlockEntityRenderer(TideBlocks.DEPOSITION_CORE_ENTITY.get(), ItemCoreRenderer::new);
        event.registerBlockEntityRenderer(TideBlocks.SINKHOLE_CORE_ENTITY.get(), CoreRenderer::new);
        event.registerBlockEntityRenderer(TideBlocks.VORTEX_CORE_ENTITY.get(), CoreRenderer::new);
        event.registerBlockEntityRenderer(TideBlocks.WHIRLPOOL_CORE_ENTITY.get(), ItemCoreRenderer::new);
        event.registerBlockEntityRenderer(TideBlocks.TRAWL_CORE_ENTITY.get(), CoreRenderer::new);
        event.registerBlockEntityRenderer(TideBlocks.FLOW_UPGRADE_ENTITY.get(), CoreRenderer::new);
        event.registerBlockEntityRenderer(TideBlocks.BOOTY_UPGRADE_ENTITY.get(), CoreRenderer::new);
        event.registerBlockEntityRenderer(TideBlocks.RANGE_UPGRADE_ENTITY.get(), CoreRenderer::new);
        event.registerBlockEntityRenderer(TideBlocks.ITEM_FILTER_ENTITY.get(), CoreRenderer::new);
        event.registerBlockEntityRenderer(TideBlocks.FLUID_FILTER_ENTITY.get(), CoreRenderer::new);
        event.registerBlockEntityRenderer(TideBlocks.ENTITY_FILTER_ENTITY.get(), CoreRenderer::new);
    }
}
