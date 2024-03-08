package de.nulldrei.saintsandsinners.item.combat.melee;

import de.nulldrei.saintsandsinners.effect.SASMobEffects;
import de.nulldrei.saintsandsinners.item.SASItems;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeMod;

import java.util.UUID;

public class SamedisHand extends SwordItem {

    public SamedisHand(Tier p_43269_, int p_43270_, float p_43271_, Properties p_43272_) {
        super(p_43269_, p_43270_, p_43271_, p_43272_);
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        if(!player.level().isClientSide() && player.level().getRandom().nextInt(25) == 0 && entity instanceof LivingEntity) {
            ((LivingEntity) entity).addEffect(new MobEffectInstance(SASMobEffects.BLOOD_POISONING.get(), 300, 1));
        }
        return super.onLeftClickEntity(stack, player, entity);
    }
}
