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
    }

    public static void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
            event.accept(TideItems.BUDDING_PRISMARINE_ITEM);
            event.accept(TideItems.IMBUED_SEAWATER_BUCKET);
            event.accept(TideItems.DRIFTWOOD_LOG_ITEM);
        }
        if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            event.accept(TideItems.WAND);
        }
    }

    public static void addFluidsClient(FMLClientSetupEvent event) {
        ItemBlockRenderTypes.setRenderLayer(TideFluids.IMBUED_SEAWATER.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(TideFluids.FLOWING_IMBUED_SEAWATER.get(), RenderType.translucent());
    }
}
