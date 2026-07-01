package dizzystem.bringthetide.datagen;

import dizzystem.bringthetide.BringTheTide;
import dizzystem.bringthetide.registration.TideParticles;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ParticleDescriptionProvider;

public class TideParticleDescriptions extends ParticleDescriptionProvider {
    public TideParticleDescriptions(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, existingFileHelper);
    }

    @Override
    protected void addDescriptions(){
        sprite(TideParticles.BUBBLE.get(), ResourceLocation.fromNamespaceAndPath(BringTheTide.MODID, "bubble"));
        sprite(TideParticles.WHIRLPOOL.get(), ResourceLocation.fromNamespaceAndPath(BringTheTide.MODID, "bubble"));
        sprite(TideParticles.SPARKLE.get(), ResourceLocation.fromNamespaceAndPath(BringTheTide.MODID, "pixel"));

        spriteSet(TideParticles.SPLASH.get(),
                ResourceLocation.fromNamespaceAndPath(BringTheTide.MODID, "splash"),
                4,
                false
        );
    }
}
