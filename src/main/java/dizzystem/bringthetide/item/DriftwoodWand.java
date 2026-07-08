package dizzystem.bringthetide.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DriftwoodWand extends Wand {
    public DriftwoodWand(){
        super();
    }

    public int getWandPower(){
        return 2;
    }
}
