package dizzystem.bringthetide.datagen;

import dizzystem.bringthetide.BringTheTide;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class TideItemTags extends ItemTagsProvider {

    public TideItemTags(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider, BlockTagsProvider blockTags, ExistingFileHelper helper){
        super(packOutput, lookupProvider, blockTags.contentsGetter(), BringTheTide.MODID, helper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider){
    }
}
