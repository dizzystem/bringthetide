package dizzystem.bringthetide.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class TideDataGenerator {
    public static void generate(GatherDataEvent event){
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        generator.addProvider(event.includeClient(), new TideBlockStates(packOutput, event.getExistingFileHelper()));
        generator.addProvider(event.includeClient(), new TideItemModels(packOutput, event.getExistingFileHelper()));
        generator.addProvider(event.includeClient(), new TideLanguageProvider(packOutput, "en_us"));

        TideBlockTags blockTags = new TideBlockTags(packOutput, lookupProvider, event.getExistingFileHelper());
        generator.addProvider(event.includeServer(), blockTags);
        generator.addProvider(event.includeServer(), new TideItemTags(packOutput, lookupProvider, blockTags, event.getExistingFileHelper()));
        generator.addProvider(event.includeServer(), new TideRecipes(packOutput));
        generator.addProvider(event.includeServer(), new LootTableProvider(packOutput, Collections.emptySet(),
                List.of(new LootTableProvider.SubProviderEntry(TideLootTables::new, LootContextParamSets.BLOCK))));
        generator.addProvider(event.includeClient(), new TideParticleDescriptions(packOutput, existingFileHelper));
        generator.addProvider(event.includeServer(), new TideGlobalLootModifiers(packOutput));
    }
}
