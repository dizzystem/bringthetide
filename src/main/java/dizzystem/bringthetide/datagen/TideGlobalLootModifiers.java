package dizzystem.bringthetide.datagen;

import dizzystem.bringthetide.BringTheTide;
import dizzystem.bringthetide.TideLootModifier;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.data.GlobalLootModifierProvider;

public class TideGlobalLootModifiers extends GlobalLootModifierProvider {
    public TideGlobalLootModifiers(PackOutput packOutput){
        super(packOutput, BringTheTide.MODID);
    }

    @Override
    protected void start(){
        add("shipwreck", new TideLootModifier(new LootItemCondition[] {
        },"shipwreck"));
    }
}
