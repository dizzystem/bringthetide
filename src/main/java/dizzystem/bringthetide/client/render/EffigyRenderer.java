package dizzystem.bringthetide.client.render;

import dizzystem.bringthetide.BringTheTide;
import net.minecraft.client.renderer.entity.ArmorStandRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.decoration.ArmorStand;

public class EffigyRenderer extends ArmorStandRenderer {
    public EffigyRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    public ResourceLocation getTextureLocation(ArmorStand armorStand) {
        return ResourceLocation.fromNamespaceAndPath(BringTheTide.MODID, "textures/entity/effigy.png");
    }
}
