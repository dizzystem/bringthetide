package dizzystem.bringthetide.compat;

import dizzystem.bringthetide.BringTheTide;
import dizzystem.bringthetide.recipe.ErosionRecipe;
import dizzystem.bringthetide.registration.TideBlocks;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;

public class ErosionCategory implements IRecipeCategory<ErosionRecipe> {
    public static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(BringTheTide.MODID, "erosion");
    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(BringTheTide.MODID,
            "textures/gui/jei_gui.png");

    public static final RecipeType<ErosionRecipe> EROSION_RECIPE_TYPE =
            new RecipeType<>(UID, ErosionRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;
    private final double RADIUS = 50;
    private final double OFFSET = Math.PI / 2;

    public ErosionCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 176, 143);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(TideBlocks.EROSION_CORE.get()));
    }

    @Override
    public RecipeType<ErosionRecipe> getRecipeType() {
        return EROSION_RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("Ritual of Erosion");
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, ErosionRecipe recipe, IFocusGroup focuses) {
        NonNullList<Ingredient> ingredients = recipe.getIngredients();

        builder.addSlot(RecipeIngredientRole.INPUT, 62, 63).addIngredients(ingredients.get(0));
        ArrayList<FluidStack> fluids = recipe.getFluidIngredients();
        int num_fluids = fluids.size();
        for (int i=0;i<num_fluids;i++){
            FluidStack fluid = fluids.get(i);
            int x = (int) (RADIUS * Math.cos(i * (Math.PI * 2) / num_fluids + OFFSET));
            int y = (int) (RADIUS * Math.sin(i * (Math.PI * 2) / num_fluids + OFFSET));
            builder.addSlot(RecipeIngredientRole.INPUT, 62 + x, 63 + y).addFluidStack(fluid.getFluid(), fluid.getAmount());
        }

        builder.addSlot(RecipeIngredientRole.OUTPUT, 152, 63).addItemStack(recipe.getResultItem(null));
    }
}
