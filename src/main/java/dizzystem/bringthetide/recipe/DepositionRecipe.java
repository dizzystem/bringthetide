package dizzystem.bringthetide.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;
import dizzystem.bringthetide.BringTheTide;
import dizzystem.bringthetide.registration.TideRecipes;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DepositionRecipe implements Recipe<RecipeWrapper> {
    public static final int DEFAULT_CRAFTING_TIME = 80;
    private final Ingredient mainIngredient;
    private final Ingredient[] catalysts;
    private final ItemStack result;
    private final int craftingTime;

    public DepositionRecipe(Ingredient mainIngredient, Ingredient[] catalysts, ItemStack result, int craftingTime){
        this.mainIngredient = mainIngredient;
        this.catalysts = catalysts;
        this.result = result;
        this.craftingTime = craftingTime;
    }

    @Override
    public NonNullList<Ingredient> getIngredients(){
        NonNullList<Ingredient> list = NonNullList.create();
        list.add(this.mainIngredient);
        list.addAll(List.of(catalysts));
        return list;
    }

    public int getCraftingTime() {
        return craftingTime;
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean matches(RecipeWrapper inputs, Level level){
        //This is the item that's thrown into the pool.
        if (!this.mainIngredient.test(inputs.getItem(0))){
            return false;
        }

        int containerSize = inputs.getContainerSize() - 1;
        if (containerSize < this.catalysts.length){
            //extra catalysts are fine
            return false;
        }

        //These are the items that get put on the cores on the sides.
        for (int i=0;i<this.catalysts.length;i++){
            boolean match = false;
            Ingredient catalyst = this.catalysts[i];
            Map<Integer,Boolean> used = new HashMap<>();

            for (int j=0;j<containerSize;j++){
                if (used.get(j) != null){
                    continue;
                }

                ItemStack item = inputs.getItem(j+1);

                if (catalyst.test(item)){
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
    public boolean canCraftInDimensions(int width, int height){
        return true;
    }

    @Override
    @ParametersAreNonnullByDefault
    public @NotNull ItemStack getResultItem(RegistryAccess registryAccess) {
        return this.result;
    }

    @Override
    public ResourceLocation getId() {
        return ResourceLocation.fromNamespaceAndPath(BringTheTide.MODID, "deposition:"+result.toString());
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return TideRecipes.DEPOSITION_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return TideRecipes.DEPOSITION.get();
    }

    public static class Serializer implements RecipeSerializer<DepositionRecipe> {
        @Override
        @ParametersAreNonnullByDefault
        public DepositionRecipe fromJson(ResourceLocation recipeId, JsonObject jsonObject) {
            JsonObject ingredientJson = GsonHelper.getAsJsonObject(jsonObject, "mainIngredient");
            Ingredient mainIngredient = Ingredient.fromJson(GsonHelper.getAsJsonObject(ingredientJson, "item"),
                    false);

            JsonArray catalystsElement = GsonHelper.getAsJsonArray(jsonObject, "catalysts");
            int catalystsLen = catalystsElement.size();
            Ingredient[] catalysts = new Ingredient[catalystsLen];
            for (int i=0;i<catalystsLen;i++){
                catalysts[i] = (Ingredient.fromJson(catalystsElement.get(i), false));
            }

            if (!jsonObject.has("result"))
                throw new com.google.gson.JsonSyntaxException("Missing result, expected to find a string or object");
            JsonObject resultJson = GsonHelper.getAsJsonObject(jsonObject, "result");
            ItemStack result = ShapedRecipe.itemStackFromJson(resultJson);

            int craftingTime = GsonHelper.getAsInt(jsonObject, "craftingTime", DEFAULT_CRAFTING_TIME);
            return new DepositionRecipe(mainIngredient, catalysts, result, craftingTime);
        }

        @Override
        @ParametersAreNonnullByDefault
        public DepositionRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            Ingredient mainIngredient = Ingredient.fromNetwork(buffer);
            int numCatalysts = buffer.readInt();
            Ingredient[] catalysts = new Ingredient[numCatalysts];
            for (int i=0;i<numCatalysts;i++){
                catalysts[i] = Ingredient.fromNetwork(buffer);
            }
            ItemStack result = buffer.readItem();
            int craftingTime = buffer.readInt();
            return new DepositionRecipe(mainIngredient, catalysts, result, craftingTime);
        }

        @Override
        @ParametersAreNonnullByDefault
        public void toNetwork(FriendlyByteBuf buffer, DepositionRecipe recipe) {
            recipe.mainIngredient.toNetwork(buffer);
            int numCatalysts = recipe.catalysts.length;
            buffer.writeInt(numCatalysts);
            for (int i=0;i<numCatalysts;i++){
                recipe.catalysts[i].toNetwork(buffer);
            }
            buffer.writeItem(recipe.result);
            buffer.writeInt(recipe.craftingTime);
        }
    }
}
