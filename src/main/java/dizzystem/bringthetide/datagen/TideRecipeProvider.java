package dizzystem.bringthetide.datagen;

import dizzystem.bringthetide.BringTheTide;
import dizzystem.bringthetide.api.TideTags;
import dizzystem.bringthetide.recipe.DepositionRecipeBuilder;
import dizzystem.bringthetide.recipe.ErosionRecipeBuilder;
import dizzystem.bringthetide.recipe.PelagicRecipeBuilder;
import dizzystem.bringthetide.registration.TideItems;
import dizzystem.bringthetide.registration.TideRecipes;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;

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
                .result(new ItemStack(TideItems.DRIFTWOOD_LOG.get(), 1))
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
        ErosionRecipeBuilder.customRecipe(erosionSerializer)
                .ingredient(Ingredient.of(Items.COPPER_BLOCK))
                .fluid(new FluidStack(Fluids.WATER, 10))
                .result(new ItemStack(Items.EXPOSED_COPPER, 9))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(BringTheTide.MODID, "erosion_copperblock"));
        ErosionRecipeBuilder.customRecipe(erosionSerializer)
                .ingredient(Ingredient.of(Items.EXPOSED_COPPER))
                .fluid(new FluidStack(Fluids.WATER, 10))
                .result(new ItemStack(Items.WEATHERED_COPPER))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(BringTheTide.MODID, "erosion_exposedcopper"));
        ErosionRecipeBuilder.customRecipe(erosionSerializer)
                .ingredient(Ingredient.of(Items.WEATHERED_COPPER))
                .fluid(new FluidStack(Fluids.WATER, 10))
                .result(new ItemStack(Items.OXIDIZED_COPPER))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(BringTheTide.MODID, "erosion_weatheredcopper"));
        ErosionRecipeBuilder.customRecipe(erosionSerializer)
                .ingredient(Ingredient.of(Items.CUT_COPPER))
                .fluid(new FluidStack(Fluids.WATER, 10))
                .result(new ItemStack(Items.EXPOSED_CUT_COPPER))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(BringTheTide.MODID, "erosion_cutcopper"));
        ErosionRecipeBuilder.customRecipe(erosionSerializer)
                .ingredient(Ingredient.of(Items.EXPOSED_CUT_COPPER))
                .fluid(new FluidStack(Fluids.WATER, 10))
                .result(new ItemStack(Items.WEATHERED_CUT_COPPER))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(BringTheTide.MODID, "erosion_exposedcutcopper"));
        ErosionRecipeBuilder.customRecipe(erosionSerializer)
                .ingredient(Ingredient.of(Items.WEATHERED_CUT_COPPER))
                .fluid(new FluidStack(Fluids.WATER, 10))
                .result(new ItemStack(Items.OXIDIZED_CUT_COPPER))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(BringTheTide.MODID, "erosion_weatheredcutcopper"));
        ErosionRecipeBuilder.customRecipe(erosionSerializer)
                .ingredient(Ingredient.of(Items.CUT_COPPER_SLAB))
                .fluid(new FluidStack(Fluids.WATER, 10))
                .result(new ItemStack(Items.EXPOSED_CUT_COPPER_SLAB))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(BringTheTide.MODID, "erosion_cutcopperslab"));
        ErosionRecipeBuilder.customRecipe(erosionSerializer)
                .ingredient(Ingredient.of(Items.EXPOSED_CUT_COPPER_SLAB))
                .fluid(new FluidStack(Fluids.WATER, 10))
                .result(new ItemStack(Items.WEATHERED_CUT_COPPER_SLAB))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(BringTheTide.MODID, "erosion_exposedcutcopperslab"));
        ErosionRecipeBuilder.customRecipe(erosionSerializer)
                .ingredient(Ingredient.of(Items.WEATHERED_CUT_COPPER_SLAB))
                .fluid(new FluidStack(Fluids.WATER, 10))
                .result(new ItemStack(Items.OXIDIZED_CUT_COPPER_SLAB))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(BringTheTide.MODID, "erosion_weatheredcutcopperslab"));
        ErosionRecipeBuilder.customRecipe(erosionSerializer)
                .ingredient(Ingredient.of(Items.CUT_COPPER_STAIRS))
                .fluid(new FluidStack(Fluids.WATER, 10))
                .result(new ItemStack(Items.EXPOSED_CUT_COPPER_STAIRS))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(BringTheTide.MODID, "erosion_cutcopperstairs"));
        ErosionRecipeBuilder.customRecipe(erosionSerializer)
                .ingredient(Ingredient.of(Items.EXPOSED_CUT_COPPER_STAIRS))
                .fluid(new FluidStack(Fluids.WATER, 10))
                .result(new ItemStack(Items.WEATHERED_CUT_COPPER_STAIRS))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(BringTheTide.MODID, "erosion_exposedcutcopperstairs"));
        ErosionRecipeBuilder.customRecipe(erosionSerializer)
                .ingredient(Ingredient.of(Items.WEATHERED_CUT_COPPER_STAIRS))
                .fluid(new FluidStack(Fluids.WATER, 10))
                .result(new ItemStack(Items.OXIDIZED_CUT_COPPER_STAIRS))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(BringTheTide.MODID, "erosion_weatheredcutcopperstairs"));
        ErosionRecipeBuilder.customRecipe(erosionSerializer)
                .ingredient(Ingredient.of(Items.BRAIN_CORAL_BLOCK))
                .fluid(new FluidStack(Fluids.WATER, 10))
                .result(new ItemStack(Items.BRAIN_CORAL, 4))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(BringTheTide.MODID, "erosion_braincoralblock"));
        ErosionRecipeBuilder.customRecipe(erosionSerializer)
                .ingredient(Ingredient.of(Items.BUBBLE_CORAL_BLOCK))
                .fluid(new FluidStack(Fluids.WATER, 10))
                .result(new ItemStack(Items.BUBBLE_CORAL, 4))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(BringTheTide.MODID, "erosion_bubblecoralblock"));
        ErosionRecipeBuilder.customRecipe(erosionSerializer)
                .ingredient(Ingredient.of(Items.FIRE_CORAL_BLOCK))
                .fluid(new FluidStack(Fluids.WATER, 10))
                .result(new ItemStack(Items.FIRE_CORAL, 4))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(BringTheTide.MODID, "erosion_firecoralblock"));
        ErosionRecipeBuilder.customRecipe(erosionSerializer)
                .ingredient(Ingredient.of(Items.HORN_CORAL_BLOCK))
                .fluid(new FluidStack(Fluids.WATER, 10))
                .result(new ItemStack(Items.HORN_CORAL, 4))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(BringTheTide.MODID, "erosion_horncoralblock"));
        ErosionRecipeBuilder.customRecipe(erosionSerializer)
                .ingredient(Ingredient.of(Items.TUBE_CORAL_BLOCK))
                .fluid(new FluidStack(Fluids.WATER, 10))
                .result(new ItemStack(Items.TUBE_CORAL, 4))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(BringTheTide.MODID, "erosion_tubecoralblock"));
        ErosionRecipeBuilder.customRecipe(erosionSerializer)
                .ingredient(Ingredient.of(Items.BRAIN_CORAL))
                .fluid(new FluidStack(Fluids.WATER, 10))
                .result(new ItemStack(Items.BRAIN_CORAL_FAN, 1))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(BringTheTide.MODID, "erosion_braincoral"));
        ErosionRecipeBuilder.customRecipe(erosionSerializer)
                .ingredient(Ingredient.of(Items.BUBBLE_CORAL))
                .fluid(new FluidStack(Fluids.WATER, 10))
                .result(new ItemStack(Items.BUBBLE_CORAL_FAN, 1))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(BringTheTide.MODID, "erosion_bubblecoral"));
        ErosionRecipeBuilder.customRecipe(erosionSerializer)
                .ingredient(Ingredient.of(Items.FIRE_CORAL))
                .fluid(new FluidStack(Fluids.WATER, 10))
                .result(new ItemStack(Items.FIRE_CORAL_FAN, 1))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(BringTheTide.MODID, "erosion_firecoral"));
        ErosionRecipeBuilder.customRecipe(erosionSerializer)
                .ingredient(Ingredient.of(Items.HORN_CORAL))
                .fluid(new FluidStack(Fluids.WATER, 10))
                .result(new ItemStack(Items.HORN_CORAL_FAN, 1))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(BringTheTide.MODID, "erosion_horncoral"));
        ErosionRecipeBuilder.customRecipe(erosionSerializer)
                .ingredient(Ingredient.of(Items.TUBE_CORAL))
                .fluid(new FluidStack(Fluids.WATER, 10))
                .result(new ItemStack(Items.TUBE_CORAL_FAN, 1))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(BringTheTide.MODID, "erosion_tubecoral"));
        ErosionRecipeBuilder.customRecipe(erosionSerializer)
                .ingredient(Ingredient.of(Items.COBBLESTONE))
                .fluid(new FluidStack(Fluids.WATER, 10))
                .result(new ItemStack(Items.GRAVEL, 1))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(BringTheTide.MODID, "erosion_cobblestone"));
        ErosionRecipeBuilder.customRecipe(erosionSerializer)
                .ingredient(Ingredient.of(Items.GRAVEL))
                .fluid(new FluidStack(Fluids.WATER, 10))
                .result(new ItemStack(Items.SAND, 1))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(BringTheTide.MODID, "erosion_gravel"));
        ErosionRecipeBuilder.customRecipe(erosionSerializer)
                .ingredient(Ingredient.of(Items.COARSE_DIRT))
                .fluid(new FluidStack(Fluids.WATER, 10))
                .result(new ItemStack(Items.DIRT, 1))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(BringTheTide.MODID, "erosion_coarsedirt"));

        RecipeSerializer<?> depositionSerializer = TideRecipes.DEPOSITION_SERIALIZER.get();
        DepositionRecipeBuilder.customRecipe(depositionSerializer)
                .mainIngredient(Ingredient.of(Items.COPPER_INGOT))
                .catalyst(Ingredient.of(Items.NAUTILUS_SHELL))
                .result(new ItemStack(TideItems.SEASHELL_ALLOY_INGOT.get(), 1))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(BringTheTide.MODID, "deposition_seashellalloyingot"));
        DepositionRecipeBuilder.customRecipe(depositionSerializer)
                .mainIngredient(Ingredient.of(Items.IRON_INGOT))
                .catalyst(Ingredient.of(Items.SCUTE))
                .catalyst(Ingredient.of(Items.SEAGRASS))
                .result(new ItemStack(TideItems.TURTLE_ALLOY_INGOT.get(), 1))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(BringTheTide.MODID, "deposition_turtlealloyingot"));
        DepositionRecipeBuilder.customRecipe(depositionSerializer)
                .mainIngredient(Ingredient.of(Items.GOLD_INGOT))
                .catalyst(Ingredient.of(Items.COD))
                .catalyst(Ingredient.of(Items.SALMON))
                .catalyst(Ingredient.of(Items.PUFFERFISH))
                .result(new ItemStack(TideItems.FISH_ALLOY_INGOT.get(), 1))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(BringTheTide.MODID, "deposition_fishalloyingot"));
        DepositionRecipeBuilder.customRecipe(depositionSerializer)
                .mainIngredient(Ingredient.of(Items.LEATHER))
                .catalyst(Ingredient.of(Items.KELP))
                .catalyst(Ingredient.of(Items.BONE_MEAL))
                .result(new ItemStack(TideItems.SEABOUND_SKIN.get(), 1))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(BringTheTide.MODID, "deposition_seaboundskin"));
        DepositionRecipeBuilder.customRecipe(depositionSerializer)
                .mainIngredient(Ingredient.of(Items.POISONOUS_POTATO))
                .catalyst(Ingredient.of(Items.KELP))
                .catalyst(Ingredient.of(Items.KELP))
                .result(new ItemStack(Items.SLIME_BALL, 1))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(BringTheTide.MODID, "deposition_slimeball"));
        DepositionRecipeBuilder.customRecipe(depositionSerializer)
                .mainIngredient(Ingredient.of(Items.COBBLESTONE))
                .catalyst(Ingredient.of(Items.PINK_DYE))
                .catalyst(Ingredient.of(Items.BONE_MEAL))
                .result(new ItemStack(Items.BRAIN_CORAL_BLOCK, 1))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(BringTheTide.MODID, "deposition_braincoral"));
        DepositionRecipeBuilder.customRecipe(depositionSerializer)
                .mainIngredient(Ingredient.of(Items.COBBLESTONE))
                .catalyst(Ingredient.of(Items.MAGENTA_DYE))
                .catalyst(Ingredient.of(Items.BONE_MEAL))
                .result(new ItemStack(Items.BUBBLE_CORAL_BLOCK, 1))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(BringTheTide.MODID, "deposition_bubblecoral"));
        DepositionRecipeBuilder.customRecipe(depositionSerializer)
                .mainIngredient(Ingredient.of(Items.COBBLESTONE))
                .catalyst(Ingredient.of(Items.RED_DYE))
                .catalyst(Ingredient.of(Items.BONE_MEAL))
                .result(new ItemStack(Items.FIRE_CORAL_BLOCK, 1))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(BringTheTide.MODID, "deposition_firecoral"));
        DepositionRecipeBuilder.customRecipe(depositionSerializer)
                .mainIngredient(Ingredient.of(Items.COBBLESTONE))
                .catalyst(Ingredient.of(Items.YELLOW_DYE))
                .catalyst(Ingredient.of(Items.BONE_MEAL))
                .result(new ItemStack(Items.HORN_CORAL_BLOCK, 1))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(BringTheTide.MODID, "deposition_horncoral"));
        DepositionRecipeBuilder.customRecipe(depositionSerializer)
                .mainIngredient(Ingredient.of(Items.COBBLESTONE))
                .catalyst(Ingredient.of(Items.BLUE_DYE))
                .catalyst(Ingredient.of(Items.BONE_MEAL))
                .result(new ItemStack(Items.TUBE_CORAL_BLOCK, 1))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(BringTheTide.MODID, "deposition_tubecoral"));

        RecipeSerializer<?> pelagicSerializer = TideRecipes.PELAGIC_SERIALIZER.get();
        PelagicRecipeBuilder.customRecipe(pelagicSerializer)
                        .ingredient(Ingredient.of(Items.ROTTEN_FLESH))
                .result(new ItemStack(Items.DROWNED_SPAWN_EGG, 2))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(BringTheTide.MODID, "pelagic_drowned"));
        PelagicRecipeBuilder.customRecipe(pelagicSerializer)
                        .ingredient(Ingredient.of(Items.ROTTEN_FLESH))
                        .ingredient(Ingredient.of(Items.BREAD))
                .result(new ItemStack(Items.ZOMBIE_VILLAGER_SPAWN_EGG))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(BringTheTide.MODID, "pelagic_zombievillager"));
        PelagicRecipeBuilder.customRecipe(pelagicSerializer)
                        .ingredient(Ingredient.of(Items.COD))
                .result(new ItemStack(Items.GUARDIAN_SPAWN_EGG, 2))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(BringTheTide.MODID, "pelagic_guardian"));
        PelagicRecipeBuilder.customRecipe(pelagicSerializer)
                        .ingredient(Ingredient.of(Items.COD))
                        .ingredient(Ingredient.of(Items.PRISMARINE_SHARD))
                .result(new ItemStack(Items.ELDER_GUARDIAN_SPAWN_EGG))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(BringTheTide.MODID, "pelagic_elderguardian"));
        PelagicRecipeBuilder.customRecipe(pelagicSerializer)
                        .ingredient(Ingredient.of(Items.INK_SAC))
                .result(new ItemStack(Items.SQUID_SPAWN_EGG))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(BringTheTide.MODID, "pelagic_squid"));
        PelagicRecipeBuilder.customRecipe(pelagicSerializer)
                        .ingredient(Ingredient.of(Items.SEAGRASS))
                .result(new ItemStack(Items.TURTLE_SPAWN_EGG))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(BringTheTide.MODID, "pelagic_turtle"));
        PelagicRecipeBuilder.customRecipe(pelagicSerializer)
                .ingredient(Ingredient.of(Items.SLIME_BALL))
                .result(new ItemStack(Items.SLIME_SPAWN_EGG))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(BringTheTide.MODID, "pelagic_slime"));

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
                .define('x', TideItems.DRIFTWOOD_LOG.get())
                .unlockedBy("has_driftwood", InventoryChangeTrigger.TriggerInstance.hasItems(
                        TideItems.DRIFTWOOD_LOG.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, TideItems.TANK.get())
                .pattern("GS")
                .define('G', Items.GLASS)
                .define('S', Items.PRISMARINE_BRICK_SLAB)
                .unlockedBy("has_glass", InventoryChangeTrigger.TriggerInstance.hasItems(
                        Items.GLASS))
                .save(consumer);

        //sandstone cores
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, TideItems.EROSION_CORE.get())
                .pattern("---")
                .pattern("FBF")
                .pattern("---")
                .define('-', Items.SANDSTONE_SLAB)
                .define('F', TideItems.FISH_ALLOY_INGOT.get())
                .define('B', Items.WATER_BUCKET)
                .unlockedBy("has_fish_alloy", InventoryChangeTrigger.TriggerInstance.hasItems(
                        TideItems.FISH_ALLOY_INGOT.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, TideItems.PELAGIC_CORE.get())
                .pattern("---")
                .pattern("CBC")
                .pattern("---")
                .define('-', Items.SANDSTONE_SLAB)
                .define('C', Items.COPPER_INGOT)
                .define('B', Items.ROTTEN_FLESH)
                .unlockedBy("has_copper", InventoryChangeTrigger.TriggerInstance.hasItems(
                        Items.COPPER_INGOT))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, TideItems.SEABED_CORE.get())
                .pattern("---")
                .pattern("FIF")
                .pattern("---")
                .define('-', Items.SANDSTONE_SLAB)
                .define('F', TideItems.FISH_ALLOY_INGOT.get())
                .define('I', Items.IRON_HOE)
                .unlockedBy("has_fish_alloy", InventoryChangeTrigger.TriggerInstance.hasItems(
                        TideItems.FISH_ALLOY_INGOT.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, TideItems.SINKHOLE_CORE.get())
                .pattern("---")
                .pattern("FIF")
                .pattern("---")
                .define('-', Items.SANDSTONE_SLAB)
                .define('F', TideItems.FISH_ALLOY_INGOT.get())
                .define('I', Items.IRON_PICKAXE)
                .unlockedBy("has_fish_alloy", InventoryChangeTrigger.TriggerInstance.hasItems(
                        TideItems.FISH_ALLOY_INGOT.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, TideItems.VORTEX_CORE.get())
                .pattern("---")
                .pattern("FBF")
                .pattern("---")
                .define('-', Items.SANDSTONE_SLAB)
                .define('F', TideItems.FISH_ALLOY_INGOT.get())
                .define('B', Items.IRON_SWORD)
                .unlockedBy("has_fish_alloy", InventoryChangeTrigger.TriggerInstance.hasItems(
                        TideItems.FISH_ALLOY_INGOT.get()))
                .save(consumer);

        //prismarine cores
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, TideItems.BASIN_CORE.get())
                .pattern("---")
                .pattern("TBT")
                .pattern("---")
                .define('-', Items.PRISMARINE_BRICK_SLAB)
                .define('T', TideItems.TURTLE_ALLOY_INGOT.get())
                .define('B', Items.GLASS_BOTTLE)
                .unlockedBy("has_turtle_alloy", InventoryChangeTrigger.TriggerInstance.hasItems(
                        TideItems.TURTLE_ALLOY_INGOT.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, TideItems.CURRENT_CORE.get())
                .pattern("---")
                .pattern("THT")
                .pattern("---")
                .define('-', Items.PRISMARINE_BRICK_SLAB)
                .define('T', TideItems.TURTLE_ALLOY_INGOT.get())
                .define('H', Items.HOPPER)
                .unlockedBy("has_turtle_alloy", InventoryChangeTrigger.TriggerInstance.hasItems(
                        TideItems.TURTLE_ALLOY_INGOT.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, TideItems.DEPOSITION_CORE.get())
                .pattern("---")
                .pattern("CDC")
                .pattern("---")
                .define('-', Items.PRISMARINE_BRICK_SLAB)
                .define('D', TideItems.DRIFTWOOD_LOG.get())
                .define('C', Items.COPPER_INGOT)
                .unlockedBy("has_copper", InventoryChangeTrigger.TriggerInstance.hasItems(
                        Items.COPPER_INGOT))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, TideItems.REEF_CORE.get())
                .pattern("---")
                .pattern("TST")
                .pattern("---")
                .define('-', Items.PRISMARINE_BRICK_SLAB)
                .define('T', TideItems.TURTLE_ALLOY_INGOT.get())
                .define('S', Items.SEAGRASS)
                .unlockedBy("has_turtle_alloy", InventoryChangeTrigger.TriggerInstance.hasItems(
                        TideItems.TURTLE_ALLOY_INGOT.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, TideItems.TRAWL_CORE.get())
                .pattern("---")
                .pattern("CFC")
                .pattern("---")
                .define('-', Items.PRISMARINE_BRICK_SLAB)
                .define('F', Items.FISHING_ROD)
                .define('C', Items.COPPER_INGOT)
                .unlockedBy("has_copper", InventoryChangeTrigger.TriggerInstance.hasItems(
                        Items.COPPER_INGOT))
                .save(consumer);
//        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, TideItems.WHIRLPOOL_CORE_ITEM.get())
//                .pattern("---")
//                .pattern("DBD")
//                .pattern("---")
//                .define('-', Items.PRISMARINE_BRICK_SLAB)
//                .define('D', TideItems.DRIFTWOOD_LOG_ITEM.get())
//                .define('B', Items.COPPER_INGOT)
//                .unlockedBy("has_gravel", InventoryChangeTrigger.TriggerInstance.hasItems(
//                        Items.GRAVEL))
//                .save(consumer);

        //upgrades
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, TideItems.FLOW_UPGRADE_.get())
                .pattern("---")
                .pattern("SPS")
                .pattern("---")
                .define('-', Items.OXIDIZED_CUT_COPPER_SLAB)
                .define('P', Items.PRISMARINE_SHARD)
                .define('S', TideItems.SEASHELL_ALLOY_INGOT.get())
                .unlockedBy("has_seashell_alloy", InventoryChangeTrigger.TriggerInstance.hasItems(
                        TideItems.SEASHELL_ALLOY_INGOT.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, TideItems.BOUNTY_UPGRADE.get())
                .pattern("---")
                .pattern("SPS")
                .pattern("---")
                .define('-', Items.OXIDIZED_CUT_COPPER_SLAB)
                .define('P', Items.PRISMARINE_CRYSTALS)
                .define('S', TideItems.SEASHELL_ALLOY_INGOT.get())
                .unlockedBy("has_seashell_alloy", InventoryChangeTrigger.TriggerInstance.hasItems(
                        TideItems.SEASHELL_ALLOY_INGOT.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, TideItems.RANGE_UPGRADE.get())
                .pattern("---")
                .pattern("SIS")
                .pattern("---")
                .define('-', Items.OXIDIZED_CUT_COPPER_SLAB)
                .define('I', Items.INK_SAC)
                .define('S', TideItems.SEASHELL_ALLOY_INGOT.get())
                .unlockedBy("has_seashell_alloy", InventoryChangeTrigger.TriggerInstance.hasItems(
                        TideItems.SEASHELL_ALLOY_INGOT.get()))
                .save(consumer);

        //driftwood furniture set
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, TideItems.DRIFTWOOD_PLANKS.get(), 4)
                .requires(TideItems.DRIFTWOOD_LOG.get())
                .unlockedBy("has_driftwood", InventoryChangeTrigger.TriggerInstance.hasItems(
                        TideItems.DRIFTWOOD_LOG.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, TideItems.DRIFTWOOD_STAIRS.get(), 4)
                .pattern("D  ")
                .pattern("DD ")
                .pattern("DDD")
                .define('D', TideItems.DRIFTWOOD_PLANKS.get())
                .unlockedBy("has_driftwood_planks", InventoryChangeTrigger.TriggerInstance.hasItems(
                        TideItems.DRIFTWOOD_PLANKS.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, TideItems.DRIFTWOOD_SLAB.get(), 6)
                .pattern("DDD")
                .define('D', TideItems.DRIFTWOOD_PLANKS.get())
                .unlockedBy("has_driftwood_planks", InventoryChangeTrigger.TriggerInstance.hasItems(
                        TideItems.DRIFTWOOD_PLANKS.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, TideItems.DRIFTWOOD_FENCE.get(), 3)
                .pattern("DSD")
                .pattern("DSD")
                .define('D', TideItems.DRIFTWOOD_PLANKS.get())
                .define('S', Items.STICK)
                .unlockedBy("has_driftwood_planks", InventoryChangeTrigger.TriggerInstance.hasItems(
                        TideItems.DRIFTWOOD_PLANKS.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, TideItems.DRIFTWOOD_FENCE_GATE.get())
                .pattern("SDS")
                .pattern("SDS")
                .define('D', TideItems.DRIFTWOOD_PLANKS.get())
                .define('S', Items.STICK)
                .unlockedBy("has_driftwood_planks", InventoryChangeTrigger.TriggerInstance.hasItems(
                        TideItems.DRIFTWOOD_PLANKS.get()))
                .save(consumer);
//        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, TideItems.DRIFTWOOD_SIGN_ITEM.get(), 3)
//                .pattern("DDD")
//                .pattern("DDD")
//                .pattern(" S ")
//                .define('D', TideItems.DRIFTWOOD_PLANKS_ITEM.get())
//                .define('S', Items.STICK)
//                .unlockedBy("has_driftwood_planks", InventoryChangeTrigger.TriggerInstance.hasItems(
//                        TideItems.DRIFTWOOD_PLANKS_ITEM.get()))
//                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, TideItems.DRIFTWOOD_PRESSURE_PLATE.get())
                .pattern("DD")
                .define('D', TideItems.DRIFTWOOD_PLANKS.get())
                .unlockedBy("has_driftwood_planks", InventoryChangeTrigger.TriggerInstance.hasItems(
                        TideItems.DRIFTWOOD_PLANKS.get()))
                .save(consumer);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, TideItems.DRIFTWOOD_BUTTON.get())
                .requires(TideItems.DRIFTWOOD_PLANKS.get())
                .unlockedBy("has_driftwood_planks", InventoryChangeTrigger.TriggerInstance.hasItems(
                        TideItems.DRIFTWOOD_PLANKS.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, TideItems.DRIFTWOOD_COLUMN.get(), 3)
                .pattern("D")
                .pattern("D")
                .pattern("D")
                .define('D', TideItems.DRIFTWOOD_PLANKS.get())
                .unlockedBy("has_driftwood_planks", InventoryChangeTrigger.TriggerInstance.hasItems(
                        TideItems.DRIFTWOOD_PLANKS.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, TideItems.DRIFTWOOD_DOOR.get(), 3)
                .pattern("DD")
                .pattern("DD")
                .pattern("DD")
                .define('D', TideItems.DRIFTWOOD_PLANKS.get())
                .unlockedBy("has_driftwood_planks", InventoryChangeTrigger.TriggerInstance.hasItems(
                        TideItems.DRIFTWOOD_PLANKS.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, TideItems.DRIFTWOOD_TRAPDOOR.get(), 2)
                .pattern("DDD")
                .pattern("DDD")
                .define('D', TideItems.DRIFTWOOD_PLANKS.get())
                .unlockedBy("has_driftwood_planks", InventoryChangeTrigger.TriggerInstance.hasItems(
                        TideItems.DRIFTWOOD_PLANKS.get()))
                .save(consumer);

        //prismarine furniture set
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, TideItems.PRISMARINE_PILLAR.get(), 3)
                .pattern("B")
                .pattern("B")
                .pattern("B")
                .define('B', Items.PRISMARINE_BRICKS)
                .unlockedBy("has_prismarine_bricks", InventoryChangeTrigger.TriggerInstance.hasItems(
                        Items.PRISMARINE_BRICKS))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, TideItems.EXPLOSION_ROD.get())
                .pattern("S")
                .pattern("S")
                .pattern("S")
                .define('S', TideItems.SEASHELL_ALLOY_INGOT.get())
                .unlockedBy("has_seashell_alloy", InventoryChangeTrigger.TriggerInstance.hasItems(
                        TideItems.SEASHELL_ALLOY_INGOT.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, TideItems.EFFIGY.get())
                .pattern("DTD")
                .pattern(" T ")
                .pattern("DSD")
                .define('D', TideItems.DRIFTWOOD_PLANKS.get())
                .define('T', TideItems.TURTLE_ALLOY_INGOT.get())
                .define('S', Items.SMOOTH_STONE)
                .unlockedBy("has_driftwood_planks", InventoryChangeTrigger.TriggerInstance.hasItems(
                        TideItems.DRIFTWOOD_PLANKS.get()))
                .save(consumer);
    };
}
