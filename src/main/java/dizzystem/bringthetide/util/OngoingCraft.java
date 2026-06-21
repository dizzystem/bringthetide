package dizzystem.bringthetide.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.crafting.Recipe;

import java.util.ArrayList;

public record OngoingCraft(ArrayList<BlockPos> corePosses, Recipe<?> recipe){}

