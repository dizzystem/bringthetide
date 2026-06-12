package dizzystem.bringthetide.loot;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dizzystem.bringthetide.registration.TideItems;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;

public class TideLootModifier extends LootModifier {
    public static final Codec<TideLootModifier> CODEC = RecordCodecBuilder.create(inst ->
            LootModifier.codecStart(inst).and(
                    Codec.STRING.fieldOf("chestType").forGetter(e -> e.chestType)
            ).apply(inst, TideLootModifier::new));
    private final String chestType;

    public TideLootModifier(LootItemCondition[] conditions, String chestType){
        super(conditions);
        this.chestType = chestType;
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec(){
        return CODEC;
    }

    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context){
        ResourceLocation lootTableId = context.getQueriedLootTableId();

        if (lootTableId.equals(BuiltInLootTables.SHIPWRECK_MAP) ||
            lootTableId.equals(BuiltInLootTables.SHIPWRECK_SUPPLY) ||
            lootTableId.equals(BuiltInLootTables.SHIPWRECK_TREASURE)){
            int amt = context.getRandom().nextIntBetweenInclusive(3, 6);
            generatedLoot.add(new ItemStack(TideItems.DRIFTWOOD_LOG_ITEM.get(), amt));
        }

        return generatedLoot;
    }
}
