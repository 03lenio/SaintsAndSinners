package de.nulldrei.saintsandsinners.entity.projectile;

import de.nulldrei.saintsandsinners.entity.SASEntities;
import de.nulldrei.saintsandsinners.item.SASItems;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;

public class ExplosiveArrow extends AbstractArrow {
   private int duration = 200;

   public ExplosiveArrow(EntityType<? extends ExplosiveArrow> p_37411_, Level p_37412_) {
      super(p_37411_, p_37412_);
   }

   public ExplosiveArrow(Level p_37419_, LivingEntity p_37420_) {
      super(SASEntities.EXPLOSIVE_ARROW.get(), p_37420_, p_37419_);
   }

   public ExplosiveArrow(Level p_37414_, double p_37415_, double p_37416_, double p_37417_) {
      super(SASEntities.EXPLOSIVE_ARROW.get(), p_37415_, p_37416_, p_37417_, p_37414_);
   }

   public void tick() {
      if (this.level().isClientSide && !this.inGround) {
         this.level().playSound(null, this.getBlockPosBelowThatAffectsMyMovement(), SoundEvents.TNT_PRIMED, SoundSource.AMBIENT);
      }
      super.tick();
   }


   protected ItemStack getPickupItem() {
      return ItemStack.EMPTY;
   }

   @Override
   protected void onHit(HitResult p_37260_) {
      if (!level().isClientSide()) {
         level().explode(this, p_37260_.getLocation().x, p_37260_.getLocation().y, p_37260_.getLocation().z, 4.0F, Level.ExplosionInteraction.MOB);
         remove(RemovalReason.DISCARDED);
         super.onHit(p_37260_);
      }
   }
}
