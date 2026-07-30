package dizzystem.bringthetide.util;

import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;

//This mixin adds getters for certain private variables that are missing them in the base class.
public interface ExplosionAccessor {
    ExplosionDamageCalculator getDamageCalculator();

    float getRadius();

    boolean getFire();

    Explosion.BlockInteraction getBlockInteraction();
}
