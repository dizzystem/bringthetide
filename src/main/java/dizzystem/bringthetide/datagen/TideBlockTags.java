package dizzystem.bringthetide.datagen;

import dizzystem.bringthetide.BringTheTide;
import dizzystem.bringthetide.api.TideTags;
import dizzystem.bringthetide.registration.TideBlocks;
import dizzystem.bringthetide.registration.TideFluids;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class TideBlockTags extends BlockTagsProvider {
    public TideBlockTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper){
        super(output, lookupProvider, BringTheTide.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider){
        tag(TideTags.GROWTH_ACCELERATABLE)
                .add(Blocks.BUDDING_AMETHYST)
                .add(TideBlocks.BUDDING_PRISMARINE.get());
        tag(TideTags.VALID_POOL_BLOCK)
                .add(Blocks.PRISMARINE)
                .add(Blocks.PRISMARINE_STAIRS)
                .add(Blocks.PRISMARINE_SLAB)
                .add(Blocks.PRISMARINE_BRICKS)
                .add(Blocks.PRISMARINE_BRICK_STAIRS)
                .add(Blocks.PRISMARINE_BRICK_SLAB)
                .add(Blocks.SAND)
                .add(Blocks.SANDSTONE)
                .add(Blocks.SANDSTONE_STAIRS)
                .add(Blocks.SANDSTONE_SLAB)
                .add(Blocks.CUT_SANDSTONE)
                .add(Blocks.CUT_SANDSTONE_SLAB)
                .add(TideBlocks.EROSION_CORE.get())
                .add(TideBlocks.DEPOSITION_CORE.get());
    }
}
