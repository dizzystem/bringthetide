package dizzystem.bringthetide.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import org.jetbrains.annotations.NotNull;

public class Bubble extends TextureSheetParticle {
    private final SpriteSet spriteSet;

    public Bubble(ClientLevel level, double x, double y, double z, SpriteSet spriteSet){
        super(level, x, y, z);
        this.spriteSet = spriteSet;
        this.gravity = 0.04f;

        this.xd = (-0.5f + this.random.nextFloat()) * 0.05;
        this.zd = (-0.5f + this.random.nextFloat()) * 0.05;

        //initialize to prevent crash
        setSpriteFromAge(spriteSet);
    }

    @Override
    public void tick() {
        setSpriteFromAge(spriteSet);
        super.tick();
        this.move(0f,0.04f,0f);
    }

    @Override
    public @NotNull ParticleRenderType getRenderType(){
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }
}
