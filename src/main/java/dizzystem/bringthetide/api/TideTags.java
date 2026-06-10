package dizzystem.bringthetide.api;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public final class TideTags {
    private TideTags(){

    }

    public static final TagKey<Block> GROWTH_ACCELERATABLE = blockTag("bringthetide:growth_acceleratable");
    public static final TagKey<Block> VALID_POOL_BLOCK = blockTag("bringthetide:valid_pool_block");

    private static TagKey<Block> blockTag(String name){
        return TagKey.create(Registries.BLOCK, ResourceLocation.parse(name));
    }
}
