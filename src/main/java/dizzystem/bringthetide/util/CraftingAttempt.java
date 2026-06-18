package dizzystem.bringthetide.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.crafting.RecipeType;

public record CraftingAttempt(BlockPos corePos, RecipeType<?> recipeType){}

