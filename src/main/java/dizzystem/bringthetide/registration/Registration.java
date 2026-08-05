package dizzystem.bringthetide.registration;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.*;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class Registration {
    public static void init(IEventBus modEventBus){
        TideBlocks.init(modEventBus);
        TideItems.init(modEventBus);
        TideFluids.init(modEventBus);
        TideParticles.init(modEventBus);
        TideLoot.init(modEventBus);
        TideRecipes.init(modEventBus);
        TideEntities.init(modEventBus);
    }

    public static void addCreative(BuildCreativeModeTabContentsEvent event){
        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS){
            event.accept(TideItems.TANK);
            event.accept(TideItems.EXPLOSION_ROD);
            event.accept(TideItems.EFFIGY);

            event.accept(TideItems.DRIFTWOOD_LOG);
            event.accept(TideItems.DRIFTWOOD_PLANKS);
            event.accept(TideItems.DRIFTWOOD_STAIRS);
            event.accept(TideItems.DRIFTWOOD_SLAB);
            event.accept(TideItems.DRIFTWOOD_FENCE);
            event.accept(TideItems.DRIFTWOOD_FENCE_GATE);
            event.accept(TideItems.DRIFTWOOD_SIGN);
            event.accept(TideItems.DRIFTWOOD_PRESSURE_PLATE);
            event.accept(TideItems.DRIFTWOOD_BUTTON);
            event.accept(TideItems.DRIFTWOOD_COLUMN);
            event.accept(TideItems.DRIFTWOOD_DOOR);
            event.accept(TideItems.DRIFTWOOD_TRAPDOOR);

            event.accept(TideItems.PRISMARINE_PILLAR);

            event.accept(TideItems.BASIN_CORE);
            event.accept(TideItems.CURRENT_CORE);
            event.accept(TideItems.DEPOSITION_CORE);
            event.accept(TideItems.EROSION_CORE);
            event.accept(TideItems.PELAGIC_CORE);
            event.accept(TideItems.REEF_CORE);
            event.accept(TideItems.SEABED_CORE);
            event.accept(TideItems.SINKHOLE_CORE);
            event.accept(TideItems.TRAWL_CORE);
            event.accept(TideItems.VORTEX_CORE);
            event.accept(TideItems.WHIRLPOOL_CORE);
            event.accept(TideItems.FLOW_UPGRADE_);
            event.accept(TideItems.BOUNTY_UPGRADE);
            event.accept(TideItems.RANGE_UPGRADE);
            event.accept(TideItems.ITEM_FILTER);
            event.accept(TideItems.FLUID_FILTER);
            event.accept(TideItems.ENTITY_FILTER);
        }
        if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES){
            event.accept(TideItems.DRIFTWOOD_WAND);
            event.accept(TideItems.CREATIVE_WAND);
            event.accept(TideItems.COSTUME_HELMET);
            event.accept(TideItems.COSTUME_CHESTPLATE);
            event.accept(TideItems.COSTUME_LEGGINGS);
            event.accept(TideItems.COSTUME_BOOTS);
        }
        if (event.getTabKey() == CreativeModeTabs.INGREDIENTS){
            event.accept(TideItems.SEABOUND_SKIN);
            event.accept(TideItems.SEASHELL_BRASS_INGOT);
            event.accept(TideItems.TURTLE_STEEL_INGOT);
            event.accept(TideItems.PUFFERGOLD_INGOT);
        }
    }

}
