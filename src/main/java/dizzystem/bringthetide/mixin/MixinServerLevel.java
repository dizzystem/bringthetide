package dizzystem.bringthetide.mixin;

import dizzystem.bringthetide.util.ExplosionHandler;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.progress.ChunkProgressListener;
import net.minecraft.world.RandomSequences;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.*;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraft.world.level.storage.ServerLevelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.util.List;
import java.util.concurrent.Executor;

@Mixin(ServerLevel.class)
public abstract class MixinServerLevel extends Level implements WorldGenLevel {
    public MixinServerLevel(MinecraftServer p_214999_, Executor p_215000_, LevelStorageSource.LevelStorageAccess p_215001_, ServerLevelData p_215002_, ResourceKey<Level> p_215003_, LevelStem p_215004_, ChunkProgressListener p_215005_, boolean p_215006_, long p_215007_, List<CustomSpawner> p_215008_, boolean p_215009_, @Nullable RandomSequences p_288977_){
        super(p_215002_, p_215003_, p_214999_.registryAccess(), p_215004_.type(), p_214999_::getProfiler, false, p_215006_, p_215007_, p_214999_.getMaxChainedNeighborUpdates());
    }

    //Here we check whether the explosion needs to be redirected.
    @Inject(method = "explode", at = @At(value = "HEAD"))
    public void explodeProxy(@Nullable Entity entity, @Nullable DamageSource damageSource, @Nullable ExplosionDamageCalculator explosionDamageCalculator, double x, double y, double z, float radius, boolean fure, Level.ExplosionInteraction explosionInteraction, CallbackInfoReturnable<Explosion> ci){
        ExplosionHandler.onExplosion((ServerLevel) (Object) this, x, y, z);
    }

    //Below we redirect the explosion.
    @ModifyVariable(method = "explode", at = @At(value = "HEAD"), argsOnly = true, ordinal = 0)
    private double explosionX(double x){
        return ExplosionHandler.getLastExplosionX();
    }
    @ModifyVariable(method = "explode", at = @At(value = "HEAD"), argsOnly = true, ordinal = 1)
    private double explosionY(double y){
        return ExplosionHandler.getLastExplosionY();
    }
    @ModifyVariable(method = "explode", at = @At(value = "HEAD"), argsOnly = true, ordinal = 2)
    private double explosionZ(double z){
        return ExplosionHandler.getLastExplosionZ();
    }
}
