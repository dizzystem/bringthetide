package dizzystem.bringthetide.datagen;

import dizzystem.bringthetide.BringTheTide;
import dizzystem.bringthetide.api.TideTags;
import dizzystem.bringthetide.registration.TideItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.concurrent.CompletableFuture;

public class TideItemTags extends ItemTagsProvider {

    public TideItemTags(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider, BlockTagsProvider blockTags, ExistingFileHelper helper){
        super(packOutput, lookupProvider, blockTags.contentsGetter(), BringTheTide.MODID, helper);
    }

    @Override
    @ParametersAreNonnullByDefault
    protected void addTags(HolderLookup.Provider provider){
        tag(ItemTags.LOGS)
                .add(TideItems.DRIFTWOOD_LOG_ITEM.get());
        tag(ItemTags.LOGS_THAT_BURN)
                .add(TideItems.DRIFTWOOD_LOG_ITEM.get());
        tag(TideTags.NON_DRIFTWOOD_LOGS)
                .addTag(ItemTags.LOGS)
                .remove(TideItems.DRIFTWOOD_LOG_ITEM.get());
    }
}
