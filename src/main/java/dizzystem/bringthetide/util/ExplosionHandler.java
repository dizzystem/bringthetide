package dizzystem.bringthetide.util;

import com.mojang.logging.LogUtils;
import dizzystem.bringthetide.block.ExplosionRod;
import dizzystem.bringthetide.registration.TideBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

public class ExplosionHandler {
    private static final int PROTECTION_RADIUS = 128;
    private static final Map<Level, ExplosionHandler.SaveData> dims = new HashMap<>();
    private static double explosionX, explosionY, explosionZ;

    public static ExplosionHandler.SaveData getDataForLevel(ServerLevel level){
        if (!dims.containsKey(level) || dims.get(level) == null){
            ExplosionHandler.SaveData data = level.getDataStorage().computeIfAbsent(ExplosionHandler::loadSaveData,
                    ExplosionHandler::createSaveData, "explosionRods");
            dims.put(level, data);
        }
        return dims.get(level);
    }

    public static void registerExplosionRod(ServerLevel level, BlockPos blockPos){
        ExplosionHandler.SaveData data = getDataForLevel(level);
        data.addRod(blockPos);
        data.setDirty();
    }

    public static void deregisterExplosionRod(ServerLevel level, BlockPos blockPos){
        ExplosionHandler.SaveData data = getDataForLevel(level);
        data.removeRod(blockPos);
        data.setDirty();
    }

    public static Set<BlockPos> getExplosionRods(ServerLevel level){
        ExplosionHandler.SaveData data = getDataForLevel(level);
        return data.getRods();
    }

    public static ExplosionHandler.SaveData createSaveData(){
        return new ExplosionHandler.SaveData(null);
    }

    public static ExplosionHandler.SaveData loadSaveData(CompoundTag tag){
        Set<BlockPos> rods = Arrays.stream(tag.getLongArray("explosionRods")).mapToObj(BlockPos::of)
                .collect(Collectors.toCollection(HashSet::new));
        return new ExplosionHandler.SaveData(rods);
    }

    //Redirects explosions if they're in range of an explosion rod.
    public static void onExplosion(ServerLevel level, double x, double y, double z){
        Vec3 position = new Vec3(x, y, z);
        ArrayList<BlockPos> toRemove = new ArrayList<>();

        //If they aren't in range of a rod, default to its current location.
        explosionX = x;
        explosionY = y;
        explosionZ = z;

        ExplosionHandler.SaveData data = getDataForLevel(level);
        for (BlockPos rodPos : data.getRods()){
            if (!level.getBlockState(rodPos).is(TideBlocks.EXPLOSION_ROD.get())){
                toRemove.add(rodPos);
                continue;
            }

            if (rodPos.distSqr(BlockPos.containing(position)) <= PROTECTION_RADIUS * PROTECTION_RADIUS){
                Direction facing = level.getBlockState(rodPos).getValue(ExplosionRod.FACING);
                Vec3 newExplosionPos = rodPos.relative(facing).getCenter();

                explosionX = newExplosionPos.x;
                explosionY = newExplosionPos.y;
                explosionZ = newExplosionPos.z;

                LogUtils.getLogger().info("explosion redirected to {}", newExplosionPos);
                break;
            }
        }

        toRemove.forEach(data::removeRod);
    }

    public static double getLastExplosionX(){
        return explosionX;
    }

    public static double getLastExplosionY(){
        return explosionY;
    }

    public static double getLastExplosionZ(){
        return explosionZ;
    }

    //Save data class, instanced per dimension.
    public static class SaveData extends SavedData {
        private final Set<BlockPos> rods;

        public SaveData(Set<BlockPos> rods){
            if (rods == null){
                this.rods = new HashSet<>();
            } else {
                this.rods = rods;
            }
        }

        public void addRod(BlockPos blockPos){
            this.rods.add(blockPos);
        }

        public void removeRod(BlockPos blockPos){
            this.rods.remove(blockPos);
        }

        public Set<BlockPos> getRods(){
            return Set.copyOf(rods);
        }

        @Override
        public CompoundTag save(CompoundTag tag) {
            tag.putLongArray("explosionRods",
                    rods.stream().map(BlockPos::asLong).collect((Collectors.toList())));
            return tag;
        }
    }
}
