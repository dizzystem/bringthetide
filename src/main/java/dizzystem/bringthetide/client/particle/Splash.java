package dizzystem.bringthetide.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import org.jetbrains.annotations.NotNull;

public class Splash extends TextureSheetParticle {
    private final SpriteSet spriteSet;

    public Splash(ClientLevel level, double x, double y, double z, SpriteSet spriteSet){
        super(level, x, y, z);
        this.spriteSet = spriteSet;
        this.setColor(0.2f, 1.0f, 0.8f);
        this.gravity = 1.2f;
        this.yd = 0.4f;

        this.xd = -0.4f + this.random.nextFloat();
        this.zd = -0.4f + this.random.nextFloat();

        //initialize to prevent crash
        setSpriteFromAge(spriteSet);
    }

    @Override
    public void tick() {
        setSpriteFromAge(spriteSet);
        super.tick();
    }

    @Override
    public @NotNull ParticleRenderType getRenderType(){
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }
}
