package dizzystem.bringthetide.util;

import dizzystem.bringthetide.registration.TideFluids;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.level.Level;

import java.util.HashSet;

public class PrimedTntHandler {
    public static final HashSet<PrimedTnt> trackedTnt = new HashSet<>();
    public static final HashSet<PrimedTnt> toRemove = new HashSet<>();

    public static void tntSpawned(Level level, PrimedTnt primedTnt){
        trackedTnt.add(primedTnt);
    }

    public static void levelTick(Level level){
        for (PrimedTnt tnt : trackedTnt){
            if (tnt.isRemoved()){
                toRemove.add(tnt);
                continue;
            }

            if (!tnt.onGround()){
                continue;
            }

            //grace mechanic - if they landed on the pool edge let them have it
            BlockPos on = tnt.getOnPos();
            for (Direction dir : Direction.Plane.HORIZONTAL){
                BlockPos adj = on.relative(dir);
                if (level.getBlockState(adj).getFluidState().is(TideFluids.IMBUED_SEAWATER.get())){
                    tnt.setPos(adj.getCenter());
                }
            }

            //regardless, it's landed so we can stop tracking it
            toRemove.add(tnt);
        }

        trackedTnt.removeAll(toRemove);
        toRemove.clear();
    }
}
