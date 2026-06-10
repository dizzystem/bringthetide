package dizzystem.bringthetide.datagen;

import dizzystem.bringthetide.registration.TideItems;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;

import java.util.function.Consumer;

public class TideRecipes extends RecipeProvider {

    public TideRecipes(PackOutput packOutput){
        super(packOutput);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> consumer) {
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
