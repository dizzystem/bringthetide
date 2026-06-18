package dizzystem.bringthetide.recipe;

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

public class ErosionRecipe implements Recipe<RecipeWrapper> {
    private final Ingredient ingredient;
    private final ItemStack result;

    public ErosionRecipe(Ingredient ingredient, ItemStack result){
        this.ingredient = ingredient;
        this.result = result;
    }

    @Override
    public NonNullList<Ingredient> getIngredients(){
        NonNullList<Ingredient> list = NonNullList.create();
        list.add(this.ingredient);
        return list;
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean matches(RecipeWrapper inputs, Level level){
        //This is the item that's thrown into the pool.
        return this.ingredient.test(inputs.getItem(0));
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
        return ResourceLocation.fromNamespaceAndPath(BringTheTide.MODID, "erosion:"+result.getItem());
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return TideRecipes.EROSION_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return TideRecipes.EROSION.get();
    }

    //this reads recipes from json i think?
    //modified from net/minecraft/world/item/crafting/SimpleCookingSerializer
    public static class Serializer implements RecipeSerializer<ErosionRecipe> {
        @Override
        @ParametersAreNonnullByDefault
        public ErosionRecipe fromJson(ResourceLocation recipeId, JsonObject jsonObject) {
            JsonObject ingredientJson = GsonHelper.getAsJsonObject(jsonObject, "ingredient");
            Ingredient ingredient = Ingredient.fromJson(GsonHelper.getAsJsonObject(ingredientJson, "item"),
                    false);
            //todo: account for ingredient.count here for more than 1 item in ingredient
            if (!jsonObject.has("result"))
                throw new com.google.gson.JsonSyntaxException("Missing result, expected to find a string or object");
            JsonObject resultJson = GsonHelper.getAsJsonObject(jsonObject, "result");
            ItemStack itemstack = ShapedRecipe.itemStackFromJson(resultJson);
            return new ErosionRecipe(ingredient, itemstack);
        }

        @Override
        @ParametersAreNonnullByDefault
        public ErosionRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            Ingredient ingredient = Ingredient.fromNetwork(buffer);
            ItemStack itemstack = buffer.readItem();
            return new ErosionRecipe(ingredient, itemstack);
        }

        @Override
        @ParametersAreNonnullByDefault
        public void toNetwork(FriendlyByteBuf buffer, ErosionRecipe recipe) {
            recipe.ingredient.toNetwork(buffer);
            buffer.writeItem(recipe.result);
        }
    }
}
