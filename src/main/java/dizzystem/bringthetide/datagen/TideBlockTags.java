package dizzystem.bringthetide.datagen;

import dizzystem.bringthetide.BringTheTide;
import dizzystem.bringthetide.api.TideTags;
import dizzystem.bringthetide.registration.Registration;
import dizzystem.bringthetide.registration.TideBlocks;
import dizzystem.bringthetide.registration.TideItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
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
        tag(TideTags.VALID_POOL_BLOCK)
                .add(Blocks.PRISMARINE)
                .add(Blocks.PRISMARINE_STAIRS)
                .add(Blocks.PRISMARINE_SLAB)
                .add(Blocks.SANDSTONE)
                .add(Blocks.SANDSTONE_STAIRS)
                .add(Blocks.SANDSTONE_SLAB)
                .add(Blocks.CUT_SANDSTONE)
                .add(Blocks.CUT_SANDSTONE_SLAB)
                .add(TideBlocks.BASIN_CORE.get())
                .add(TideBlocks.CURRENT_CORE.get())
                .add(TideBlocks.DEPOSITION_CORE.get())
                .add(TideBlocks.EROSION_CORE.get())
                .add(TideBlocks.PELAGIC_CORE.get())
                .add(TideBlocks.REEF_CORE.get())
                .add(TideBlocks.SEABED_CORE.get())
                .add(TideBlocks.SINKHOLE_CORE.get())
                .add(TideBlocks.TRAWL_CORE.get())
                .add(TideBlocks.VORTEX_CORE.get())
                .add(TideBlocks.WHIRLPOOL_CORE.get())
                .add(TideBlocks.FLOW_UPGRADE.get())
                .add(TideBlocks.BOUNTY_UPGRADE.get())
                .add(TideBlocks.RANGE_UPGRADE.get())
                .add(TideBlocks.ITEM_FILTER.get())
                .add(TideBlocks.FLUID_FILTER.get())
                .add(TideBlocks.ENTITY_FILTER.get());
        tag(TideTags.DEAD_CORAL_WALL_FANS)
                .add(Blocks.DEAD_BRAIN_CORAL_WALL_FAN)
                .add(Blocks.DEAD_BUBBLE_CORAL_WALL_FAN)
                .add(Blocks.DEAD_FIRE_CORAL_WALL_FAN)
                .add(Blocks.DEAD_HORN_CORAL_WALL_FAN)
                .add(Blocks.DEAD_TUBE_CORAL_WALL_FAN);
        tag(BlockTags.MINEABLE_WITH_AXE)
                .add(TideBlocks.DRIFTWOOD_LOG.get())
                .add(TideBlocks.DRIFTWOOD_PLANKS.get())
                .add(TideBlocks.DRIFTWOOD_STAIRS.get())
                .add(TideBlocks.DRIFTWOOD_SLAB.get())
                .add(TideBlocks.DRIFTWOOD_FENCE.get())
                .add(TideBlocks.DRIFTWOOD_FENCE_GATE.get())
//                .add(TideBlocks.DRIFTWOOD_SIGN.get())
//                .add(TideBlocks.DRIFTWOOD_WALL_SIGN.get())
                .add(TideBlocks.DRIFTWOOD_PRESSURE_PLATE.get())
                .add(TideBlocks.DRIFTWOOD_BUTTON.get())
                .add(TideBlocks.DRIFTWOOD_COLUMN.get())
                .add(TideBlocks.DRIFTWOOD_DOOR.get())
                .add(TideBlocks.DRIFTWOOD_TRAPDOOR.get());
        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(TideBlocks.BASIN_CORE.get())
                .add(TideBlocks.CURRENT_CORE.get())
                .add(TideBlocks.DEPOSITION_CORE.get())
                .add(TideBlocks.EROSION_CORE.get())
                .add(TideBlocks.PELAGIC_CORE.get())
                .add(TideBlocks.REEF_CORE.get())
                .add(TideBlocks.SINKHOLE_CORE.get())
                .add(TideBlocks.TRAWL_CORE.get())
                .add(TideBlocks.VORTEX_CORE.get())
                .add(TideBlocks.WHIRLPOOL_CORE.get())
                .add(TideBlocks.FLOW_UPGRADE.get())
                .add(TideBlocks.BOUNTY_UPGRADE.get())
                .add(TideBlocks.RANGE_UPGRADE.get())
                .add(TideBlocks.ITEM_FILTER.get())
                .add(TideBlocks.FLUID_FILTER.get())
                .add(TideBlocks.ENTITY_FILTER.get())
                .add(TideBlocks.PRISMARINE_PILLAR.get())
                .add(TideBlocks.EXPLOSION_ROD.get())
                .add(TideBlocks.SEASHELL_BRASS_BLOCK.get())
                .add(TideBlocks.TURTLE_STEEL_BLOCK.get())
                .add(TideBlocks.PUFFERGOLD_BLOCK.get());
        tag(BlockTags.PLANKS)
                .add(TideBlocks.DRIFTWOOD_PLANKS.get());
        tag(BlockTags.STAIRS)
                .add(TideBlocks.DRIFTWOOD_STAIRS.get());
        tag(BlockTags.WOODEN_STAIRS)
                .add(TideBlocks.DRIFTWOOD_STAIRS.get());
        tag(BlockTags.SLABS)
                .add(TideBlocks.DRIFTWOOD_SLAB.get());
        tag(BlockTags.WOODEN_SLABS)
                .add(TideBlocks.DRIFTWOOD_SLAB.get());
        tag(BlockTags.FENCES)
                .add(TideBlocks.DRIFTWOOD_FENCE.get());
        tag(BlockTags.WOODEN_FENCES)
                .add(TideBlocks.DRIFTWOOD_FENCE.get());
        tag(BlockTags.FENCE_GATES)
                .add(TideBlocks.DRIFTWOOD_FENCE_GATE.get());
//        tag(BlockTags.SIGNS)
//                .add(TideBlocks.DRIFTWOOD_SIGN.get());
        tag(BlockTags.WOODEN_PRESSURE_PLATES)
                .add(TideBlocks.DRIFTWOOD_PRESSURE_PLATE.get());
        tag(BlockTags.BUTTONS)
                .add(TideBlocks.DRIFTWOOD_BUTTON.get());
        tag(BlockTags.WOODEN_BUTTONS)
                .add(TideBlocks.DRIFTWOOD_BUTTON.get());
        tag(BlockTags.DOORS)
                .add(TideBlocks.DRIFTWOOD_DOOR.get());
        tag(BlockTags.WOODEN_DOORS)
                .add(TideBlocks.DRIFTWOOD_DOOR.get());
        tag(BlockTags.TRAPDOORS)
                .add(TideBlocks.DRIFTWOOD_TRAPDOOR.get());
        tag(BlockTags.WOODEN_TRAPDOORS)
                .add(TideBlocks.DRIFTWOOD_TRAPDOOR.get());
    }
}
