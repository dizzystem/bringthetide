package dizzystem.bringthetide.item;

import dizzystem.bringthetide.BringTheTide;
import dizzystem.bringthetide.item.client.AbstractArmorItem;
import dizzystem.bringthetide.item.client.model.DolphinCostumeModel;
import dizzystem.bringthetide.item.client.provider.ArmorModelProvider;
import dizzystem.bringthetide.item.client.provider.SimpleModelProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.*;

import javax.annotation.Nullable;

public class DolphinCostumeItem extends AbstractArmorItem {
    private static final String TEXTURE_LOCATION = makeCustomTextureLocation(BringTheTide.MODID, "dolphin_costume");

    public DolphinCostumeItem(Type type){
        super(ArmorMaterials.DIAMOND, type, new Properties().rarity(Rarity.RARE));
    }

    @Override
    protected boolean withCustomModel(){
        return true;
    }

    @Override
    protected ArmorModelProvider createModelProvider() {
        return new SimpleModelProvider(DolphinCostumeModel::createBodyLayer, DolphinCostumeModel::new);
    }

    @Override
    public @Nullable String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        return TEXTURE_LOCATION;
    }
}
