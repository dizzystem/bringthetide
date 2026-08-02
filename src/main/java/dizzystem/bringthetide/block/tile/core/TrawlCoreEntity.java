package dizzystem.bringthetide.block.tile.core;

import dizzystem.bringthetide.block.tile.CoreEntity;
import dizzystem.bringthetide.registration.TideBlocks;
import dizzystem.bringthetide.registration.TideParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ItemFishedEvent;

import java.util.List;
import java.util.Map;

import static java.util.Map.entry;

public class TrawlCoreEntity extends CoreEntity {
    Map<Vec3i, Object> requiredShape = Map.ofEntries(
            entry(new Vec3i(-1, 0, 0), Blocks.PRISMARINE),
            entry(new Vec3i(1, 0, 0), Blocks.PRISMARINE)
    );
    FishingHook hook = null;

    public TrawlCoreEntity(BlockPos blockPos, BlockState blockState){
        super(TideBlocks.TRAWL_CORE_ENTITY.get(), blockPos, blockState);
        //10 seconds base
        setBaseTicksPerAction(200);
    }

    public Map<Vec3i, Object> getRequiredShape(){
        return this.requiredShape;
    }

    //this is called every getTicksPerAction() ticks
    @Override
    public void doPeriodicAction(ServerLevel level, Vec3 pos){
        ItemStack fishingRod = new ItemStack(Items.FISHING_ROD, 1);
        LootParams lootparams = (new LootParams.Builder(level))
                .withParameter(LootContextParams.ORIGIN, pos)
                .withParameter(LootContextParams.TOOL, fishingRod)
                .withLuck(this.getLuck())
                .create(LootContextParamSets.FISHING);
        LootTable loottable = level.getServer().getLootData().getLootTable(BuiltInLootTables.FISHING);
        List<ItemStack> list = loottable.getRandomItems(lootparams);

        if (this.hook == null){
            this.hook = new FishingHook(EntityType.FISHING_BOBBER, level);
        }
        MinecraftForge.EVENT_BUS.post(new ItemFishedEvent(list, 0, this.hook));
        for (ItemStack item : list) {
            ItemEntity entity = new ItemEntity(level, pos.x, pos.y, pos.z, item);
            level.addFreshEntity(entity);
        }

        if (!list.isEmpty()){
            level.sendParticles(TideParticles.SPLASH.get(),
                    pos.x,
                    pos.y + 1,
                    pos.z,
                    10,
                    0,
                    0,
                    0,
                    0.1);
        }
    }
}
