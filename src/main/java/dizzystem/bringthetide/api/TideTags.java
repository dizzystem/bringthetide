package dizzystem.bringthetide.api;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public final class TideTags {
    private TideTags(){

    }

    public static final TagKey<Item> NON_DRIFTWOOD_LOGS = itemTag("bringthetide:non_driftwood_logs");
    public static final TagKey<Block> VALID_POOL_BLOCK = blockTag("bringthetide:valid_pool_block");
    public static final TagKey<Block> DEAD_CORAL_WALL_FANS = blockTag("bringthetide:dead_coral_wall_fans");

    private static TagKey<Item> itemTag(String name){
        return TagKey.create(Registries.ITEM, ResourceLocation.parse(name));
    }
    private static TagKey<Block> blockTag(String name){
        return TagKey.create(Registries.BLOCK, ResourceLocation.parse(name));
    }
}
