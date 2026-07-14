package dizzystem.bringthetide.datagen;

import dizzystem.bringthetide.BringTheTide;
import dizzystem.bringthetide.api.TideTags;
import dizzystem.bringthetide.recipe.DepositionRecipe;
import dizzystem.bringthetide.recipe.DepositionRecipeBuilder;
import dizzystem.bringthetide.recipe.ErosionRecipe;
import dizzystem.bringthetide.recipe.ErosionRecipeBuilder;
import dizzystem.bringthetide.registration.TideItems;
import dizzystem.bringthetide.registration.TideRecipes;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;
import vazkii.patchouli.api.PatchouliAPI;
import vazkii.patchouli.api.PatchouliConfigAccess;

import java.util.function.Consumer;

public class TideRecipeProvider extends RecipeProvider {

    public TideRecipeProvider(PackOutput packOutput){
        super(packOutput);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> consumer) {
        RecipeSerializer<?> erosionSerializer = TideRecipes.EROSION_SERIALIZER.get();
        ErosionRecipeBuilder.customRecipe(erosionSerializer)
                .ingredient(Ingredient.of(TideTags.NON_DRIFTWOOD_LOGS))
                .fluid(new FluidStack(Fluids.WATER, 10))
                .result(new ItemStack(TideItems.DRIFTWOOD_LOG_ITEM.get(), 1))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(BringTheTide.MODID, "erosion_log"));
        ErosionRecipeBuilder.customRecipe(erosionSerializer)
                .ingredient(Ingredient.of(Items.SANDSTONE))
                .fluid(new FluidStack(Fluids.WATER, 10))
                .result(new ItemStack(Items.SAND, 4))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(BringTheTide.MODID, "erosion_sandstone"));
        ErosionRecipeBuilder.customRecipe(erosionSerializer)
                .ingredient(Ingredient.of(Items.RED_SANDSTONE))
                .fluid(new FluidStack(Fluids.WATER, 10))
                .result(new ItemStack(Items.RED_SAND, 4))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(BringTheTide.MODID, "erosion_redsandstone"));
        ErosionRecipeBuilder.customRecipe(erosionSerializer)
                .ingredient(Ingredient.of(Items.NETHER_WART_BLOCK))
                .fluid(new FluidStack(Fluids.WATER, 10))
                .result(new ItemStack(Items.NETHER_WART, 9))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(BringTheTide.MODID, "erosion_netherwartblock"));
        ErosionRecipeBuilder.customRecipe(erosionSerializer)
                .ingredient(Ingredient.of(Items.BRICKS))
                .fluid(new FluidStack(Fluids.WATER, 10))
                .result(new ItemStack(Items.BRICK, 4))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(BringTheTide.MODID, "erosion_bricks"));
        ErosionRecipeBuilder.customRecipe(erosionSerializer)
                .ingredient(Ingredient.of(Items.PURPUR_BLOCK))
                .fluid(new FluidStack(Fluids.WATER, 10))
                .result(new ItemStack(Items.POPPED_CHORUS_FRUIT, 1))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(BringTheTide.MODID, "erosion_purpurblock"));
        ErosionRecipeBuilder.customRecipe(erosionSerializer)
                .ingredient(Ingredient.of(Items.DRIPSTONE_BLOCK))
                .fluid(new FluidStack(Fluids.WATER, 10))
                .result(new ItemStack(Items.POINTED_DRIPSTONE, 4))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(BringTheTide.MODID, "erosion_dripstoneblock"));
        ErosionRecipeBuilder.customRecipe(erosionSerializer)
                .ingredient(Ingredient.of(Items.AMETHYST_BLOCK))
                .fluid(new FluidStack(Fluids.WATER, 10))
                .result(new ItemStack(Items.AMETHYST_SHARD, 4))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(BringTheTide.MODID, "erosion_amethystblock"));
        ErosionRecipeBuilder.customRecipe(erosionSerializer)
                .ingredient(Ingredient.of(Items.CLAY))
                .fluid(new FluidStack(Fluids.WATER, 10))
                .result(new ItemStack(Items.CLAY_BALL, 4))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(BringTheTide.MODID, "erosion_clayblock"));
        ErosionRecipeBuilder.customRecipe(erosionSerializer)
                .ingredient(Ingredient.of(Items.SNOW_BLOCK))
                .fluid(new FluidStack(Fluids.WATER, 10))
                .result(new ItemStack(Items.SNOWBALL, 4))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(BringTheTide.MODID, "erosion_snowblock"));
        ErosionRecipeBuilder.customRecipe(erosionSerializer)
                .ingredient(Ingredient.of(Items.PRISMARINE))
                .fluid(new FluidStack(Fluids.WATER, 10))
                .result(new ItemStack(Items.PRISMARINE_SHARD, 4))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(BringTheTide.MODID, "erosion_prismarine"));
        ErosionRecipeBuilder.customRecipe(erosionSerializer)
                .ingredient(Ingredient.of(Items.NETHER_BRICKS))
                .fluid(new FluidStack(Fluids.WATER, 10))
                .result(new ItemStack(Items.NETHER_BRICK, 4))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(BringTheTide.MODID, "erosion_netherbricks"));
        ErosionRecipeBuilder.customRecipe(erosionSerializer)
                .ingredient(Ingredient.of(Items.QUARTZ_BLOCK))
                .fluid(new FluidStack(Fluids.WATER, 10))
                .result(new ItemStack(Items.QUARTZ, 4))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(BringTheTide.MODID, "erosion_quartzblock"));
        ErosionRecipeBuilder.customRecipe(erosionSerializer)
                .ingredient(Ingredient.of(Items.BLUE_ICE))
                .fluid(new FluidStack(Fluids.WATER, 10))
                .result(new ItemStack(Items.PACKED_ICE, 9))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(BringTheTide.MODID, "erosion_blueice"));
        ErosionRecipeBuilder.customRecipe(erosionSerializer)
                .ingredient(Ingredient.of(Items.PACKED_ICE))
                .fluid(new FluidStack(Fluids.WATER, 10))
                .result(new ItemStack(Items.ICE, 9))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(BringTheTide.MODID, "erosion_packedice"));
        ErosionRecipeBuilder.customRecipe(erosionSerializer)
                .ingredient(Ingredient.of(Items.LEATHER))
                .fluid(new FluidStack(Fluids.WATER, 10))
                .result(new ItemStack(Items.RABBIT_HIDE, 4))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(BringTheTide.MODID, "erosion_leather"));
        ErosionRecipeBuilder.customRecipe(erosionSerializer)
                .ingredient(Ingredient.of(Items.BLAZE_ROD))
                .fluid(new FluidStack(Fluids.WATER, 10))
                .result(new ItemStack(Items.BLAZE_POWDER, 3))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(BringTheTide.MODID, "erosion_blazerod"));
        ErosionRecipeBuilder.customRecipe(erosionSerializer)
                .ingredient(Ingredient.of(Items.BONE))
                .fluid(new FluidStack(Fluids.WATER, 10))
                .result(new ItemStack(Items.BONE_MEAL, 4))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(BringTheTide.MODID, "erosion_bone"));
        ErosionRecipeBuilder.customRecipe(erosionSerializer)
                .ingredient(Ingredient.of(Items.COBWEB))
                .fluid(new FluidStack(Fluids.WATER, 10))
                .result(new ItemStack(Items.STRING, 1))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(BringTheTide.MODID, "erosion_cobweb"));
        ErosionRecipeBuilder.customRecipe(erosionSerializer)
                .ingredient(Ingredient.of(Items.MAGMA_BLOCK))
                .fluid(new FluidStack(Fluids.WATER, 10))
                .result(new ItemStack(Items.MAGMA_CREAM, 4))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(BringTheTide.MODID, "erosion_magmablock"));
        ErosionRecipeBuilder.customRecipe(erosionSerializer)
                .ingredient(Ingredient.of(Items.GLOWSTONE))
                .fluid(new FluidStack(Fluids.WATER, 10))
                .result(new ItemStack(Items.GLOWSTONE_DUST, 4))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(BringTheTide.MODID, "erosion_glowstone"));
        ErosionRecipeBuilder.customRecipe(erosionSerializer)
                .ingredient(Ingredient.of(Items.MELON))
                .fluid(new FluidStack(Fluids.WATER, 10))
                .result(new ItemStack(Items.MELON_SLICE, 9))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(BringTheTide.MODID, "erosion_melon"));

        RecipeSerializer<?> depositionSerializer = TideRecipes.DEPOSITION_SERIALIZER.get();
        DepositionRecipeBuilder.customRecipe(depositionSerializer)
                .mainIngredient(Ingredient.of(Items.COPPER_INGOT))
                .catalyst(Ingredient.of(Items.NAUTILUS_SHELL))
                .result(new ItemStack(TideItems.SEASHELL_ALLOY_INGOT.get(), 1))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(BringTheTide.MODID, "deposition_seashellalloyingot"));
        DepositionRecipeBuilder.customRecipe(depositionSerializer)
                .mainIngredient(Ingredient.of(Items.LEATHER))
                .catalyst(Ingredient.of(Items.KELP))
                .catalyst(Ingredient.of(Items.BONE_MEAL))
                .result(new ItemStack(TideItems.SEABOUND_SKIN.get(), 1))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(BringTheTide.MODID, "deposition_seaboundskin"));

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, TideItems.COSTUME_HELMET.get())
                .pattern("xxx")
                .pattern("x x")
                .pattern("   ")
                .define('x', TideItems.SEABOUND_SKIN.get())
                .unlockedBy("has_skins", InventoryChangeTrigger.TriggerInstance.hasItems(
                        TideItems.SEABOUND_SKIN.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, TideItems.COSTUME_CHESTPLATE.get())
                .pattern("x x")
                .pattern("xxx")
                .pattern("xxx")
                .define('x', TideItems.SEABOUND_SKIN.get())
                .unlockedBy("has_skins", InventoryChangeTrigger.TriggerInstance.hasItems(
                        TideItems.SEABOUND_SKIN.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, TideItems.COSTUME_LEGGINGS.get())
                .pattern("xxx")
                .pattern("x x")
                .pattern("x x")
                .define('x', TideItems.SEABOUND_SKIN.get())
                .unlockedBy("has_skins", InventoryChangeTrigger.TriggerInstance.hasItems(
                        TideItems.SEABOUND_SKIN.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, TideItems.COSTUME_BOOTS.get())
                .pattern("x x")
                .pattern("x x")
                .pattern("   ")
                .define('x', TideItems.SEABOUND_SKIN.get())
                .unlockedBy("has_skins", InventoryChangeTrigger.TriggerInstance.hasItems(
                        TideItems.SEABOUND_SKIN.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, TideItems.DRIFTWOOD_WAND.get())
                .pattern("  x")
                .pattern(" x ")
                .pattern("x  ")
                .define('x', TideItems.DRIFTWOOD_LOG_ITEM.get())
                .unlockedBy("has_driftwood", InventoryChangeTrigger.TriggerInstance.hasItems(
                        TideItems.DRIFTWOOD_LOG_ITEM.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, TideItems.EROSION_CORE_ITEM.get())
                .pattern("---")
                .pattern("GBG")
                .pattern("---")
                .define('-', Items.SANDSTONE_SLAB)
                .define('G', Items.GRAVEL)
                .define('B', Items.WATER_BUCKET)
                .unlockedBy("has_gravel", InventoryChangeTrigger.TriggerInstance.hasItems(
                        Items.GRAVEL))
                .save(consumer);
    };
}
