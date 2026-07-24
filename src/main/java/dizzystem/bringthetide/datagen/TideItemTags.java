package dizzystem.bringthetide.datagen;

import dizzystem.bringthetide.BringTheTide;
import dizzystem.bringthetide.api.TideTags;
import dizzystem.bringthetide.registration.TideBlocks;
import dizzystem.bringthetide.registration.TideItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
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
        tag(ItemTags.PLANKS)
                .add(TideItems.DRIFTWOOD_PLANKS_ITEM.get());
        tag(ItemTags.STAIRS)
                .add(TideItems.DRIFTWOOD_STAIRS_ITEM.get());
        tag(ItemTags.WOODEN_STAIRS)
                .add(TideItems.DRIFTWOOD_STAIRS_ITEM.get());
        tag(ItemTags.SLABS)
                .add(TideItems.DRIFTWOOD_SLAB_ITEM.get());
        tag(ItemTags.WOODEN_SLABS)
                .add(TideItems.DRIFTWOOD_SLAB_ITEM.get());
        tag(ItemTags.FENCES)
                .add(TideItems.DRIFTWOOD_FENCE_ITEM.get());
        tag(ItemTags.WOODEN_FENCES)
                .add(TideItems.DRIFTWOOD_FENCE_ITEM.get());
        tag(ItemTags.FENCE_GATES)
                .add(TideItems.DRIFTWOOD_FENCE_GATE_ITEM.get());
//        tag(ItemTags.SIGNS)
//                .add(TideItems.DRIFTWOOD_SIGN_ITEM.get());
        tag(ItemTags.WOODEN_PRESSURE_PLATES)
                .add(TideItems.DRIFTWOOD_PRESSURE_PLATE_ITEM.get());
        tag(ItemTags.BUTTONS)
                .add(TideItems.DRIFTWOOD_BUTTON_ITEM.get());
        tag(ItemTags.WOODEN_BUTTONS)
                .add(TideItems.DRIFTWOOD_BUTTON_ITEM.get());
        tag(TideTags.NON_DRIFTWOOD_LOGS)
                .addTag(ItemTags.LOGS)
                .remove(TideItems.DRIFTWOOD_LOG_ITEM.get());
    }
}
