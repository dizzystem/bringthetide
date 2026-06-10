package dizzystem.bringthetide.datagen;

import com.google.common.collect.ImmutableMap;
import dizzystem.bringthetide.BringTheTide;
import dizzystem.bringthetide.registration.TideBlocks;
import net.minecraft.data.loot.packs.VanillaBlockLoot;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.ApplyExplosionDecay;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TideLootTables extends VanillaBlockLoot {
    private final Map<Block, Function<Block, LootTable.Builder>> overrides = createOverrides();

    @NotNull
    private ImmutableMap<Block, Function<Block, LootTable.Builder>> createOverrides(){
        return ImmutableMap.<Block, Function<Block, LootTable.Builder>>builder()
                .put(TideBlocks.SMALL_PRISMARINE_BUD.get(), this::prismarineBud)
                .put(TideBlocks.MEDIUM_PRISMARINE_BUD.get(), this::prismarineBud)
                .put(TideBlocks.LARGE_PRISMARINE_BUD.get(), this::prismarineBud)
                .put(TideBlocks.PRISMARINE_CLUSTER.get(), this::prismarineCluster)
                .build();
    }

    @Override
    protected void generate(){
        for (var block : getKnownBlocks()){
            add(block, overrides.getOrDefault(block, this::defaultBuilder).apply(block));
        }
    }

    private LootTable.Builder defaultBuilder(Block block){
        LootPoolEntryContainer.Builder<?> entry = LootItem.lootTableItem(block);
        LootPool.Builder pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(entry)
                .when(ExplosionCondition.survivesExplosion());

        return LootTable.lootTable().withPool(pool);
    }

    @Override
    protected Iterable<Block> getKnownBlocks(){
        return ForgeRegistries.BLOCKS.getEntries().stream()
                .filter(e -> e.getKey().location().getNamespace().equals(BringTheTide.MODID))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }

    private LootTable.Builder prismarineBud(Block bud){
        return createSilkTouchOnlyTable(bud);
    }

    private LootTable.Builder prismarineCluster(Block cluster){
        return createSilkTouchDispatchTable(cluster,
                LootItem.lootTableItem(Items.PRISMARINE_SHARD)
                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(4)))
                        .apply(ApplyBonusCount.addUniformBonusCount(Enchantments.BLOCK_FORTUNE))
                        .apply(ApplyExplosionDecay.explosionDecay()));
    }
}
