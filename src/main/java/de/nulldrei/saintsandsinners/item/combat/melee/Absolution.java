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

public class Absolution extends SwordItem {

    public Absolution(Tier p_43269_, int p_43270_, float p_43271_, Properties p_43272_) {
        super(p_43269_, p_43270_, p_43271_, p_43272_);
    }

    @Override
    public void onInventoryTick(ItemStack stack, Level level, Player player, int slotIndex, int selectedIndex) {
        AttributeModifier rangeModifier = new AttributeModifier(UUID.fromString("556E7665-5bf6-4d07-9c0f-22b3512c1494"), "SAS Absolution extended reach", 0.5D, AttributeModifier.Operation.fromValue(AttributeModifier.Operation.MULTIPLY_BASE.ordinal()));
        if(!player.level().isClientSide()) {
            if(player.getMainHandItem().getItem() == SASItems.ABSOLUTION.get()) {
                if(!player.getAttribute(ForgeMod.ENTITY_REACH.get()).hasModifier(rangeModifier)) {
                    player.getAttribute(ForgeMod.ENTITY_REACH.get()).addTransientModifier(rangeModifier);
                }
            } else {
                player.getAttribute(ForgeMod.ENTITY_REACH.get()).removeModifier(UUID.fromString("556E7665-5bf6-4d07-9c0f-22b3512c1494"));
            }
        }
        super.onInventoryTick(stack, level, player, slotIndex, selectedIndex);
    }

    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        if(!player.level().isClientSide() && player.level().getRandom().nextInt(20) == 0 && entity instanceof LivingEntity) {
            ((LivingEntity) entity).addEffect(new MobEffectInstance(SASMobEffects.BLOOD_POISONING.get(), 300, 1));
        }
        return super.onLeftClickEntity(stack, player, entity);
    }
}
