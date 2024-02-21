package de.nulldrei.saintsandsinners.item.combat;

import de.nulldrei.saintsandsinners.entity.projectile.arrow.ExplosiveArrow;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;

public class ExplosiveArrowItem extends ArrowItem {

    public ExplosiveArrowItem(Item.Properties p_43235_) {
        super(p_43235_);
    }

    public AbstractArrow createArrow(Level p_43237_, ItemStack p_43238_, LivingEntity p_43239_) {
        return new ExplosiveArrow(p_43237_, p_43239_);
    }
}
