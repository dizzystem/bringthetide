package dizzystem.bringthetide.registration;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.*;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class Registration {
    public static void init(IEventBus modEventBus) {
        TideBlocks.init(modEventBus);
        TideItems.init(modEventBus);
        TideFluids.init(modEventBus);
        TideParticles.init(modEventBus);
        TideLoot.init(modEventBus);
        TideRecipes.init(modEventBus);
    }

    public static void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
            event.accept(TideItems.BUDDING_PRISMARINE_ITEM);
            event.accept(TideItems.DRIFTWOOD_LOG_ITEM);
            event.accept(TideItems.BASIN_CORE_ITEM);
            event.accept(TideItems.CURRENT_CORE_ITEM);
            event.accept(TideItems.DEPOSITION_CORE_ITEM);
            event.accept(TideItems.EROSION_CORE_ITEM);
            event.accept(TideItems.SINKHOLE_CORE_ITEM);
            event.accept(TideItems.TRAWL_CORE_ITEM);
            event.accept(TideItems.VORTEX_CORE_ITEM);
        }
        if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            event.accept(TideItems.DRIFTWOOD_WAND);
            event.accept(TideItems.CREATIVE_WAND);
            event.accept(TideItems.MASCOT_HELMET);
        }
    }

    public static void addFluidsClient(FMLClientSetupEvent event) {
        ItemBlockRenderTypes.setRenderLayer(TideFluids.IMBUED_SEAWATER.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(TideFluids.FLOWING_IMBUED_SEAWATER.get(), RenderType.translucent());
    }
}
