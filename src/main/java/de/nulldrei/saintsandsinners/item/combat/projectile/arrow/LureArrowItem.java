package de.nulldrei.saintsandsinners.item.combat.projectile.arrow;

import de.nulldrei.saintsandsinners.entity.projectile.arrow.LureArrow;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class LureArrowItem extends ArrowItem {

    public LureArrowItem(Properties p_43235_) {
        super(p_43235_);
    }

    public AbstractArrow createArrow(Level p_43237_, ItemStack p_43238_, LivingEntity p_43239_) {
        return new LureArrow(p_43237_, p_43239_);
    }
}
