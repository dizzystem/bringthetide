package dizzystem.bringthetide.compat;


import dizzystem.bringthetide.BringTheTide;
import dizzystem.bringthetide.recipe.DepositionRecipe;
import dizzystem.bringthetide.recipe.ErosionRecipe;
import dizzystem.bringthetide.recipe.PelagicRecipe;
import dizzystem.bringthetide.registration.TideRecipes;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeManager;

import java.util.List;

/**
 * copied from <a href="https://github.com/Tutorials-By-Kaupenjoe/Forge-Tutorial-1.20.X/tree/32-jeiComapt/src/main/java/net/kaupenjoe/tutorialmod/compat">kaupenjoe's tutorial</a>
 */
@JeiPlugin
public class JEIPlugin implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath(BringTheTide.MODID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(
                new DepositionCategory(registration.getJeiHelpers().getGuiHelper()),
                new ErosionCategory(registration.getJeiHelpers().getGuiHelper()),
                new PelagicCategory(registration.getJeiHelpers().getGuiHelper())
        );
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager recipeManager = Minecraft.getInstance().level.getRecipeManager();

        List<DepositionRecipe> depositionRecipes = recipeManager.getAllRecipesFor(TideRecipes.DEPOSITION.get());
        registration.addRecipes(DepositionCategory.DEPOSITION_RECIPE_TYPE, depositionRecipes);
        List<ErosionRecipe> erosionRecipes = recipeManager.getAllRecipesFor(TideRecipes.EROSION.get());
        registration.addRecipes(ErosionCategory.EROSION_RECIPE_TYPE, erosionRecipes);
        List<PelagicRecipe> pelagicRecipes = recipeManager.getAllRecipesFor(TideRecipes.PELAGIC.get());
        registration.addRecipes(PelagicCategory.PELAGIC_RECIPE_TYPE, pelagicRecipes);
    }
}