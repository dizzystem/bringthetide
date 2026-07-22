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
        TideEntities.init(modEventBus);
    }

    public static void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
            event.accept(TideItems.DRIFTWOOD_LOG_ITEM);
            event.accept(TideItems.SEABOUND_SKIN);
            event.accept(TideItems.SEASHELL_ALLOY_INGOT);
            event.accept(TideItems.TURTLE_ALLOY_INGOT);
            event.accept(TideItems.FISH_ALLOY_INGOT);

            event.accept(TideItems.BASIN_CORE_ITEM);
            event.accept(TideItems.CURRENT_CORE_ITEM);
            event.accept(TideItems.DEPOSITION_CORE_ITEM);
            event.accept(TideItems.EROSION_CORE_ITEM);
            event.accept(TideItems.PELAGIC_CORE_ITEM);
            event.accept(TideItems.REEF_CORE_ITEM);
            event.accept(TideItems.SINKHOLE_CORE_ITEM);
            event.accept(TideItems.TRAWL_CORE_ITEM);
            event.accept(TideItems.VORTEX_CORE_ITEM);
            event.accept(TideItems.WHIRLPOOL_CORE_ITEM);
            event.accept(TideItems.FLOW_UPGRADE_ITEM);
            event.accept(TideItems.BOUNTY_UPGRADE_ITEM);
            event.accept(TideItems.RANGE_UPGRADE_ITEM);
            event.accept(TideItems.ITEM_FILTER_ITEM);
            event.accept(TideItems.FLUID_FILTER_ITEM);
            event.accept(TideItems.ENTITY_FILTER_ITEM);
        }
        if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            event.accept(TideItems.DRIFTWOOD_WAND);
            event.accept(TideItems.CREATIVE_WAND);
            event.accept(TideItems.COSTUME_HELMET);
            event.accept(TideItems.COSTUME_CHESTPLATE);
            event.accept(TideItems.COSTUME_LEGGINGS);
            event.accept(TideItems.COSTUME_BOOTS);
        }
    }

    public static void addFluidsClient(FMLClientSetupEvent event) {
        ItemBlockRenderTypes.setRenderLayer(TideFluids.IMBUED_SEAWATER.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(TideFluids.FLOWING_IMBUED_SEAWATER.get(), RenderType.translucent());
    }
}
