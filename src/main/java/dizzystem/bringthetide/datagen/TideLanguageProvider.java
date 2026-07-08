package dizzystem.bringthetide.datagen;

import dizzystem.bringthetide.BringTheTide;
import dizzystem.bringthetide.registration.TideBlocks;
import dizzystem.bringthetide.registration.TideFluids;
import dizzystem.bringthetide.registration.TideItems;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.LanguageProvider;

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
        add(TideBlocks.CURRENT_CORE.get(), "Current Core");
        add(TideBlocks.DEPOSITION_CORE.get(), "Deposition Core");
        add(TideBlocks.EROSION_CORE.get(), "Erosion Core");
        add(TideBlocks.SINKHOLE_CORE.get(), "Sinkhole Core");
        add(TideBlocks.TRAWL_CORE.get(), "Trawl Core");
        add(TideBlocks.BASIN_CORE.get(), "Basin Core");
        add(TideBlocks.VORTEX_CORE.get(), "Vortex Core");
        add(TideBlocks.BLOCK_IMBUED_SEAWATER.get(), "Imbued Seawater");

        add(TideFluids.TYPE_IMBUED_SEAWATER.getId().getPath(), "Imbued Seawater");

        //add(TideItems.IMBUED_SEAWATER_BUCKET.get(), "Imbued Seawater Bucket");
        add(TideItems.DRIFTWOOD_WAND.get(), "Driftwood Wand");
        add(TideItems.CREATIVE_WAND.get(), "Creative Wand");
        add(TideItems.SEASHELL_ALLOY_INGOT.get(), "Seashell Alloy Ingot");
        add(TideItems.SEABOUND_SKIN.get(), "Seabound Skin");
        add(TideItems.COSTUME_HELMET.get(), "Dolphin Costume Head");
        add(TideItems.COSTUME_CHESTPLATE.get(), "Dolphin Costume Body");
        add(TideItems.COSTUME_LEGGINGS.get(), "Dolphin Costume Legs");
        add(TideItems.COSTUME_BOOTS.get(), "Dolphin Costume Shoes");
    }
}
