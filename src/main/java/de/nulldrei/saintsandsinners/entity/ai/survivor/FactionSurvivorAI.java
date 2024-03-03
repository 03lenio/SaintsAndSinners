package de.nulldrei.saintsandsinners.entity.ai.survivor;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import de.nulldrei.saintsandsinners.entity.activitiy.SASActivities;
import de.nulldrei.saintsandsinners.entity.ai.behavior.*;
import de.nulldrei.saintsandsinners.entity.ai.behavior.StartAdmiringItemIfSeen;
import de.nulldrei.saintsandsinners.entity.ai.behavior.StopHoldingItemIfNoLongerAdmiring;
import de.nulldrei.saintsandsinners.entity.ai.memory.SASMemoryModules;
import de.nulldrei.saintsandsinners.entity.hostile.AbstractFactionSurvivor;
import de.nulldrei.saintsandsinners.entity.neutral.AbstractSurvivor;
import net.minecraft.core.GlobalPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.*;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.piglin.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.schedule.Activity;

import java.util.List;
import java.util.Optional;

public class FactionSurvivorAI {
    private static final UniformInt RETREAT_DURATION = TimeUtil.rangeOfSeconds(15, 25);

    public static Brain<?> makeBrain(AbstractFactionSurvivor p_35100_, Brain<AbstractFactionSurvivor> p_35101_) {
        initCoreActivity(p_35100_, p_35101_);
        initIdleActivity(p_35100_, p_35101_);
        initFightActivity(p_35100_, p_35101_);
        initRetreatActivity(p_35100_, p_35101_);
        p_35101_.setCoreActivities(ImmutableSet.of(Activity.CORE));
        p_35101_.setDefaultActivity(Activity.IDLE);
        p_35101_.useDefaultActivity();
        return p_35101_;
    }

    public static void initMemories(AbstractSurvivor p_35095_) {
        GlobalPos globalpos = GlobalPos.of(p_35095_.level().dimension(), p_35095_.blockPosition());
        p_35095_.getBrain().setMemory(MemoryModuleType.HOME, globalpos);
    }

    private static void initCoreActivity(AbstractSurvivor p_35112_, Brain<AbstractFactionSurvivor> p_35113_) {
        p_35113_.addActivity(Activity.CORE, 0, ImmutableList.of(new Swim(0.8F), new LookAtTargetSink(45, 90), new MoveToTargetSink(),InteractWithDoor.create(), StartDemanding.create(), StopDemanding.create(), StopDemandingWhenFleeing.create(), StopBeingAngryIfTargetDead.create(), StopHoldingItemIfNoLongerAdmiring.create(), StartAdmiringItemIfSeen.create(200)));
    }

    private static void initIdleActivity(AbstractSurvivor p_35120_, Brain<AbstractFactionSurvivor> p_35121_) {
        p_35121_.addActivity(Activity.IDLE, 10, ImmutableList.of(StartAttacking.<AbstractSurvivor>create(FactionSurvivorAI::findNearestValidAttackTarget), createIdleLookBehaviors(), createIdleMovementBehaviors(), SetLookAndInteract.create(EntityType.PLAYER, 4)));
    }

    private static void initFightActivity(AbstractSurvivor p_35125_, Brain<AbstractFactionSurvivor> p_35126_) {
        p_35126_.addActivityAndRemoveMemoryWhenStopped(Activity.FIGHT, 10, ImmutableList.<BehaviorControl<? super AbstractSurvivor>>of(StopAttackingIfTargetInvalid.<AbstractSurvivor>create((p_34981_) -> {
            return !isNearestValidAttackTarget(p_35125_, p_34981_);
        }), BehaviorBuilder.triggerIf(FactionSurvivorAI::hasCrossbow, BackUpIfTooClose.create(5, 0.75F)), SetWalkTargetFromAttackTargetIfTargetOutOfReach.create(1.0F), MeleeAttack.create(20), new CrossbowAttack(), RememberIfHoglinWasKilled.create()), MemoryModuleType.ATTACK_TARGET);    }

    
    private static void initRetreatActivity(AbstractSurvivor AbstractSurvivor, Brain<AbstractFactionSurvivor> p_34959_) {
        p_34959_.addActivityAndRemoveMemoryWhenStopped(Activity.AVOID, 10, ImmutableList.of(SetWalkTargetAwayFrom.entity(MemoryModuleType.AVOID_TARGET, 1.0F, 12, true), createIdleLookBehaviors(), createIdleMovementBehaviors(), EraseMemoryIf.<AbstractFactionSurvivor>create(FactionSurvivorAI::wantsToStopFleeing, MemoryModuleType.AVOID_TARGET)), MemoryModuleType.AVOID_TARGET);
    }

    private static RunOne<AbstractSurvivor> createIdleLookBehaviors() {
        return new RunOne<>(ImmutableList.of(Pair.of(SetEntityLookTarget.create(EntityType.PLAYER, 20.0F), 0)));
    }

    private static RunOne<AbstractSurvivor> createIdleMovementBehaviors() {
            return new RunOne<>(ImmutableList.of(Pair.of(RandomStroll.stroll(0.6F), 2), Pair.of(StrollToPoi.create(MemoryModuleType.HOME, 0.6F, 2, 100), 2), Pair.of(StrollAroundPoi.create(MemoryModuleType.HOME, 0.6F, 5), 2), Pair.of(new DoNothing(30, 60), 1)));


    }


    public static void updateActivity(AbstractSurvivor p_35110_) {
        Brain<AbstractSurvivor> brain = (Brain<AbstractSurvivor>) p_35110_.getBrain();
        Activity activity = brain.getActiveNonCoreActivity().orElse((Activity)null);
        brain.setActiveActivityToFirstValid(ImmutableList.of(Activity.FIGHT, Activity.AVOID, Activity.IDLE));
        Activity activity1 = brain.getActiveNonCoreActivity().orElse((Activity)null);
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
            AbstractSurvivor AbstractSurvivor = (AbstractSurvivor) p_35087_;
            Optional<? extends LivingEntity> playerNearby = getTargetIfWithinRange(p_35087_, MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER);
            Optional<? extends LivingEntity> rivalNearby = getTargetIfWithinRange(p_35087_, SASMemoryModules.NEAREST_RIVALING_SURVIVOR.get());
            Optional<? extends LivingEntity> zombieNearby = getTargetIfWithinRange(p_35087_, SASMemoryModules.NEAREST_VISIBLE_ZOMBIE.get());

            if(rivalNearby.isPresent()) {
                return rivalNearby;
            }
            else if(zombieNearby.isPresent() && factionCanTakeOnZombies(AbstractSurvivor)) {
                broadcastAngerTarget(p_35087_, zombieNearby.get());
                return zombieNearby;
            }
            else if(playerNearby.isPresent()) {
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

    private static void broadcastRetreat(AbstractSurvivor p_34930_, LivingEntity p_34931_) {
        getFactionSurvivors((AbstractFactionSurvivor) p_34930_).stream().filter((p_34985_) -> {
            return p_34985_ instanceof AbstractSurvivor;
        }).forEach((p_34819_) -> {
            setAvoidTarget((AbstractSurvivor) p_34819_, p_34931_);
        });
    }

    private static void setAvoidTarget(AbstractSurvivor p_34968_, LivingEntity p_34969_) {
        p_34968_.getBrain().eraseMemory(MemoryModuleType.ANGRY_AT);
        p_34968_.getBrain().eraseMemory(MemoryModuleType.ATTACK_TARGET);
        p_34968_.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
        p_34968_.getBrain().setMemoryWithExpiry(MemoryModuleType.AVOID_TARGET, p_34969_, (long)RETREAT_DURATION.sample(p_34968_.level().random));
    }

    protected static void broadcastAngerTarget(AbstractSurvivor p_34896_, LivingEntity p_34897_) {
        getFactionSurvivors((AbstractFactionSurvivor) p_34896_).forEach((p_289474_) -> {
            setAngerTargetIfCloserThanCurrent(p_289474_, p_34897_);

        });
    }

    private static void setAngerTargetIfCloserThanCurrent(AbstractSurvivor p_34963_, LivingEntity p_34964_) {
        Optional<LivingEntity> optional = getAngerTarget(p_34963_);
        LivingEntity livingentity = BehaviorUtils.getNearestTarget(p_34963_, optional, p_34964_);
        if (!optional.isPresent() || optional.get() != livingentity) {
            setAngerTarget(p_34963_, livingentity);
        }
    }

    private static Optional<LivingEntity> getAngerTarget(AbstractSurvivor p_34976_) {
        return BehaviorUtils.getLivingEntityFromUUIDMemory(p_34976_, MemoryModuleType.ANGRY_AT);
    }

    private static List<AbstractFactionSurvivor> getFactionSurvivors(AbstractFactionSurvivor p_34961_) {
        return p_34961_.getBrain().getMemory(SASMemoryModules.NEARBY_SURVIVORS.get()).orElse(ImmutableList.of());
    }

    private static boolean wantsToStopFleeing(AbstractSurvivor p_35009_) {
        Brain<AbstractSurvivor> brain = (Brain<AbstractSurvivor>) p_35009_.getBrain();
        if (!brain.hasMemoryValue(MemoryModuleType.AVOID_TARGET)) {
            return true;
        } else {
            LivingEntity livingentity = brain.getMemory(MemoryModuleType.AVOID_TARGET).get();
            EntityType<?> entitytype = livingentity.getType();
            if (entitytype == EntityType.ZOMBIE) {
                return factionCanTakeOnZombies(p_35009_);
            } else {
                return false;
            }
        }
    }


    public static void wasHurtBy(AbstractSurvivor p_35097_, LivingEntity p_35098_) {
        if (!(p_35098_ instanceof AbstractSurvivor)) {
            Brain<AbstractSurvivor> brain = (Brain<AbstractSurvivor>) p_35097_.getBrain();
            if (p_35098_ instanceof Player) {
                broadcastAngerTarget(p_35097_, p_35098_);
               setAngerTarget(p_35097_, p_35098_);
            }
            if (p_35098_ instanceof Zombie zombie) {
                if(zombiesOutnumberFaction(p_35097_)) {
                    if(p_35097_.getHealth() > 5) {
                        setAvoidTarget(p_35097_, zombie);
                    } else {
                        broadcastRetreat(p_35097_, zombie);
                        setAngerTarget(p_35097_, zombie);
                    }
                } else {
                    setAngerTarget(p_35097_, zombie);
                    broadcastAngerTarget(p_35097_, zombie);
                }
            }
        }
    }

    protected static void setAngerTarget(AbstractSurvivor p_149989_, LivingEntity p_149990_) {
        p_149989_.getBrain().eraseMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
        p_149989_.getBrain().setMemoryWithExpiry(MemoryModuleType.ANGRY_AT, p_149990_.getUUID(), 600L);
    }

    private static boolean zombiesOutnumberFaction(AbstractSurvivor AbstractSurvivor) {
        int zombieCount = AbstractSurvivor.getBrain().getMemory(SASMemoryModules.VISIBLE_ZOMBIE_COUNT.get()).orElse(0);
        int survivorFromFactionCount = AbstractSurvivor.getBrain().getMemory(SASMemoryModules.VISIBLE_SURVIVOR_COUNT.get()).orElse(0);
        return zombieCount > survivorFromFactionCount;
    }

    private static boolean factionCanTakeOnZombies(AbstractSurvivor p35009) {
        return !zombiesOutnumberFaction(p35009);
    }



    private static void stopWalking(AbstractSurvivor p_35007_) {
        p_35007_.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
        p_35007_.getNavigation().stop();
    }

    private static boolean hasCrossbow(LivingEntity p_34919_) {
        return p_34919_.isHolding(is -> is.getItem() instanceof net.minecraft.world.item.CrossbowItem);
    }

    public static void maybePlayActivitySound(AbstractSurvivor p_35115_) {
        if ((double)p_35115_.level().random.nextFloat() < 0.0125D) {
            playActivitySound(p_35115_);
        }

    }

    private static void playActivitySound(AbstractSurvivor p_35123_) {
        p_35123_.getBrain().getActiveNonCoreActivity().ifPresent((p_35104_) -> {
            if (p_35104_ == Activity.FIGHT) {
                p_35123_.playSound(SoundEvents.EVOKER_CELEBRATE);
            }

        });
    }
}
