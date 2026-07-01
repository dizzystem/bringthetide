package dizzystem.bringthetide.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.Level;

import java.util.ArrayList;

public class DroppedItemCollector {
    public static ArrayList<ItemEntity> items;
    public static Level level;
    public static boolean checking = false;

    public static void collect(Level level){
        DroppedItemCollector.items = new ArrayList<>();
        DroppedItemCollector.level = level;
        DroppedItemCollector.checking = true;
    }

    /**
     * Called from events when an item entity enters the level.
     */
    public static void itemDropped(Level level, ItemEntity item){
        if (DroppedItemCollector.checking && level.equals(DroppedItemCollector.level)){
            DroppedItemCollector.items.add(item);
        }
    }

    public static ArrayList<ItemEntity> retrieve(){
        DroppedItemCollector.checking = false;
        return DroppedItemCollector.items;
    }
}
