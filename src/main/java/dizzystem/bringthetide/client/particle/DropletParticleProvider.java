package dizzystem.bringthetide.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;

public class DropletParticleProvider implements ParticleProvider<DropletParticleType> {
    private final SpriteSet spriteSet;

    public DropletParticleProvider(SpriteSet spriteSet){
        this.spriteSet = spriteSet;
    }

    @Override
    public Particle createParticle(DropletParticleType type, ClientLevel level, double x, double y, double z,
                                   double xSpeed, double ySpeed, double zSpeed){
        return new Droplet(level, x, y, z, spriteSet, type, xSpeed, ySpeed, zSpeed);
    }
}
