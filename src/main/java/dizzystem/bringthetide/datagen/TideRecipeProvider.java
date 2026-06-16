package dizzystem.bringthetide.datagen;

import dizzystem.bringthetide.BringTheTide;
import dizzystem.bringthetide.recipe.ErosionRecipe;
import dizzystem.bringthetide.recipe.ErosionRecipeBuilder;
import dizzystem.bringthetide.registration.TideItems;
import dizzystem.bringthetide.registration.TideRecipes;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

import java.util.function.Consumer;

public class TideRecipeProvider extends RecipeProvider {

    public TideRecipeProvider(PackOutput packOutput){
        super(packOutput);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> consumer) {
        RecipeSerializer<?> erosionSerializer = TideRecipes.EROSION_SERIALIZER.get();
        ErosionRecipeBuilder.customRecipe(erosionSerializer)
                .ingredient(Ingredient.of(Items.SANDSTONE))
                .result(new ItemStack(Items.SAND, 4))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(BringTheTide.MODID, "erosion_sandstone"));
        ErosionRecipeBuilder.customRecipe(erosionSerializer)
                .ingredient(Ingredient.of(Items.PRISMARINE))
                .result(new ItemStack(Items.PRISMARINE_SHARD, 4))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(BringTheTide.MODID, "erosion_prismarine"));
        ErosionRecipeBuilder.customRecipe(erosionSerializer)
                .ingredient(Ingredient.of(Items.NETHER_WART_BLOCK))
                .result(new ItemStack(Items.NETHER_WART, 4))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(BringTheTide.MODID, "erosion_netherwartblock"));
        ErosionRecipeBuilder.customRecipe(erosionSerializer)
                .ingredient(Ingredient.of(ItemTags.LOGS))
                .result(new ItemStack(TideItems.DRIFTWOOD_LOG_ITEM.get(), 1))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(BringTheTide.MODID, "erosion_log"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, TideItems.WAND.get())
                .pattern("  x")
                .pattern(" x ")
                .pattern("x  ")
                .define('x', TideItems.DRIFTWOOD_LOG_ITEM.get())
                .unlockedBy("has_driftwood", InventoryChangeTrigger.TriggerInstance.hasItems(
                        TideItems.DRIFTWOOD_LOG_ITEM.get()))
                .save(consumer);
    };
}
