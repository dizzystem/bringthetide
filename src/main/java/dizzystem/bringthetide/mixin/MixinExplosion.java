package dizzystem.bringthetide.mixin;

import dizzystem.bringthetide.util.ExplosionAccessor;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

//This mixin adds getters for certain private variables that are missing them in the base class.
@Mixin(Explosion.class)
public abstract class MixinExplosion implements ExplosionAccessor {
    @Shadow @Final private ExplosionDamageCalculator damageCalculator;
    @Shadow @Final private float radius;
    @Shadow @Final private boolean fire;
    @Shadow @Final private Explosion.BlockInteraction blockInteraction;

    public ExplosionDamageCalculator getDamageCalculator(){
        return this.damageCalculator;
    }

    public float getRadius(){
        return this.radius;
    }

    public boolean getFire(){
        return this.fire;
    }

    public Explosion.BlockInteraction getBlockInteraction(){
        return this.blockInteraction;
    }
}
