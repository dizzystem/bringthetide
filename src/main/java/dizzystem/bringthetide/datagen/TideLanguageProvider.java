package dizzystem.bringthetide.datagen;

import dizzystem.bringthetide.BringTheTide;
import dizzystem.bringthetide.registration.TideBlocks;
import dizzystem.bringthetide.registration.TideFluids;
import dizzystem.bringthetide.registration.TideItems;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.registries.RegistryObject;

public class TideLanguageProvider extends LanguageProvider {
    public TideLanguageProvider(PackOutput output, String locale){
        super (output, BringTheTide.MODID, locale);
    }

    @Override
    protected void addTranslations(){
        add(TideBlocks.BUDDING_PRISMARINE.get(), "Budding Prismarine");
        add(TideBlocks.SMALL_PRISMARINE_BUD.get(), "Small Prismarine Bud");
        add(TideBlocks.MEDIUM_PRISMARINE_BUD.get(), "Medium Prismarine Bud");
        add(TideBlocks.LARGE_PRISMARINE_BUD.get(), "Large Prismarine Bud");
        add(TideBlocks.PRISMARINE_CLUSTER.get(), "Prismarine Cluster");

        add(TideBlocks.DRIFTWOOD_LOG.get(), "Driftwood Log");
        add(TideBlocks.DRIFTWOOD_PLANKS.get(), "Driftwood Planks");
        add(TideBlocks.DRIFTWOOD_STAIRS.get(), "Driftwood Stairs");
        add(TideBlocks.DRIFTWOOD_SLAB.get(), "Driftwood Slab");
        add(TideBlocks.DRIFTWOOD_FENCE.get(), "Driftwood Fence");
        add(TideBlocks.DRIFTWOOD_FENCE_GATE.get(), "Driftwood Fence Gate");
//        add(TideBlocks.DRIFTWOOD_SIGN.get(), "Driftwood Sign");
//        add(TideBlocks.DRIFTWOOD_WALL_SIGN.get(), "Driftwood Sign");
        add(TideBlocks.DRIFTWOOD_PRESSURE_PLATE.get(), "Driftwood Pressure Plate");
        add(TideBlocks.DRIFTWOOD_BUTTON.get(), "Driftwood Button");

        add(TideBlocks.TANK.get(), "Prismarine Tank");

        add(TideBlocks.BASIN_CORE.get(), "Basin Core");
        add(TideBlocks.CURRENT_CORE.get(), "Current Core");
        add(TideBlocks.DEPOSITION_CORE.get(), "Deposition Core");
        add(TideBlocks.EROSION_CORE.get(), "Erosion Core");
        add(TideBlocks.PELAGIC_CORE.get(), "Pelagic Core");
        add(TideBlocks.REEF_CORE.get(), "Reef Core");
        add(TideBlocks.SINKHOLE_CORE.get(), "Sinkhole Core");
        add(TideBlocks.TRAWL_CORE.get(), "Trawl Core");
        add(TideBlocks.VORTEX_CORE.get(), "Vortex Core");
        add(TideBlocks.WHIRLPOOL_CORE.get(), "Whirlpool Core");
        add(TideBlocks.FLOW_UPGRADE.get(), "Flow Upgrade");
        add(TideBlocks.BOUNTY_UPGRADE.get(), "Bounty Upgrade");
        add(TideBlocks.RANGE_UPGRADE.get(), "Range Upgrade");
        add(TideBlocks.ITEM_FILTER.get(), "Item Filter Upgrade");
        add(TideBlocks.FLUID_FILTER.get(), "Fluid Filter Upgrade");
        add(TideBlocks.ENTITY_FILTER.get(), "Entity Filter Upgrade");
        add(TideBlocks.BLOCK_IMBUED_SEAWATER.get(), "Imbued Seawater");

        add(TideFluids.TYPE_IMBUED_SEAWATER.getId().getPath(), "Imbued Seawater");

        //add(TideItems.IMBUED_SEAWATER_BUCKET.get(), "Imbued Seawater Bucket");
        add(TideItems.DRIFTWOOD_WAND.get(), "Driftwood Wand");
        add(TideItems.CREATIVE_WAND.get(), "Creative Wand");
        add(TideItems.SEASHELL_ALLOY_INGOT.get(), "Seashell Brass Ingot");
        add(TideItems.TURTLE_ALLOY_INGOT.get(), "Turtle Steel Ingot");
        add(TideItems.FISH_ALLOY_INGOT.get(), "Puffergold Ingot");
        add(TideItems.SEABOUND_SKIN.get(), "Seabound Skin");
        add(TideItems.COSTUME_HELMET.get(), "Dolphin Costume Head");
        add(TideItems.COSTUME_CHESTPLATE.get(), "Dolphin Costume Body");
        add(TideItems.COSTUME_LEGGINGS.get(), "Dolphin Costume Legs");
        add(TideItems.COSTUME_BOOTS.get(), "Dolphin Costume Shoes");
    }
}
