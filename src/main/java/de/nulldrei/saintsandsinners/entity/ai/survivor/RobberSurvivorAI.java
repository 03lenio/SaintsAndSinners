package de.nulldrei.saintsandsinners.entity.ai.survivor;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import de.nulldrei.saintsandsinners.SASUtil;
import de.nulldrei.saintsandsinners.entity.activitiy.SASActivities;
import de.nulldrei.saintsandsinners.entity.ai.behavior.*;
import de.nulldrei.saintsandsinners.entity.ai.behavior.StartAdmiringItemIfSeen;
import de.nulldrei.saintsandsinners.entity.ai.behavior.StopHoldingItemIfNoLongerAdmiring;
import de.nulldrei.saintsandsinners.entity.ai.memory.SASMemoryModules;
import de.nulldrei.saintsandsinners.entity.ai.memory.sensors.RobberSurvivorSpecificSensor;
import de.nulldrei.saintsandsinners.entity.neutral.AbstractSurvivor;
import de.nulldrei.saintsandsinners.entity.neutral.RobberSurvivor;
import de.nulldrei.saintsandsinners.entity.peaceful.BeggarSurvivor;
import de.nulldrei.saintsandsinners.item.SASItems;
import net.minecraft.core.GlobalPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.*;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.entity.animal.allay.AllayAi;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.piglin.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;
import java.util.Random;
import java.util.function.Predicate;

public class RobberSurvivorAI {
    private static final UniformInt RETREAT_DURATION = TimeUtil.rangeOfSeconds(15, 25);

    public static Brain<?> makeBrain(RobberSurvivor p_35100_, Brain<RobberSurvivor> p_35101_) {
        initCoreActivity(p_35100_, p_35101_);
        initIdleActivity(p_35100_, p_35101_);
        initAdmireItemActivity(p_35101_);
        initDemandActivity(p_35100_, p_35101_);
        initFightActivity(p_35100_, p_35101_);
        initRetreatActivity(p_35101_);
        p_35101_.setCoreActivities(ImmutableSet.of(Activity.CORE));
        p_35101_.setDefaultActivity(Activity.IDLE);
        p_35101_.useDefaultActivity();
        return p_35101_;
    }

    public static void initMemories(RobberSurvivor p_35095_) {
        GlobalPos globalpos = GlobalPos.of(p_35095_.level().dimension(), p_35095_.blockPosition());
        p_35095_.getBrain().setMemory(MemoryModuleType.HOME, globalpos);
    }

    private static void initCoreActivity(RobberSurvivor p_35112_, Brain<RobberSurvivor> p_35113_) {
        p_35113_.addActivity(Activity.CORE, 0, ImmutableList.of(new LookAtTargetSink(45, 90), new MoveToTargetSink(),InteractWithDoor.create(), avoidZombiesIfOutnumbered(), StartDemanding.create(), StopDemanding.create(), StopDemandingWhenFleeing.create(), StopBeingAngryIfTargetDead.create(), StopHoldingItemIfNoLongerAdmiring.create(), StartAdmiringItemIfSeen.create(200)));
    }

    private static void initIdleActivity(RobberSurvivor p_35120_, Brain<RobberSurvivor> p_35121_) {
        p_35121_.addActivity(Activity.IDLE, 10, ImmutableList.of(StartAttacking.<RobberSurvivor>create(RobberSurvivorAI::findNearestValidAttackTarget), createIdleLookBehaviors(), createIdleMovementBehaviors(), SetLookAndInteract.create(EntityType.PLAYER, 4)));
    }

    private static void initFightActivity(RobberSurvivor p_35125_, Brain<RobberSurvivor> p_35126_) {
        p_35126_.addActivityAndRemoveMemoryWhenStopped(Activity.FIGHT, 10, ImmutableList.<net.minecraft.world.entity.ai.behavior.BehaviorControl<? super RobberSurvivor>>of(StopAttackingIfTargetInvalid.<RobberSurvivor>create((p_34981_) -> {
            return !isNearestValidAttackTarget(p_35125_, p_34981_);
        }), BehaviorBuilder.triggerIf(RobberSurvivorAI::hasCrossbow, BackUpIfTooClose.create(5, 0.75F)), SetWalkTargetFromAttackTargetIfTargetOutOfReach.create(1.0F), MeleeAttack.create(20), new CrossbowAttack(), RememberIfHoglinWasKilled.create()), MemoryModuleType.ATTACK_TARGET);    }


    private static void initDemandActivity(RobberSurvivor p_35125_, Brain<RobberSurvivor> p_35126_) {
        p_35126_.addActivityWithConditions(SASActivities.DEMAND_ITEM.get(), ImmutableList.of(Pair.of(0, ChaseClosestPlayer.create(RobberSurvivor::isDemanding)), Pair.of(1, SetEntityLookTarget.create(EntityType.PLAYER, 16F))), ImmutableSet.of(Pair.of(SASMemoryModules.IS_DEMANDING.get(), MemoryStatus.VALUE_PRESENT), Pair.of(MemoryModuleType.NEAREST_VISIBLE_PLAYER, MemoryStatus.VALUE_PRESENT), Pair.of(SASMemoryModules.NEAREST_VISIBLE_ZOMBIE.get(), MemoryStatus.VALUE_ABSENT)));
    }

    private static void initAdmireItemActivity(Brain<RobberSurvivor> p_34941_) {
        p_34941_.addActivityAndRemoveMemoryWhenStopped(Activity.ADMIRE_ITEM, 10, ImmutableList.of(GoToWantedItem.create(RobberSurvivorAI::isNotHoldingNeededItemInOffHand, 1.0F, true, 9), StopAdmiringIfItemTooFarAway.create(9), StopAdmiringIfTiredOfTryingToReachItem.create(200, 200)), MemoryModuleType.ADMIRING_ITEM);
    }

    private static void initRetreatActivity(Brain<RobberSurvivor> p_34959_) {
        p_34959_.addActivityAndRemoveMemoryWhenStopped(Activity.AVOID, 10, ImmutableList.of(SetWalkTargetAwayFrom.entity(MemoryModuleType.AVOID_TARGET, 1.0F, 12, true), createIdleLookBehaviors(), createIdleMovementBehaviors(), EraseMemoryIf.<RobberSurvivor>create(RobberSurvivorAI::wantsToStopFleeing, MemoryModuleType.AVOID_TARGET)), MemoryModuleType.AVOID_TARGET);
    }

    private static RunOne<RobberSurvivor> createIdleLookBehaviors() {
        return new RunOne<>(ImmutableList.of(Pair.of(SetEntityLookTarget.create(EntityType.PLAYER, 20.0F), 0)));
    }

    private static RunOne<RobberSurvivor> createIdleMovementBehaviors() {
        return new RunOne<>(ImmutableList.of(Pair.of(RandomStroll.stroll(0.6F), 2), Pair.of(StrollToPoi.create(MemoryModuleType.HOME, 0.6F, 2, 100), 2), Pair.of(StrollAroundPoi.create(MemoryModuleType.HOME, 0.6F, 5), 2), Pair.of(new DoNothing(30, 60), 1)));
    }

    private static BehaviorControl<RobberSurvivor> avoidZombiesIfOutnumbered() {
        return CopyMemoryWithExpiry.create(RobberSurvivorAI::zombiesOutnumberRobber, SASMemoryModules.NEAREST_VISIBLE_ZOMBIE.get(), MemoryModuleType.AVOID_TARGET, RETREAT_DURATION);
    }

    public static void updateActivity(RobberSurvivor p_35110_) {
        Brain<RobberSurvivor> brain = p_35110_.getBrain();
        Activity activity = brain.getActiveNonCoreActivity().orElse((Activity)null);
        brain.setActiveActivityToFirstValid(ImmutableList.of(Activity.ADMIRE_ITEM, Activity.FIGHT, Activity.AVOID, SASActivities.DEMAND_ITEM.get(), Activity.IDLE));
        Activity activity1 = brain.getActiveNonCoreActivity().orElse((Activity)null);
        System.out.println(activity+":"+activity1);
        if (activity != activity1) {
            playActivitySound(p_35110_);
        }

        p_35110_.setAggressive(brain.hasMemoryValue(MemoryModuleType.ATTACK_TARGET));
    }

    private static boolean isNearestValidAttackTarget(AbstractSurvivor p_35089_, LivingEntity p_35090_) {
        return findNearestValidAttackTarget(p_35089_).filter((p_35085_) -> {
            return p_35085_ == p_35090_;
        }).isPresent();
    }

    private static Optional<? extends LivingEntity> findNearestValidAttackTarget(AbstractSurvivor p_35087_) {
        Optional<LivingEntity> optional = BehaviorUtils.getLivingEntityFromUUIDMemory(p_35087_, MemoryModuleType.ANGRY_AT);
        if (optional.isPresent() && Sensor.isEntityAttackableIgnoringLineOfSight(p_35087_, optional.get())) {
            return optional;
        } else {
            RobberSurvivor robberSurvivor = (RobberSurvivor) p_35087_;
            Optional<? extends LivingEntity> playerNearby = getTargetIfWithinRange(p_35087_, MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER);
            Optional<? extends LivingEntity> zombieNearby = getTargetIfWithinRange(p_35087_, SASMemoryModules.NEAREST_VISIBLE_ZOMBIE.get());
            System.out.println(robberSurvivor.isDemanding() + ":" + robberSurvivor.hasReceivedItem() + ":" + optional.isPresent());

            if(zombieNearby.isPresent() && robberCanTakeOnZombies(robberSurvivor)) {
                return zombieNearby;
            } else if(playerNearby.isPresent() && !robberSurvivor.isDemanding() && !robberSurvivor.hasReceivedItem()) {
                System.out.println("PUT YOU IN THE TUNDRA");
                return playerNearby;
            }
        }
        return Optional.empty();
    }

    private static Optional<? extends LivingEntity> getTargetIfWithinRange(AbstractSurvivor p_35092_, MemoryModuleType<? extends LivingEntity> p_35093_) {
        return p_35092_.getBrain().getMemory(p_35093_).filter((p_35108_) -> {
            return p_35108_.closerThan(p_35092_, 12.0D);
        });
    }

    private static void setAvoidTarget(RobberSurvivor p_34968_, LivingEntity p_34969_) {
        p_34968_.getBrain().eraseMemory(MemoryModuleType.ANGRY_AT);
        p_34968_.getBrain().eraseMemory(MemoryModuleType.ATTACK_TARGET);
        p_34968_.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
        p_34968_.getBrain().setMemoryWithExpiry(MemoryModuleType.AVOID_TARGET, p_34969_, (long)RETREAT_DURATION.sample(p_34968_.level().random));
    }


    private static boolean wantsToStopFleeing(RobberSurvivor p_35009_) {
        Brain<RobberSurvivor> brain = p_35009_.getBrain();
        if (!brain.hasMemoryValue(MemoryModuleType.AVOID_TARGET)) {
            return true;
        } else {
            LivingEntity livingentity = brain.getMemory(MemoryModuleType.AVOID_TARGET).get();
            EntityType<?> entitytype = livingentity.getType();
            if (entitytype == EntityType.ZOMBIE) {
                return robberCanTakeOnZombies(p_35009_);
            } else {
                return false;
            }
        }
    }


    public static void wasHurtBy(RobberSurvivor p_35097_, LivingEntity p_35098_) {
        if (!(p_35098_ instanceof AbstractSurvivor)) {
            if (isHoldingItemInOffHand(p_35097_)) {
                stopHoldingOffHandItem(p_35097_, false);
            }
            Brain<RobberSurvivor> brain = p_35097_.getBrain();
            brain.eraseMemory(MemoryModuleType.ADMIRING_ITEM);
            if (p_35098_ instanceof Player) {
                p_35097_.setDemanding(false);
                brain.setMemoryWithExpiry(MemoryModuleType.ADMIRING_DISABLED, true, 400L);
                setAngerTarget(p_35097_, p_35098_);
            }
        }
    }

    protected static void setAngerTarget(RobberSurvivor p_149989_, LivingEntity p_149990_) {
        p_149989_.getBrain().eraseMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
        p_149989_.getBrain().setMemoryWithExpiry(MemoryModuleType.ANGRY_AT, p_149990_.getUUID(), 600L);
    }

    private static boolean zombiesOutnumberRobber(RobberSurvivor robberSurvivor) {
        int zombieCount = robberSurvivor.getBrain().getMemory(SASMemoryModules.VISIBLE_ZOMBIE_COUNT.get()).orElse(0);
        return zombieCount > 4;
    }

    private static boolean robberCanTakeOnZombies(RobberSurvivor p35009) {
        return !zombiesOutnumberRobber(p35009);
    }

    private static void admireGiftItem(LivingEntity p_34939_) {
        p_34939_.getBrain().setMemoryWithExpiry(MemoryModuleType.ADMIRING_ITEM, true, 120L);
    }

    private static boolean isAdmiringItem(RobberSurvivor p_35021_) {
        return p_35021_.getBrain().hasMemoryValue(MemoryModuleType.ADMIRING_ITEM);
    }


    private static void stopWalking(RobberSurvivor p_35007_) {
        p_35007_.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
        p_35007_.getNavigation().stop();
    }

    private static boolean hasCrossbow(LivingEntity p_34919_) {
        return p_34919_.isHolding(is -> is.getItem() instanceof net.minecraft.world.item.CrossbowItem);
    }

    public static void maybePlayActivitySound(RobberSurvivor p_35115_) {
        if ((double)p_35115_.level().random.nextFloat() < 0.0125D) {
            playActivitySound(p_35115_);
        }

    }

    private static void playActivitySound(RobberSurvivor p_35123_) {
        p_35123_.getBrain().getActiveNonCoreActivity().ifPresent((p_35104_) -> {
            if (p_35104_ == Activity.FIGHT) {
                p_35123_.playSound(SoundEvents.EVOKER_CELEBRATE);
            }

        });
    }

    public static boolean canAdmire(RobberSurvivor p_34910_, ItemStack p_34911_) {
        return !isAdmiringDisabled(p_34910_) && !isAdmiringItem(p_34910_) && p_34910_.doesSurvivorNeedItem(p_34911_);
    }

    private static boolean isAdmiringDisabled(RobberSurvivor p_35025_) {
        return p_35025_.getBrain().hasMemoryValue(MemoryModuleType.ADMIRING_DISABLED);
    }


    public static InteractionResult mobInteract(RobberSurvivor p_34847_, Player p_34848_, InteractionHand p_34849_) {
        ItemStack itemstack = p_34848_.getItemInHand(p_34849_);
        if (canAdmire(p_34847_, itemstack)) {
            p_34847_.setDemanding(false);
            p_34847_.setItemReceived(true);
            ItemStack itemstack1 = itemstack.split(1);
            holdInOffhand(p_34847_, itemstack1);
            admireGiftItem(p_34847_);
            stopWalking(p_34847_);
            return InteractionResult.CONSUME;
        } else {
            return InteractionResult.PASS;
        }
    }


    private static boolean isHoldingItemInOffHand(RobberSurvivor p_35027_) {
        return !p_35027_.getOffhandItem().isEmpty();
    }

    private static boolean isNotHoldingNeededItemInOffHand(RobberSurvivor p_35029_) {
        return p_35029_.getOffhandItem().isEmpty() || !p_35029_.doesSurvivorNeedItem(p_35029_.getOffhandItem());
    }


    public static boolean wantsToPickup(RobberSurvivor robberSurvivor, ItemStack p34777) {
        if (isAdmiringDisabled(robberSurvivor) && robberSurvivor.getBrain().hasMemoryValue(MemoryModuleType.ATTACK_TARGET)) {
            return false;
        } else if (robberSurvivor.doesSurvivorNeedItem(p34777)) {
            return isNotHoldingNeededItemInOffHand(robberSurvivor);
        } else {
            boolean flag = robberSurvivor.canAddToInventory(p34777);
            if (p34777.is(Items.ROTTEN_FLESH)) {
                return flag;
            } else if (!robberSurvivor.doesSurvivorNeedItem(p34777)) {
                return robberSurvivor.canReplaceCurrentItem(p34777);
            } else {
                return isNotHoldingNeededItemInOffHand(robberSurvivor) && flag;
            }
        }
    }

    private static ItemStack removeOneItemFromItemEntity(ItemEntity p_34823_) {
        ItemStack itemstack = p_34823_.getItem();
        ItemStack itemstack1 = itemstack.split(1);
        if (itemstack.isEmpty()) {
            p_34823_.discard();
        } else {
            p_34823_.setItem(itemstack);
        }

        return itemstack1;
    }

    private static void holdInOffhand(RobberSurvivor p_34933_, ItemStack p_34934_) {

        if (isHoldingItemInOffHand(p_34933_)) {
            p_34933_.spawnAtLocation(p_34933_.getItemInHand(InteractionHand.OFF_HAND));
        }

        p_34933_.holdInOffHand(p_34934_);

    }

    public static void stopHoldingOffHandItem(RobberSurvivor p_34868_, boolean p_34869_) {
        ItemStack itemstack = p_34868_.getItemInHand(InteractionHand.OFF_HAND);
        p_34868_.setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);
        boolean flag = p_34868_.doesSurvivorNeedItem(itemstack);
        if (p_34869_) {
            putInInventory(p_34868_, itemstack);
        }
    }

    private static void putInInventory(RobberSurvivor p_34953_, ItemStack p_34954_) {
        ItemStack itemstack = p_34953_.addToInventory(p_34954_);
    }

    public static void pickUpItem(RobberSurvivor p_34844_, ItemEntity p_34845_) {
        stopWalking(p_34844_);
        ItemStack itemstack;
        itemstack = p_34845_.getItem();
        if (p_34844_.doesSurvivorNeedItem(itemstack)) {
            p_34844_.take(p_34845_, 1);
            itemstack = removeOneItemFromItemEntity(p_34845_);
            p_34844_.setDemanding(false);
            p_34844_.setItemReceived(true);
            p_34844_.getBrain().eraseMemory(MemoryModuleType.TIME_TRYING_TO_REACH_ADMIRE_ITEM);
            holdInOffhand(p_34844_, itemstack);
            admireGiftItem(p_34844_);
        }
    }
}
