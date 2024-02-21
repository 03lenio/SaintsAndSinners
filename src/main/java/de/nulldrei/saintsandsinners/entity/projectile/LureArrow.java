package de.nulldrei.saintsandsinners.entity.projectile;

import com.mojang.datafixers.types.templates.CompoundList;
import de.nulldrei.saintsandsinners.entity.SASEntities;
import de.nulldrei.saintsandsinners.entity.ai.behavior.goal.ZombieWalkToPositionGoal;
import de.nulldrei.saintsandsinners.entity.neutral.RobberSurvivor;
import de.nulldrei.saintsandsinners.item.SASItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.FollowMobGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.projectile.SpectralArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import org.checkerframework.checker.units.qual.C;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LureArrow extends AbstractArrow {
   private static final EntityDataAccessor<Integer> DATA_TICKS_UNTIL_STOP = SynchedEntityData.defineId(LureArrow.class, EntityDataSerializers.INT);

   private static final EntityDataAccessor<Integer> DATA_TICKS_UUID = SynchedEntityData.defineId(LureArrow.class, EntityDataSerializers.INT);

   private static final EntityDataAccessor<Integer> DATA_TICKS_AFFECTED_ZOMBIES_COUNT = SynchedEntityData.defineId(LureArrow.class, EntityDataSerializers.INT);

   private ArrayList<Zombie> affectedZombies = new ArrayList<Zombie>();

   public LureArrow(EntityType<? extends LureArrow> p_37411_, Level p_37412_) {
      super(p_37411_, p_37412_);
   }

   public LureArrow(Level p_37419_, LivingEntity p_37420_) {
      super(SASEntities.LURE_ARROW.get(), p_37420_, p_37419_);
   }

   public LureArrow(Level p_37414_, double p_37415_, double p_37416_, double p_37417_) {
      super(SASEntities.LURE_ARROW.get(), p_37415_, p_37416_, p_37417_, p_37414_);
   }

   public void tick() {
      if (!this.level().isClientSide) {
         if (getTicksUntilStop() <= 400) {
            if((getTicksUntilStop() % 6) == 0) {
               this.level().playSound(null, this.getBlockPosBelowThatAffectsMyMovement(), SoundEvents.FIREWORK_ROCKET_LARGE_BLAST, SoundSource.AMBIENT);
            }
            AABB distractionRange = this.getBoundingBox().inflate(5D);
            for (Entity e : level().getEntities(this, distractionRange)) {
               if (e instanceof Zombie zombie) {
                  if (random.nextInt(10) == 0) {
                     //System.out.println("holy gay " + getTicksUntilStop());
                     zombie.goalSelector.addGoal(1, new ZombieWalkToPositionGoal(zombie, 2.0D, 0.75D, getBlockPosBelowThatAffectsMyMovement()));
                     affectedZombies.add(zombie);
                  }
               }
            }
            setTicksUntilStop(getTicksUntilStop()+1);
         } else {
            try {
               if(!affectedZombies.isEmpty()) {
                  removeWalkToPositionGoal(affectedZombies);
               }
            } catch (NullPointerException | IndexOutOfBoundsException e) {
               e.printStackTrace();
            }
        }
      }
      super.tick();
   }


   @Override
   protected boolean tryPickup(Player p_150121_) {
      try {
         if (!p_150121_.level().isClientSide() && !affectedZombies.isEmpty()) {
            removeWalkToPositionGoal(affectedZombies);
         }
      } catch (NullPointerException | IndexOutOfBoundsException e) {
         e.printStackTrace();
      }
      return super.tryPickup(p_150121_);
   }

   protected ItemStack getPickupItem() {
      return new ItemStack(SASItems.LURE_ARROW.get());
   }

   @Override
   protected void defineSynchedData() {
      getEntityData().define(DATA_TICKS_UNTIL_STOP, 0);
      super.defineSynchedData();
   }

   public void setTicksUntilStop(int ticksUntilStop) {
      getEntityData().set(DATA_TICKS_UNTIL_STOP, ticksUntilStop);
   }

   public int getTicksUntilStop() {
      return getEntityData().get(DATA_TICKS_UNTIL_STOP);
   }


   private void removeWalkToPositionGoal(ArrayList<Zombie> affectedZombies) {
      ArrayList<Zombie> markedForRemoval = new ArrayList<Zombie>();
      if (!affectedZombies.isEmpty()) {
         for (Zombie zombie : affectedZombies) {
            if (zombie.isAddedToWorld()) {
               List<WrappedGoal> goalList = zombie.goalSelector.getAvailableGoals().stream().toList();
               for (int j = 0; j < goalList.size(); j++) {
                  Goal goal = goalList.get(j).getGoal();
                  if (goal instanceof ZombieWalkToPositionGoal) {
                     markedForRemoval.add(zombie);
                     zombie.goalSelector.removeGoal(goal);
                  }
               }
            }
         }
      }
      for(Zombie zombie : markedForRemoval) {
         affectedZombies.remove(zombie);
      }
   }

   public void readAdditionalSaveData(CompoundTag p_37424_) {
      super.readAdditionalSaveData(p_37424_);
      if (p_37424_.contains("TicksUntilStop")) {
         setTicksUntilStop(p_37424_.getInt("TicksUntilStop"));
      }
   }

   public void addAdditionalSaveData(CompoundTag p_37426_) {
      super.addAdditionalSaveData(p_37426_);
      p_37426_.putInt("TicksUntilStop", this.getTicksUntilStop());
   }
}
