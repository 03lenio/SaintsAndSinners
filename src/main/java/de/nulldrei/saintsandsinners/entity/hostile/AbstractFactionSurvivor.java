package de.nulldrei.saintsandsinners.entity.hostile;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Dynamic;
import de.nulldrei.saintsandsinners.SASUtil;
import de.nulldrei.saintsandsinners.SaintsAndSinners;
import de.nulldrei.saintsandsinners.entity.ai.memory.SASMemoryModules;
import de.nulldrei.saintsandsinners.entity.ai.memory.sensors.SASSensorTypes;
import de.nulldrei.saintsandsinners.entity.ai.survivor.FactionSurvivorAI;
import de.nulldrei.saintsandsinners.entity.ai.survivor.RobberSurvivorAI;
import de.nulldrei.saintsandsinners.entity.animations.pose.SurvivorArmPose;
import de.nulldrei.saintsandsinners.entity.neutral.AbstractSurvivor;
import de.nulldrei.saintsandsinners.entity.neutral.RobberSurvivor;
import de.nulldrei.saintsandsinners.entity.neutral.variant.Variant;
import de.nulldrei.saintsandsinners.item.SASItems;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.CrossbowAttackMob;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.piglin.PiglinArmPose;
import net.minecraft.world.entity.npc.InventoryCarrier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Random;

public abstract class AbstractFactionSurvivor extends AbstractSurvivor {
    private static final int MAX_HEALTH = 50;
    private static final float MOVEMENT_SPEED_WHEN_FIGHTING = 0.35F;
    private static final int ATTACK_DAMAGE = 7;
    protected static final ImmutableList<SensorType<? extends Sensor<? super AbstractFactionSurvivor>>> SENSOR_TYPES = ImmutableList.of(SensorType.NEAREST_LIVING_ENTITIES, SensorType.NEAREST_PLAYERS, SensorType.NEAREST_ITEMS, SensorType.HURT_BY, SASSensorTypes.FACTION_SURVIVOR_SPECIFIC_SENSOR.get());
    protected static final ImmutableList<MemoryModuleType<?>> MEMORY_TYPES = ImmutableList.of(MemoryModuleType.LOOK_TARGET, MemoryModuleType.AVOID_TARGET, MemoryModuleType.DOORS_TO_CLOSE, MemoryModuleType.NEAREST_LIVING_ENTITIES, MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, MemoryModuleType.NEAREST_VISIBLE_PLAYER, MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER, MemoryModuleType.NEAREST_VISIBLE_ADULT_PIGLINS, MemoryModuleType.NEARBY_ADULT_PIGLINS, MemoryModuleType.HURT_BY, MemoryModuleType.HURT_BY_ENTITY, MemoryModuleType.WALK_TARGET, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryModuleType.ATTACK_TARGET, MemoryModuleType.ATTACK_COOLING_DOWN, MemoryModuleType.INTERACTION_TARGET, MemoryModuleType.PATH, MemoryModuleType.ANGRY_AT, MemoryModuleType.NEAREST_VISIBLE_NEMESIS, MemoryModuleType.HOME, SASMemoryModules.NEARBY_SURVIVORS.get(), SASMemoryModules.NEAREST_VISIBLE_ZOMBIE.get(), SASMemoryModules.VISIBLE_SURVIVOR_COUNT.get(), SASMemoryModules.VISIBLE_ZOMBIE_COUNT.get(), SASMemoryModules.NEAREST_RIVALING_SURVIVOR.get());

    public AbstractFactionSurvivor(EntityType<? extends AbstractFactionSurvivor> p_35048_, Level p_35049_) {
        super(p_35048_, p_35049_);
        this.xpReward = 20;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 50.0D).add(Attributes.MOVEMENT_SPEED, (double)0.35F).add(Attributes.ATTACK_DAMAGE, 7.0D);
    }

    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor p_35058_, DifficultyInstance p_35059_, MobSpawnType p_35060_, @Nullable SpawnGroupData p_35061_, @Nullable CompoundTag p_35062_) {
        FactionSurvivorAI.initMemories(this);
        return super.finalizeSpawn(p_35058_, p_35059_, p_35060_, p_35061_, p_35062_);
    }

    protected void populateDefaultEquipmentSlots(RandomSource p_219209_, DifficultyInstance p_219210_) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.GOLDEN_AXE));
    }

    protected Brain.Provider<AbstractFactionSurvivor> brainProvider() {
        return Brain.provider(MEMORY_TYPES, SENSOR_TYPES);
    }

    protected Brain<?> makeBrain(Dynamic<?> p_35064_) {
        return FactionSurvivorAI.makeBrain(this, this.brainProvider().makeBrain(p_35064_));
    }

    public Brain<AbstractFactionSurvivor> getBrain() {
        return (Brain<AbstractFactionSurvivor>)super.getBrain();
    }


    public boolean canHunt() {
        return false;
    }

    protected void maybeWearArmor(EquipmentSlot p_219192_, ItemStack p_219193_, RandomSource p_219194_) {
        if (p_219194_.nextFloat() < 0.1F) {
            this.setItemSlot(p_219192_, p_219193_);
        }

    }

    protected void customServerAiStep() {
        this.level().getProfiler().push("FactionSurvivorBrain");
        this.getBrain().tick((ServerLevel)this.level(), this);
        this.level().getProfiler().pop();
        FactionSurvivorAI.updateActivity(this);
        FactionSurvivorAI.maybePlayActivitySound(this);
        super.customServerAiStep();
    }

    public boolean hurt(DamageSource p_35055_, float p_35056_) {
        boolean flag = super.hurt(p_35055_, p_35056_);
        if (this.level().isClientSide) {
            return false;
        } else {
            if (flag && p_35055_.getEntity() instanceof LivingEntity) {
                FactionSurvivorAI.wasHurtBy(this, (LivingEntity)p_35055_.getEntity());
            }

            return flag;
        }
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.PIGLIN_BRUTE_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource p_35072_) {
        return SoundEvents.PIGLIN_BRUTE_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.PIGLIN_BRUTE_DEATH;
    }

    protected void playStepSound(BlockPos p_35066_, BlockState p_35067_) {
        this.playSound(SoundEvents.PIGLIN_BRUTE_STEP, 0.15F, 1.0F);
    }


}
