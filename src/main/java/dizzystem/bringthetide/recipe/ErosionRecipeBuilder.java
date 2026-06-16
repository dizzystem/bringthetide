package dizzystem.bringthetide.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dizzystem.bringthetide.registration.TideRecipes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class ErosionRecipeBuilder extends TideRecipeBuilder {
    private final JsonObject ingredient = new JsonObject();
    private final JsonObject result = new JsonObject();
    private final RecipeSerializer<?> serializer;

    public ErosionRecipeBuilder(RecipeSerializer<?> serializer) {
        this.serializer = serializer;
    }

    public static ErosionRecipeBuilder customRecipe(RecipeSerializer<?> serializer){
        return new ErosionRecipeBuilder(serializer);
    }

    public ErosionRecipeBuilder ingredient(Ingredient ingredient){
        return ingredient(ingredient, 1);
    }

    public ErosionRecipeBuilder ingredient(Ingredient ingredient, int count){
        this.ingredient.add("item", ingredient.toJson());
        this.ingredient.addProperty("count", count);
        return this;
    }

    public ErosionRecipeBuilder result(ItemStack result){
        ResourceLocation itemRegistryName = BuiltInRegistries.ITEM.getKey(result.getItem());
        if (itemRegistryName == null) {
            throw new IllegalStateException("Result item is not registered.");
        }
        this.result.addProperty("item", itemRegistryName.toString());
        this.result.addProperty("count", result.getCount());
        return this;
    }

    public void save(Consumer<FinishedRecipe> consumer, ResourceLocation id) {
        consumer.accept(new FinishedRecipe(){
            @Override
            public void serializeRecipeData(JsonObject json) {
                json.addProperty("type", ForgeRegistries.RECIPE_SERIALIZERS.getKey(serializer).toString());
                json.add("ingredient", ingredient);
                json.add("result", result);
            }

            @Override
            public ResourceLocation getId() {
                return id;
            }

            @Override
            public RecipeSerializer<?> getType() {
                return TideRecipes.EROSION_SERIALIZER.get();
            }

            @Override
            public @Nullable JsonObject serializeAdvancement() {
                return null;
            }

            @Override
            public @Nullable ResourceLocation getAdvancementId() {
                return null;
            }
        });
    }
}
