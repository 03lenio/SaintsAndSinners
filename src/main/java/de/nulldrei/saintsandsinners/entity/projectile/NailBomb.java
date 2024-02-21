package de.nulldrei.saintsandsinners.entity.projectile;

import de.nulldrei.saintsandsinners.entity.SASEntities;
import de.nulldrei.saintsandsinners.item.SASItems;
import de.nulldrei.saintsandsinners.item.combat.NailBombItem;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Blaze;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class NailBomb extends ThrowableItemProjectile {
    public NailBomb(EntityType<? extends NailBomb> p_37391_, Level p_37392_) {
        super(p_37391_, p_37392_);
    }

    public NailBomb(Level p_37399_, LivingEntity p_37400_) {
        super(SASEntities.NAIL_BOMB.get(), p_37400_, p_37399_);
    }

    public NailBomb(Level p_37394_, double p_37395_, double p_37396_, double p_37397_) {
        super(SASEntities.NAIL_BOMB.get(), p_37395_, p_37396_, p_37397_, p_37394_);
    }

    protected Item getDefaultItem() {
        return SASItems.NAIL_BOMB.get();
    }

    protected void onHit(HitResult p_37406_) {
        super.onHit(p_37406_);
        if (!this.level().isClientSide) {
            level().explode(this, p_37406_.getLocation().x, p_37406_.getLocation().y, p_37406_.getLocation().z, 4.0F, Level.ExplosionInteraction.MOB);
            remove(RemovalReason.DISCARDED);
        }

    }
}
