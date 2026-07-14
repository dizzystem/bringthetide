package dizzystem.bringthetide.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dizzystem.bringthetide.BringTheTide;
import dizzystem.bringthetide.registration.TideRecipes;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.TagParser;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class ErosionRecipe implements Recipe<RecipeWrapper> {
    public static final int DEFAULT_CRAFTING_TIME = 80;
    private final Ingredient ingredient;
    private final FluidStack[] fluids;
    private final ItemStack result;
    private final int craftingTime;

    public ErosionRecipe(Ingredient ingredient, FluidStack[] fluids, ItemStack result, int craftingTime){
        this.ingredient = ingredient;
        this.fluids = fluids;
        this.result = result;
        this.craftingTime = craftingTime;
    }

    //We also do smelting at 10mb of lava each.
    public ErosionRecipe(SmeltingRecipe recipe){
        this(
                recipe.getIngredients().get(0),
                new FluidStack[]{new FluidStack(Fluids.LAVA, 10)},
                recipe.getResultItem(null),
                recipe.getCookingTime() * 2 / 5
                );
    }

    @Override
    public @NotNull NonNullList<Ingredient> getIngredients(){
        NonNullList<Ingredient> list = NonNullList.create();
        list.add(this.ingredient);
        return list;
    }

    public ArrayList<FluidStack> getFluidIngredients(){
        return new ArrayList<>(Arrays.asList(fluids));
    }

    public int getCraftingTime() {
        return craftingTime;
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
    public @NotNull ResourceLocation getId() {
        return ResourceLocation.fromNamespaceAndPath(BringTheTide.MODID, "erosion_"+result.getItem());
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return TideRecipes.EROSION_SERIALIZER.get();
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return TideRecipes.EROSION.get();
    }

    //this reads recipes from json i think?
    //modified from net/minecraft/world/item/crafting/SimpleCookingSerializer
    public static class Serializer implements RecipeSerializer<ErosionRecipe> {
        @Override
        @ParametersAreNonnullByDefault
        public @NotNull ErosionRecipe fromJson(ResourceLocation recipeId, JsonObject jsonObject) {
            JsonObject ingredientJson = GsonHelper.getAsJsonObject(jsonObject, "ingredient");
            Ingredient ingredient = Ingredient.fromJson(GsonHelper.getAsJsonObject(ingredientJson, "item"),
                    false);

            JsonArray fluidsArray = GsonHelper.getAsJsonArray(jsonObject, "fluids");
            int fluidsLen = fluidsArray.size();
            FluidStack[] fluids = new FluidStack[fluidsLen];
            try {
                for (int i=0;i<fluidsLen;i++){
                    fluids[i] = FluidStack.loadFluidStackFromNBT(TagParser.parseTag(fluidsArray.get(i).getAsString()));
                }
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }

            //todo: account for ingredient.count here for more than 1 item in ingredient
            if (!jsonObject.has("result"))
                throw new com.google.gson.JsonSyntaxException("Missing result, expected to find a string or object");
            JsonObject resultJson = GsonHelper.getAsJsonObject(jsonObject, "result");
            ItemStack itemstack = ShapedRecipe.itemStackFromJson(resultJson);
            int craftingTime = GsonHelper.getAsInt(jsonObject, "craftingTime", DEFAULT_CRAFTING_TIME);
            return new ErosionRecipe(ingredient, fluids, itemstack, craftingTime);
        }

        @Override
        @ParametersAreNonnullByDefault
        public ErosionRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            Ingredient ingredient = Ingredient.fromNetwork(buffer);
            int numFluids = buffer.readInt();
            FluidStack[] fluids = new FluidStack[numFluids];
            for (int i=0;i<numFluids;i++){
                fluids[i] = FluidStack.readFromPacket(buffer);
            }
            ItemStack result = buffer.readItem();
            int craftingTime = buffer.readInt();
            return new ErosionRecipe(ingredient, fluids, result, craftingTime);
        }

        @Override
        @ParametersAreNonnullByDefault
        public void toNetwork(FriendlyByteBuf buffer, ErosionRecipe recipe) {
            recipe.ingredient.toNetwork(buffer);
            int numFluids = recipe.fluids.length;
            buffer.writeInt(numFluids);
            for (int i=0;i<numFluids;i++){
                recipe.fluids[i].writeToPacket(buffer);
            }
            buffer.writeItem(recipe.result);
            buffer.writeInt(recipe.craftingTime);
        }
    }
}
