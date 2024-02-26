package de.nulldrei.saintsandsinners.entity.neutral;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Dynamic;
import de.nulldrei.saintsandsinners.SASUtil;
import de.nulldrei.saintsandsinners.SaintsAndSinners;
import de.nulldrei.saintsandsinners.entity.ai.memory.SASMemoryModules;
import de.nulldrei.saintsandsinners.entity.ai.memory.sensors.SASSensorTypes;
import de.nulldrei.saintsandsinners.entity.ai.survivor.RobberSurvivorAI;
import de.nulldrei.saintsandsinners.entity.animations.pose.SurvivorArmPose;
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
import net.minecraft.world.entity.npc.InventoryCarrier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PlayerHeadItem;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Random;

public class Decapitated extends Monster  {

        public static final EntityDataAccessor<String> DATA_VARIANT = SynchedEntityData.defineId(Decapitated.class, EntityDataSerializers.STRING);

        public Decapitated(EntityType<? extends Decapitated> p_35048_, Level p_35049_) {
            super(p_35048_, p_35049_);
            this.xpReward = 5;
        }

        public static AttributeSupplier.Builder createAttributes() {
            return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 0).add(Attributes.MOVEMENT_SPEED, 0).add(Attributes.ATTACK_DAMAGE, 0);
        }

        @Nullable
        public SpawnGroupData finalizeSpawn(ServerLevelAccessor p_35058_, DifficultyInstance p_35059_, MobSpawnType p_35060_, @Nullable SpawnGroupData p_35061_, @Nullable CompoundTag p_35062_) {
            //setVariant("survivor:ben");
            setVariant("zombie");
            kill();
            return super.finalizeSpawn(p_35058_, p_35059_, p_35060_, p_35061_, p_35062_);
        }


        public void addAdditionalSaveData(CompoundTag p_34751_) {
            super.addAdditionalSaveData(p_34751_);
            p_34751_.putString("Variant", this.getVariant());
        }

        public void readAdditionalSaveData(CompoundTag p_34725_) {
            super.readAdditionalSaveData(p_34725_);
            setVariant(p_34725_.getString("Variant"));
        }


        protected void defineSynchedData() {
            this.getEntityData().define(DATA_VARIANT, "");
            super.defineSynchedData();
        }


        public void setVariant(String p_262689_) {
            getEntityData().set(DATA_VARIANT, p_262689_);
        }

        public @NotNull String getVariant() {
            return getEntityData().get(DATA_VARIANT);
        }

        protected void dropCustomDeathLoot(DamageSource p_34697_, int p_34698_, boolean p_34699_) {
            super.dropCustomDeathLoot(p_34697_, p_34698_, p_34699_);
            // TODO // Implement drop of Survivor Heads if killed with cleaver or katana

        }

}
