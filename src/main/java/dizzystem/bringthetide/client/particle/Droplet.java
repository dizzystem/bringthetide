package dizzystem.bringthetide.client.particle;

import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class Droplet extends TextureSheetParticle {
    private final SpriteSet spriteSet;
    private final float uo;
    private final float vo;

    public Droplet(ClientLevel level, double x, double y, double z, SpriteSet spriteSet, DropletParticleType type,
                   double xSpeed, double ySpeed, double zSpeed){
        super(level, x, y, z);
        this.spriteSet = spriteSet;
        this.lifetime = 10;
        this.scale(0.5f);
        this.xd = xSpeed;
        this.yd = ySpeed;
        this.zd = zSpeed;

        FluidStack fluidStack = type.getFluid();
//        ResourceLocation stillTexture = IClientFluidTypeExtensions.of(fluidStack.getFluid())
//                .getStillTexture(fluidStack);
//        TextureAtlasSprite sprite = Objects.requireNonNull(Minecraft.getInstance().getTextureAtlas(
//                TextureAtlas.LOCATION_BLOCKS
//        ).apply(stillTexture));
//        this.setSprite(sprite);
//        this.quadSize /= 2.0F;
        this.uo = this.random.nextFloat() * 3.0F;
        this.vo = this.random.nextFloat() * 3.0F;
        this.setSpriteFromAge(spriteSet);

        int tint = IClientFluidTypeExtensions.of(fluidStack.getFluid()).getTintColor(fluidStack);
        int red   = (tint >> 16) & 0xFF;
        int green = (tint >> 8)  & 0xFF;
        int blue  =  tint        & 0xFF;
        int alpha = (tint >> 24) & 0xFF;

        this.setColor(red / 256f, green / 256f, blue / 256f);
        this.setAlpha(alpha / 256f);
    }

//    @Override
//    protected float getU0() {
//        return this.sprite.getU((double)((this.uo + 1.0F) / 4.0F * 16.0F));
//    }
//
//    @Override
//    protected float getU1() {
//        return this.sprite.getU((double)(this.uo / 4.0F * 16.0F));
//    }
//
//    @Override
//    protected float getV0() {
//        return this.sprite.getV((double)(this.vo / 4.0F * 16.0F));
//    }
//
//    @Override
//    protected float getV1() {
//        return this.sprite.getV((double)((this.vo + 1.0F) / 4.0F * 16.0F));
//    }

    @Override
    public void tick() {
        super.tick();
        this.setSpriteFromAge(spriteSet);
        //Vec3 towardsCentre = this.centre.subtract(getPos()).normalize().scale(0.1);
        //this.move(towardsCentre.x, towardsCentre.y, towardsCentre.z);
    }

    @Override
    public @NotNull ParticleRenderType getRenderType(){
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }
}
