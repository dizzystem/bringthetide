package dizzystem.bringthetide.tile;

import dizzystem.bringthetide.registration.TideBlocks;
import dizzystem.bringthetide.registration.TideParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
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
import net.minecraftforge.event.entity.player.ItemFishedEvent;

import java.util.List;
import java.util.Map;

import static java.util.Map.entry;

public class TrawlCoreEntity extends CoreEntity {
    Map<Vec3i, Object> requiredShape = Map.ofEntries(
            entry(new Vec3i(-1, 0, 0), Blocks.PRISMARINE),
            entry(new Vec3i(1, 0, 0), Blocks.PRISMARINE)
    );

    public TrawlCoreEntity(BlockPos blockPos, BlockState blockState){
        super(TideBlocks.TRAWL_CORE_ENTITY.get(), blockPos, blockState);
    }

    public Map<Vec3i, Object> getRequiredShape(){
        return this.requiredShape;
    }

    public void tickServer() {
        super.tickServer();

        if (!this.isPoolActive()) {
            return;
        }

        ServerLevel level = (ServerLevel) getLevel();
        Vec3 poolCentre = this.getPoolCentre().getCenter();
        ItemStack fishingRod = new ItemStack(Items.FISHING_ROD, 1);

        //every 4 seconds
        if (ticks % getTicksPerAction() == 0){
            ItemFishedEvent event = null;
            LootParams lootparams = (new LootParams.Builder(level))
                    .withParameter(LootContextParams.ORIGIN, poolCentre)
                    .withParameter(LootContextParams.TOOL, fishingRod)
                    .withLuck(this.getLuck())
                    .create(LootContextParamSets.FISHING);
            LootTable loottable = level.getServer().getLootData().getLootTable(BuiltInLootTables.FISHING);
            List<ItemStack> list = loottable.getRandomItems(lootparams);

            for (ItemStack item : list) {
                ItemEntity entity = new ItemEntity(level, poolCentre.x, poolCentre.y, poolCentre.z, item);
                level.addFreshEntity(entity);
            }

            if (!list.isEmpty()){
                level.sendParticles(TideParticles.SPLASH.get(),
                        poolCentre.x,
                        poolCentre.y + 1,
                        poolCentre.z,
                        10,
                        0,
                        0,
                        0,
                        0.1);
            }
        }
    }
}
