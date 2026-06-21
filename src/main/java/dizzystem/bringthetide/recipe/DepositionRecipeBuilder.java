package dizzystem.bringthetide.recipe;

import com.google.gson.GsonBuilder;
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

public class DepositionRecipeBuilder {
    private final JsonObject mainIngredient = new JsonObject();
    private final ArrayList<Ingredient> catalysts = new ArrayList<>();
    private final JsonObject result = new JsonObject();
    private Integer craftingTime;
    private final RecipeSerializer<?> serializer;

    public DepositionRecipeBuilder(RecipeSerializer<?> serializer) {
        this.serializer = serializer;
    }

    public static DepositionRecipeBuilder customRecipe(RecipeSerializer<?> serializer){
        return new DepositionRecipeBuilder(serializer);
    }

    public DepositionRecipeBuilder mainIngredient(Ingredient ingredient){
        return mainIngredient(ingredient, 1);
    }

    public DepositionRecipeBuilder mainIngredient(Ingredient ingredient, int count){
        this.mainIngredient.add("item", ingredient.toJson());
        this.mainIngredient.addProperty("count", count);
        return this;
    }

    public DepositionRecipeBuilder catalyst(Ingredient ingredient){
        this.catalysts.add(ingredient);
        return this;
    }

    public DepositionRecipeBuilder result(ItemStack result){
        ResourceLocation itemRegistryName = BuiltInRegistries.ITEM.getKey(result.getItem());
        if (itemRegistryName == null) {
            throw new IllegalStateException("Result item is not registered.");
        }
        this.result.addProperty("item", itemRegistryName.toString());
        this.result.addProperty("count", result.getCount());
        return this;
    }

    public DepositionRecipeBuilder craftingTime(int time){
        this.craftingTime = time;
        return this;
    }

    public void save(Consumer<FinishedRecipe> consumer, ResourceLocation id) {
        consumer.accept(new FinishedRecipe(){
            @Override
            public void serializeRecipeData(JsonObject json) {
                json.addProperty("type", ForgeRegistries.RECIPE_SERIALIZERS.getKey(serializer).toString());
                json.add("mainIngredient", mainIngredient);
                JsonArray jsonCatalysts = new JsonArray();
                catalysts.forEach(c -> jsonCatalysts.add(c.toJson()));
                json.add("catalysts", jsonCatalysts);
                json.add("result", result);
                if (craftingTime != null){
                    json.addProperty("craftingTime", craftingTime);
                }
            }

            @Override
            public ResourceLocation getId() {
                return id;
            }

            @Override
            public RecipeSerializer<?> getType() {
                return TideRecipes.DEPOSITION_SERIALIZER.get();
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
