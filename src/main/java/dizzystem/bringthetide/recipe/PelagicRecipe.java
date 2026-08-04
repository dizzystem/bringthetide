package dizzystem.bringthetide.recipe;


import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dizzystem.bringthetide.BringTheTide;
import dizzystem.bringthetide.registration.TideRecipes;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PelagicRecipe implements Recipe<RecipeWrapper> {
    private final Ingredient[] ingredients;
    private final ItemStack result;

    public PelagicRecipe(Ingredient[] ingredients, ItemStack result){
        this.ingredients = ingredients;
        this.result = result;
    }

    @Override
    public @NotNull NonNullList<Ingredient> getIngredients(){
        NonNullList<Ingredient> list = NonNullList.create();
        list.addAll(List.of(this.ingredients));
        return list;
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean matches(RecipeWrapper inputs, Level level){
        int containerSize = inputs.getContainerSize();
        if (containerSize != this.ingredients.length){
            return false;
        }

        //These are the items that get put on the cores on the sides.
        for (int i=0;i<this.ingredients.length;i++){
            boolean match = false;
            Ingredient ingredient = this.ingredients[i];
            Map<Integer,Boolean> used = new HashMap<>();

            for (int j=0;j<containerSize;j++){
                if (used.get(j) != null){
                    continue;
                }

                ItemStack item = inputs.getItem(j);

                if (ingredient.test(item)){
                    match = true;
                    used.put(j, true);
                    break;
                }
            }

            if (!match){
                return false;
            }
        }

        return true;
    }

    @Override
    @ParametersAreNonnullByDefault
    public @NotNull ItemStack assemble(RecipeWrapper inputs, RegistryAccess registryAccess){
        return this.result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height){ return true; }

    @Override
    @ParametersAreNonnullByDefault
    public @NotNull ItemStack getResultItem(RegistryAccess registryAccess) {
        return this.result;
    }

    @Override
    public @NotNull ResourceLocation getId() {
        return ResourceLocation.fromNamespaceAndPath(BringTheTide.MODID, "pelagic_"+result.getItem().toString());
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return TideRecipes.PELAGIC_SERIALIZER.get();
    }

    @Override
    public @NotNull RecipeType<?> getType() { return TideRecipes.PELAGIC.get(); }

    @Override
    public boolean isSpecial() { return true; }

    public static class Serializer implements RecipeSerializer<PelagicRecipe> {
        @Override
        @ParametersAreNonnullByDefault
        public @NotNull PelagicRecipe fromJson(ResourceLocation recipeId, JsonObject jsonObject) {
            JsonArray ingredientsElement = GsonHelper.getAsJsonArray(jsonObject, "ingredients");
            int ingredientsLen = ingredientsElement.size();
            Ingredient[] ingredients = new Ingredient[ingredientsLen];
            for (int i=0;i<ingredientsLen;i++){
                ingredients[i] = (Ingredient.fromJson(ingredientsElement.get(i), false));
            }

            if (!jsonObject.has("result"))
                throw new com.google.gson.JsonSyntaxException("Missing result, expected to find a string or object");
            JsonObject resultJson = GsonHelper.getAsJsonObject(jsonObject, "result");

            ItemStack result = ShapedRecipe.itemStackFromJson(resultJson);

            return new PelagicRecipe(ingredients, result);
        }

        @Override
        @ParametersAreNonnullByDefault
        public PelagicRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            int numIngredients = buffer.readInt();
            Ingredient[] ingredients = new Ingredient[numIngredients];
            for (int i=0;i<numIngredients;i++){
                ingredients[i] = Ingredient.fromNetwork(buffer);
            }
            ItemStack result = buffer.readItem();
            return new PelagicRecipe(ingredients, result);
        }

        @Override
        @ParametersAreNonnullByDefault
        public void toNetwork(FriendlyByteBuf buffer, PelagicRecipe recipe) {
            int numIngredients = recipe.ingredients.length;
            buffer.writeInt(numIngredients);
            for (int i=0;i<numIngredients;i++){
                recipe.ingredients[i].toNetwork(buffer);
            }
            buffer.writeItem(recipe.result);
        }
    }
}
