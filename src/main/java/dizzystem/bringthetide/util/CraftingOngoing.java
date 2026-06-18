package dizzystem.bringthetide.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.crafting.Recipe;

import java.util.ArrayList;

public record CraftingOngoing(ArrayList<BlockPos> corePosses, Recipe<?> recipe){}

