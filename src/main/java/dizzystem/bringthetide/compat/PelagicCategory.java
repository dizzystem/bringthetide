package dizzystem.bringthetide.compat;

import dizzystem.bringthetide.BringTheTide;
import dizzystem.bringthetide.recipe.PelagicRecipe;
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
import org.jetbrains.annotations.Nullable;

public class PelagicCategory implements IRecipeCategory<PelagicRecipe> {
    public static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(BringTheTide.MODID, "pelagic");
    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(BringTheTide.MODID,
            "textures/gui/jei_gui.png");

    public static final RecipeType<PelagicRecipe> PELAGIC_RECIPE_TYPE =
            new RecipeType<>(UID, PelagicRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;
    private final double RADIUS = 25;
    private final double OFFSET = Math.PI / 2;

    public PelagicCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 176, 143);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(TideBlocks.PELAGIC_CORE.get()));
    }

    @Override
    public RecipeType<PelagicRecipe> getRecipeType() {
        return PELAGIC_RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("Ritual of the Pelagic Zone");
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public @Nullable IDrawable getBackground() { return background; }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, PelagicRecipe recipe, IFocusGroup focuses) {
        NonNullList<Ingredient> ingredients = recipe.getIngredients();

        int num_catalysts = ingredients.size();
        for (int i=0;i<num_catalysts;i++){
            Ingredient catalyst = ingredients.get(i);
            int x = (int) (RADIUS * Math.cos(i * (Math.PI * 2) / num_catalysts - OFFSET));
            int y = (int) (RADIUS * Math.sin(i * (Math.PI * 2) / num_catalysts - OFFSET));
            builder.addSlot(RecipeIngredientRole.INPUT, 62 + x, 63 + y).addIngredients(catalyst);
        }

        builder.addSlot(RecipeIngredientRole.OUTPUT, 152, 63).addItemStack(recipe.getResultItem(null));
    }
}
