package dizzystem.bringthetide.entity;

import dizzystem.bringthetide.registration.TideItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class Effigy extends ArmorStand {
    private UUID playerId;
    private final String PLACEDBY_TAG = "placedBy";

    public Effigy(EntityType<? extends Effigy> entityType, Level level){
        super(entityType, level);
    }

    public void setPlacedBy(Player player){
        this.playerId = player.getUUID();
    }

    public Player getPlacedByPlayer(){
        if (level().isClientSide()){
            return null;
        }
        ServerLevel level = (ServerLevel) level();

        Player player = level.getServer().getPlayerList().getPlayer(this.playerId);

        if (player != null && !player.isRemoved()){
            return player;
        }

        return null;
    }

    @Override
    public CompoundTag saveWithoutId(CompoundTag tag) {
        if (this.playerId != null){
            tag.putUUID(PLACEDBY_TAG, this.playerId);
        }

        return super.saveWithoutId(tag);
    }

    @Override
    public void load(CompoundTag tag) {
        if (tag.contains(PLACEDBY_TAG)){
            this.playerId = tag.getUUID(PLACEDBY_TAG);
        }

        super.load(tag);
    }

    //Drop our effigy item instead of an armour stand item.
    @Override
    protected void brokenByPlayer(DamageSource damageSource) {
        ItemStack itemstack = new ItemStack(TideItems.EFFIGY.get());
        if (this.hasCustomName()) {
            itemstack.setHoverName(this.getCustomName());
        }

        Block.popResource(this.level(), this.blockPosition(), itemstack);
        brokenByAnything(damageSource);
    }

    // === player redirects start ===

    @Override
    public @Nullable MobEffectInstance getEffect(MobEffect effect) {
        Player placedBy = getPlacedByPlayer();
        if (placedBy != null){
            return placedBy.getEffect(effect);
        }

        return super.getEffect(effect);
    }

    @Override
    public boolean addEffect(MobEffectInstance effectInstance, @Nullable Entity source){
        Player placedBy = getPlacedByPlayer();
        if (placedBy != null){
            return placedBy.addEffect(effectInstance, source);
        }

        return super.addEffect(effectInstance, source);
    }

    @Override
    public boolean isAffectedByPotions() {
        Player placedBy = getPlacedByPlayer();
        if (placedBy != null){
            return placedBy.isAffectedByPotions();
        }

        return super.isAffectedByPotions();
    }

    @Override
    public float getHealth() {
        Player placedBy = getPlacedByPlayer();
        if (placedBy != null){
            return (float) Math.max(0.5, placedBy.getHealth());
        }

        return super.getHealth();
    }

    @Override
    public double getAttributeValue(Attribute attribute) {
        Player placedBy = getPlacedByPlayer();
        if (placedBy != null){
            return placedBy.getAttributeValue(attribute);
        }

        return super.getAttributeValue(attribute);
    }

    @Override
    public double getAttributeBaseValue(Attribute attribute) {
        Player placedBy = getPlacedByPlayer();
        if (placedBy != null){
            return placedBy.getAttributeBaseValue(attribute);
        }

        return super.getAttributeBaseValue(attribute);
    }

    @Override
    public AttributeMap getAttributes() {
        Player placedBy = getPlacedByPlayer();
        if (placedBy != null){
            return placedBy.getAttributes();
        }

        return super.getAttributes();
    }

    @Override
    public void heal(float damage){
        Player placedBy = getPlacedByPlayer();
        if (placedBy != null){
            placedBy.heal(damage);
        }
        
        super.heal(damage);
    }

    @Override
    public boolean hurt(DamageSource damageSource, float damage){
        Player placedBy = getPlacedByPlayer();
        if (placedBy != null){
            //don't return here so that the effigy can be broken
            placedBy.hurt(damageSource, damage);
        }

        return super.hurt(damageSource, damage);
    }

    @Override
    public void knockback(double strength, double ratioX, double ratioZ){
        Player placedBy = getPlacedByPlayer();
        if (placedBy != null){
            placedBy.knockback(strength, ratioX, ratioZ);
            return;
        }
        
        super.knockback(strength, ratioX, ratioZ);
    }

    @Override
    public boolean curePotionEffects(ItemStack curativeItem){
        Player placedBy = getPlacedByPlayer();
        if (placedBy != null){
            return placedBy.curePotionEffects(curativeItem);
        }
        
        return super.curePotionEffects(curativeItem);
    }

    @Override
    public void lavaHurt(){
        Player placedBy = getPlacedByPlayer();
        if (placedBy != null){
            placedBy.lavaHurt();
        }
        
        super.lavaHurt();
    }

    @Override
    public void setSecondsOnFire(int seconds){
        Player placedBy = getPlacedByPlayer();
        if (placedBy != null){
            placedBy.setSecondsOnFire(seconds);
        }
        
        super.setSecondsOnFire(seconds);
    }

    @Override
    public boolean isOnFire() {
        Player placedBy = getPlacedByPlayer();
        if (placedBy != null){
            return placedBy.isOnFire();
        }

        return super.isOnFire();
    }

    @Override
    public void extinguishFire(){
        Player placedBy = getPlacedByPlayer();
        if (placedBy != null){
            placedBy.extinguishFire();
        }

        super.extinguishFire();
    }

    // === player redirects end ===
}
