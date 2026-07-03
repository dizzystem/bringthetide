package dizzystem.bringthetide.datagen;

import dizzystem.bringthetide.BringTheTide;
import dizzystem.bringthetide.registration.TideBlocks;
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
        add(TideBlocks.TURBULENCE_CORE.get(), "Turbulence Core");
        add(TideBlocks.VORTEX_CORE.get(), "Vortex Core");
        add(TideItems.IMBUED_SEAWATER_BUCKET.get(), "Imbued Seawater Bucket");
        add(TideItems.WAND.get(), "Driftwood Wand");
        add(TideItems.SEASHELL_ALLOY_INGOT.get(), "Seashell Alloy Ingot");
        add(TideItems.MASCOT_HELMET.get(), "Dolphin Mascot Head");
    }
}
