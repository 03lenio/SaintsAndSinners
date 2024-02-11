package de.nulldrei.saintsandsinners.entity.ai.survivor;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import de.nulldrei.saintsandsinners.SASUtil;
import de.nulldrei.saintsandsinners.entity.ai.behavior.*;
import de.nulldrei.saintsandsinners.entity.ai.memory.SASMemoryModules;
import de.nulldrei.saintsandsinners.entity.neutral.AbstractSurvivor;
import de.nulldrei.saintsandsinners.entity.peaceful.BeggarSurvivor;
import de.nulldrei.saintsandsinners.item.SASItems;
import net.minecraft.core.GlobalPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.*;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.piglin.StopAdmiringIfItemTooFarAway;
import net.minecraft.world.entity.monster.piglin.StopAdmiringIfTiredOfTryingToReachItem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;
import java.util.Random;

public class BeggarSurvivorAI {
    private static final UniformInt RETREAT_DURATION = TimeUtil.rangeOfSeconds(5, 20);
    private static final UniformInt AVOID_ZOMBIE_DURATION = TimeUtil.rangeOfSeconds(5, 7);

    public static Brain<?> makeBrain(BeggarSurvivor p_34841_, Brain<BeggarSurvivor> p_34842_) {
        initCoreActivity(p_34842_);
        initIdleActivity(p_34842_);
        initAdmireItemActivity(p_34842_);
        initRetreatActivity(p_34842_);
        p_34842_.setCoreActivities(ImmutableSet.of(Activity.CORE));
        p_34842_.setDefaultActivity(Activity.IDLE);
        p_34842_.useDefaultActivity();
        return p_34842_;
    }
    private final SimpleContainer inventory = new SimpleContainer(8);
    public static void initMemories(BeggarSurvivor p_35095_) {
        GlobalPos globalpos = GlobalPos.of(p_35095_.level().dimension(), p_35095_.blockPosition());
        p_35095_.getBrain().setMemory(MemoryModuleType.HOME, globalpos);
    }

    private static void initCoreActivity(Brain<BeggarSurvivor> p_34821_) {
        p_34821_.addActivity(Activity.CORE, 0, ImmutableList.of(new LookAtTargetSink(45, 90), new MoveToTargetSink(), InteractWithDoor.create(), avoidZombie(), avoidNemesis(), StartBegging.create(), StopBegging.create(), StopBeggingWhenFleeing.create(),StopHoldingItemIfNoLongerAdmiring.create(), StartAdmiringItemIfSeen.create(200)));
    }

    private static void initIdleActivity(Brain<BeggarSurvivor> p_34892_) {
        p_34892_.addActivity(Activity.IDLE, 10, ImmutableList.of(SetEntityLookTargetSometimes.create(8.0F, UniformInt.of(30, 60)), createIdleMovementBehaviors(), SetLookAndInteract.create(EntityType.PLAYER, 4)));
    }

    private static void initAdmireItemActivity(Brain<BeggarSurvivor> p_34941_) {
        p_34941_.addActivityAndRemoveMemoryWhenStopped(Activity.ADMIRE_ITEM, 10, ImmutableList.of(GoToWantedItem.create(BeggarSurvivorAI::isNotHoldingNeededItemInOffHand, 1.0F, true, 9), StopAdmiringIfItemTooFarAway.create(9), StopAdmiringIfTiredOfTryingToReachItem.create(200, 200)), MemoryModuleType.ADMIRING_ITEM);
    }

    private static void initRetreatActivity(Brain<BeggarSurvivor> p_34959_) {
        p_34959_.addActivityAndRemoveMemoryWhenStopped(Activity.AVOID, 10, ImmutableList.of(SetWalkTargetAwayFrom.entity(MemoryModuleType.AVOID_TARGET, 1.0F, 12, true), createIdleMovementBehaviors(), EraseMemoryIf.<BeggarSurvivor>create(BeggarSurvivorAI::wantsToStopFleeing, MemoryModuleType.AVOID_TARGET)), MemoryModuleType.AVOID_TARGET);
    }

    private static RunOne<BeggarSurvivor> createIdleMovementBehaviors() {
        return new RunOne<>(ImmutableList.of(Pair.of(RandomStroll.stroll(0.6F), 2), Pair.of(StrollToPoi.create(MemoryModuleType.HOME, 0.6F, 2, 100), 2), Pair.of(StrollAroundPoi.create(MemoryModuleType.HOME, 0.6F, 5), 2), Pair.of(new DoNothing(30, 60), 1)));
    }

    private static BehaviorControl<BeggarSurvivor> avoidZombie() {
        return CopyMemoryWithExpiry.create(BeggarSurvivorAI::isNearZombie, SASMemoryModules.NEAREST_VISIBLE_ZOMBIE.get(), MemoryModuleType.AVOID_TARGET, AVOID_ZOMBIE_DURATION);
    }

    public static void updateActivity(BeggarSurvivor p_34899_) {
        Brain<BeggarSurvivor> brain = p_34899_.getBrain();
        Activity activity = brain.getActiveNonCoreActivity().orElse((Activity) null);
        brain.setActiveActivityToFirstValid(ImmutableList.of(Activity.ADMIRE_ITEM, Activity.AVOID,  Activity.IDLE));
        Activity activity1 = brain.getActiveNonCoreActivity().orElse((Activity) null);
        if (activity != activity1) {
            maybePlayActivitySound(p_34899_);
        }
    }


    public static void pickUpItem(BeggarSurvivor p_34844_, ItemEntity p_34845_) {
        stopWalking(p_34844_);
        ItemStack itemstack;
        itemstack = p_34845_.getItem();
        if (p_34844_.doesSurvivorNeedItem(itemstack)) {
            p_34844_.take(p_34845_, 1);
            itemstack = removeOneItemFromItemEntity(p_34845_);
            p_34844_.setBegging(false);
            p_34844_.getBrain().eraseMemory(MemoryModuleType.TIME_TRYING_TO_REACH_ADMIRE_ITEM);
            holdInOffhand(p_34844_, itemstack);
            admireGiftItem(p_34844_);
        } else if (isFood(itemstack) && !hasEatenRecently(p_34844_)) {
            eat(p_34844_);
        }
    }

    private static void holdInOffhand(BeggarSurvivor p_34933_, ItemStack p_34934_) {

            if (isHoldingItemInOffHand(p_34933_)) {
                p_34933_.spawnAtLocation(p_34933_.getItemInHand(InteractionHand.OFF_HAND));
            }

            p_34933_.holdInOffHand(p_34934_);

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

    public static void stopHoldingOffHandItem(BeggarSurvivor p_34868_, boolean p_34869_) {
        ItemStack itemstack = p_34868_.getItemInHand(InteractionHand.OFF_HAND);
        p_34868_.setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);
        boolean flag = p_34868_.doesSurvivorNeedItem(itemstack);
        if (p_34869_ && flag) {

            throwItem(p_34868_, getBoxToGift(p_34868_));
            putInInventory(p_34868_, itemstack);
        } else if (!flag) {
                putInInventory(p_34868_, itemstack);

        }
    }


    private static void putInInventory(BeggarSurvivor p_34953_, ItemStack p_34954_) {
        ItemStack itemstack = p_34953_.addToInventory(p_34954_);
        throwItemTowardRandomPos(p_34953_, itemstack);
    }

    private static void throwItem(BeggarSurvivor p_34861_, ItemStack boxToThrow) {
        Optional<Player> optional = p_34861_.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_PLAYER);
        if (optional.isPresent()) {
            throwItemTowardPlayer(p_34861_, optional.get(), boxToThrow);
        } else {
            throwItemTowardRandomPos(p_34861_, boxToThrow);
        }

    }

    private static void throwItemTowardRandomPos(BeggarSurvivor p_34913_, ItemStack itemStack) {
        throwItemTowardPos(p_34913_, itemStack, getRandomNearbyPos(p_34913_));
    }

    private static void throwItemTowardPlayer(BeggarSurvivor p_34851_, Player p_34852_, ItemStack itemStack) {
        throwItemTowardPos(p_34851_, itemStack, p_34852_.position());
    }

    private static void throwItemTowardPos(BeggarSurvivor p_34864_, ItemStack itemStack, Vec3 p_34866_) {
        if(!itemStack.isEmpty()) {
            p_34864_.swing(InteractionHand.OFF_HAND);
            BehaviorUtils.throwItem(p_34864_, itemStack, p_34866_.add(0.0D, 1.0D, 0.0D));
        }
    }

    private static ItemStack getBoxToGift(BeggarSurvivor p_34997_) {
        Random random = new Random();
        int rand = random.nextInt(3);
        switch (rand) {
            case 2 -> {
                return new ItemStack(SASItems.GREEN_BOX_OF_STUFF.get());

            }
            case 1 -> {
                    return new ItemStack(SASItems.GREY_BOX_OF_STUFF.get());

            }
            default -> {
                return new ItemStack(SASItems.ORANGE_BOX_OF_STUFF.get());
            }
        }
    }

    public static boolean wantsToPickup(BeggarSurvivor p_34858_, ItemStack p_34859_) {
        if (isAdmiringDisabled(p_34858_) && p_34858_.getBrain().hasMemoryValue(MemoryModuleType.ATTACK_TARGET)) {
            return false;
        } else if (p_34858_.doesSurvivorNeedItem(p_34859_)) {
            return isNotHoldingNeededItemInOffHand(p_34858_);
        } else {
            boolean flag = p_34858_.canAddToInventory(p_34859_);
            if (p_34859_.is(Items.ROTTEN_FLESH)) {
                return flag;
            } else if (!p_34858_.doesSurvivorNeedItem(p_34859_)) {
                return p_34858_.canReplaceCurrentItem(p_34859_);
            } else {
                return isNotHoldingNeededItemInOffHand(p_34858_) && flag;
            }
        }
    }

    public static boolean isNeededItem(ItemStack p_149966_) {
        return SASUtil.doSurvivorsFindItemUseful(p_149966_);
    }

    private static boolean isNearZombie(BeggarSurvivor p_34999_) {
        Brain<BeggarSurvivor> brain = p_34999_.getBrain();
        if (brain.hasMemoryValue(SASMemoryModules.NEAREST_VISIBLE_ZOMBIE.get())) {
            LivingEntity livingentity = brain.getMemory(SASMemoryModules.NEAREST_VISIBLE_ZOMBIE.get()).get();
            return p_34999_.closerThan(livingentity, 6.0D);
        } else {
            return false;
        }
    }

    public static InteractionResult mobInteract(BeggarSurvivor p_34847_, Player p_34848_, InteractionHand p_34849_) {
        ItemStack itemstack = p_34848_.getItemInHand(p_34849_);
        if (canAdmire(p_34847_, itemstack)) {
            p_34847_.setBegging(false);
            ItemStack itemstack1 = itemstack.split(1);
            holdInOffhand(p_34847_, itemstack1);
            admireGiftItem(p_34847_);
            stopWalking(p_34847_);
            return InteractionResult.CONSUME;
        } else {
            return InteractionResult.PASS;
        }
    }

    public static boolean canAdmire(BeggarSurvivor p_34910_, ItemStack p_34911_) {
        return !isAdmiringDisabled(p_34910_) && !isAdmiringItem(p_34910_) && p_34910_.doesSurvivorNeedItem(p_34911_);
    }

    public static void maybePlayActivitySound(BeggarSurvivor p_35115_) {
        if ((double)p_35115_.level().random.nextFloat() < 0.0125D) {
            playActivitySound(p_35115_);
        }

    }

    private static void playActivitySound(BeggarSurvivor p_35123_) {
        p_35123_.getBrain().getActiveNonCoreActivity().ifPresent((p_35104_) -> {
            if (p_35104_ == Activity.AVOID) {
                p_35123_.playSound(SoundEvents.VILLAGER_NO);
            }

        });
    }

    private static BehaviorControl<BeggarSurvivor> avoidNemesis() {
        return CopyMemoryWithExpiry.create((p_289472_) -> { return true; }, MemoryModuleType.NEAREST_VISIBLE_NEMESIS, MemoryModuleType.AVOID_TARGET, AVOID_ZOMBIE_DURATION);
    }

    private static void stopWalking(BeggarSurvivor p_35007_) {
        p_35007_.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
        p_35007_.getNavigation().stop();
    }

    private static boolean wantsToStopFleeing(BeggarSurvivor p_35009_) {
        Brain<BeggarSurvivor> brain = p_35009_.getBrain();
        if (!brain.hasMemoryValue(MemoryModuleType.AVOID_TARGET)) {
            return true;
        } else {
            LivingEntity livingentity = brain.getMemory(MemoryModuleType.AVOID_TARGET).get();
            EntityType<?> entitytype = livingentity.getType();
            return entitytype == EntityType.ZOMBIE;
        }
    }

    private static void setAvoidTarget(AbstractSurvivor p_34968_, LivingEntity p_34969_) {
        p_34968_.getBrain().setMemoryWithExpiry(MemoryModuleType.AVOID_TARGET, p_34969_, (long)RETREAT_DURATION.sample(p_34968_.level().random));
    }

    private static void eat(BeggarSurvivor p_35015_) {
        p_35015_.getBrain().setMemoryWithExpiry(MemoryModuleType.ATE_RECENTLY, true, 200L);
    }

    private static Vec3 getRandomNearbyPos(BeggarSurvivor p_35017_) {
        Vec3 vec3 = LandRandomPos.getPos(p_35017_, 4, 2);
        return vec3 == null ? p_35017_.position() : vec3;
    }

    private static boolean hasEatenRecently(BeggarSurvivor p_35019_) {
        return p_35019_.getBrain().hasMemoryValue(MemoryModuleType.ATE_RECENTLY);
    }

    public static boolean isIdle(AbstractSurvivor p_34943_) {
        return p_34943_.getBrain().isActive(Activity.IDLE);
    }

    private static void admireGiftItem(LivingEntity p_34939_) {
        p_34939_.getBrain().setMemoryWithExpiry(MemoryModuleType.ADMIRING_ITEM, true, 120L);
    }

    private static boolean isAdmiringItem(BeggarSurvivor p_35021_) {
        return p_35021_.getBrain().hasMemoryValue(MemoryModuleType.ADMIRING_ITEM);
    }

    private static boolean isFood(ItemStack p_149970_) {
        return p_149970_.is(ItemTags.PIGLIN_FOOD);
    }

    public static boolean seesPlayer(LivingEntity p_34972_) {
        return p_34972_.getBrain().hasMemoryValue(MemoryModuleType.NEAREST_PLAYER_HOLDING_WANTED_ITEM);
    }

    private static boolean doesntSeeAnyPlayer(LivingEntity p_34983_) {
        return !seesPlayer(p_34983_);
    }

    private static boolean isAdmiringDisabled(BeggarSurvivor p_35025_) {
        return p_35025_.getBrain().hasMemoryValue(MemoryModuleType.ADMIRING_DISABLED);
    }

    private static boolean isHoldingItemInOffHand(BeggarSurvivor p_35027_) {
        return !p_35027_.getOffhandItem().isEmpty();
    }

    private static boolean isNotHoldingNeededItemInOffHand(BeggarSurvivor p_35029_) {
        return p_35029_.getOffhandItem().isEmpty() || !p_35029_.doesSurvivorNeedItem(p_35029_.getOffhandItem());
    }

    public static void wasHurtBy(BeggarSurvivor beggarSurvivor, LivingEntity entity) {
        setAvoidTarget(beggarSurvivor, entity);
    }

}
