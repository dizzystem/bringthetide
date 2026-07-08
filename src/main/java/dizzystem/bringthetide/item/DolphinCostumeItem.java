package dizzystem.bringthetide.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import dizzystem.bringthetide.BringTheTide;
import dizzystem.bringthetide.item.client.AbstractArmorItem;
import dizzystem.bringthetide.item.client.model.DolphinCostumeModel;
import dizzystem.bringthetide.item.client.provider.ArmorModelProvider;
import dizzystem.bringthetide.item.client.provider.SimpleModelProvider;
import dizzystem.bringthetide.registration.TideItems;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.EnumMap;
import java.util.List;
import java.util.UUID;

public class DolphinCostumeItem extends AbstractArmorItem {
    private static final String TEXTURE_LOCATION = makeCustomTextureLocation(BringTheTide.MODID, "dolphin_costume");
    private static final EnumMap<Type, UUID> ARMOR_MODIFIER_UUID_PER_TYPE = Util.make(new EnumMap<>(ArmorItem.Type.class), (p_266744_) -> {
        p_266744_.put(ArmorItem.Type.BOOTS, UUID.fromString("845DB27C-C624-495F-8C9F-6020A9A58B6B"));
        p_266744_.put(ArmorItem.Type.LEGGINGS, UUID.fromString("D8499B04-0E66-4726-AB29-64469D734E0D"));
        p_266744_.put(ArmorItem.Type.CHESTPLATE, UUID.fromString("9F3D476D-C118-4544-8365-64846904B48E"));
        p_266744_.put(ArmorItem.Type.HELMET, UUID.fromString("2AD3F246-FEE1-4E67-B886-69FD380BB150"));
    }); //copied from minecraft.world.item.ArmorItem
    private final Multimap<Attribute, AttributeModifier> defaultModifiers;

    public DolphinCostumeItem(Type type){
        super(ArmorMaterials.LEATHER, type, getArmorProperties(type));

        UUID uuid = ARMOR_MODIFIER_UUID_PER_TYPE.get(type);
        Multimap<Attribute, AttributeModifier> defaults = super.getDefaultAttributeModifiers(type.getSlot());
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        for (var entry : defaults.entries()){
            builder.put(entry);
        }
        builder.put(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier(uuid, "Armor knockback resistance",
                0.2, AttributeModifier.Operation.ADDITION));
        this.defaultModifiers = builder.build();
    }

    public static Item.Properties getArmorProperties(Type type){
        Item.Properties props = new Properties().rarity(Rarity.RARE);

        switch (type){
            case HELMET -> props.defaultDurability(11 * 33);
            case CHESTPLATE -> props.defaultDurability(16 * 33);
            case LEGGINGS -> props.defaultDurability(15 * 33);
            case BOOTS -> props.defaultDurability(13 * 33);
        }

        return props;
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

    @Override
    public int getEnchantmentValue() {
        return 25;
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean isValidRepairItem(ItemStack armor, ItemStack item) {
        return item.is(TideItems.SEABOUND_SKIN.get());
    }

    @Override
    @ParametersAreNonnullByDefault
    public @NotNull Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
        return slot == this.type.getSlot() ? this.defaultModifiers : super.getDefaultAttributeModifiers(slot);
    }

    public int getWandPower(){
        return 2;
    }

    @Override
    public void appendHoverText(ItemStack item, @org.jetbrains.annotations.Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(item, level, tooltip, flag);
        tooltip.add(Component.translatable("Wand Power: ")
                .append(((Integer) getWandPower()).toString()).withStyle(ChatFormatting.AQUA));
    }
}
