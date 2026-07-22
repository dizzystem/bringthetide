package dizzystem.bringthetide.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dizzystem.bringthetide.registration.TideRecipes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.function.Consumer;

public class PelagicRecipeBuilder {
    private final ArrayList<Ingredient> ingredients = new ArrayList<>();
    private final JsonObject result = new JsonObject();
    private final RecipeSerializer<?> serializer;

    public PelagicRecipeBuilder(RecipeSerializer<?> serializer) {
        this.serializer = serializer;
    }

    public static PelagicRecipeBuilder customRecipe(RecipeSerializer<?> serializer){
        return new PelagicRecipeBuilder(serializer);
    }

    public PelagicRecipeBuilder ingredient(Ingredient ingredient){
        this.ingredients.add(ingredient);
        return this;
    }

    public PelagicRecipeBuilder result(ItemStack result){
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
                JsonArray jsonIngredients = new JsonArray();
                ingredients.forEach(c -> jsonIngredients.add(c.toJson()));
                json.add("ingredients", jsonIngredients);
                json.add("result", result);
            }

            @Override
            public ResourceLocation getId() {
                return id;
            }

            @Override
            public RecipeSerializer<?> getType() {
                return TideRecipes.PELAGIC_SERIALIZER.get();
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
