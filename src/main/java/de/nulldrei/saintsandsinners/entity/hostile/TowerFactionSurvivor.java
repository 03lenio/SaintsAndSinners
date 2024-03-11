package de.nulldrei.saintsandsinners.entity.hostile;

import de.nulldrei.saintsandsinners.SaintsAndSinners;
import de.nulldrei.saintsandsinners.entity.animations.pose.SurvivorArmPose;
import de.nulldrei.saintsandsinners.entity.hostile.variant.TowerVariant;
import de.nulldrei.saintsandsinners.entity.neutral.RobberSurvivor;
import de.nulldrei.saintsandsinners.entity.neutral.variant.Variant;
import de.nulldrei.saintsandsinners.item.SASItems;
import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.CrossbowAttackMob;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class TowerFactionSurvivor extends AbstractFactionSurvivor implements VariantHolder<TowerVariant>, CrossbowAttackMob {

    private static final EntityDataAccessor<Boolean> DATA_IS_CHARGING_CROSSBOW = SynchedEntityData.defineId(TowerFactionSurvivor.class, EntityDataSerializers.BOOLEAN);

    public static final EntityDataAccessor<Integer> DATA_ID_TYPE_VARIANT = SynchedEntityData.defineId(TowerFactionSurvivor.class, EntityDataSerializers.INT);

    public TowerFactionSurvivor(EntityType<? extends AbstractFactionSurvivor> p_35048_, Level p_35049_) {
        super(p_35048_, p_35049_);
    }

    protected void populateDefaultEquipmentSlots(RandomSource p_219189_, DifficultyInstance p_219190_) {
        maybeWearArmor(EquipmentSlot.HEAD, new ItemStack(SASItems.TOWER_HELMET.get()), p_219189_);
        maybeWearArmor(EquipmentSlot.CHEST, new ItemStack(SASItems.TOWER_VEST.get()), p_219189_);
    }

    protected void dropCustomDeathLoot(DamageSource p_34697_, int p_34698_, boolean p_34699_) {
        super.dropCustomDeathLoot(p_34697_, p_34698_, p_34699_);
        Entity entity = p_34697_.getEntity();
        if (entity instanceof Creeper creeper) {
            if (creeper.canDropMobsSkull()) {
                ItemStack itemStack;
                switch (getVariant().getSerializedName()) {
                    case "missy":
                        itemStack = new ItemStack(SASItems.MISSY_HEAD.get());
                        break;
                    case "abraham":
                        itemStack = new ItemStack(SASItems.ABRAHAM_HEAD.get());
                        break;
                    default:
                        itemStack = new ItemStack(SASItems.JOE_HEAD.get());
                        break;
                }
                creeper.increaseDroppedSkulls();
                this.spawnAtLocation(itemStack);
            }
        }
    }



    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor p_35058_, DifficultyInstance p_35059_, MobSpawnType p_35060_, @Nullable SpawnGroupData p_35061_, @Nullable CompoundTag p_35062_) {
        RandomSource randomsource = p_35058_.getRandom();
        this.setItemSlot(EquipmentSlot.MAINHAND, this.createSpawnWeapon());
        populateDefaultEquipmentSlots(randomsource, p_35059_);
        setVariant(Util.getRandom(TowerVariant.values(), p_35058_.getRandom()));
        return super.finalizeSpawn(p_35058_, p_35059_, p_35060_, p_35061_, p_35062_);
    }

    @Override
    protected void defineSynchedData() {
        getEntityData().define(DATA_IS_CHARGING_CROSSBOW, Boolean.FALSE);
        getEntityData().define(DATA_ID_TYPE_VARIANT, 0);
        super.defineSynchedData();
    }

    public void addAdditionalSaveData(CompoundTag p_34751_) {
        super.addAdditionalSaveData(p_34751_);
        p_34751_.putInt("Variant", this.getTypeVariant());
    }

    public void readAdditionalSaveData(CompoundTag p_34725_) {
        super.readAdditionalSaveData(p_34725_);
        setTypeVariant(p_34725_.getInt("Variant"));
    }

    private void setTypeVariant(int typeVariant) {
        this.entityData.set(DATA_ID_TYPE_VARIANT, typeVariant);
    }

    private int getTypeVariant() {
        return this.entityData.get(DATA_ID_TYPE_VARIANT);
    }

    @Override
    public void setVariant(TowerVariant p_262689_) {
        this.setTypeVariant(p_262689_.getId() & 255 | this.getTypeVariant() & -256);
    }

    @Override
    public TowerVariant getVariant() {
        return TowerVariant.byId(this.getTypeVariant() & 255);
    }


    private ItemStack createSpawnWeapon() {
        ItemStack meleeWeapon;
        Random random = new Random();
        if(random.nextBoolean()) {
            meleeWeapon = new ItemStack(SASItems.NIGHTSHIFT.get());
        } else {
            meleeWeapon = new ItemStack(SASItems.NATIONAL_GUARD_KNIFE.get());
        }

        return (double)this.random.nextFloat() < 0.5D ? new ItemStack(Items.CROSSBOW) : meleeWeapon;
    }

    public SurvivorArmPose getArmPose() {
        if (this.isAggressive() && this.isHoldingMeleeWeapon()) {
            return SurvivorArmPose.ATTACKING_WITH_MELEE_WEAPON;
        } else if (this.isChargingCrossbow()) {
            return SurvivorArmPose.CROSSBOW_CHARGE;
        } else {
            return this.isAggressive() && this.isHolding(is -> is.getItem() instanceof net.minecraft.world.item.CrossbowItem) ? SurvivorArmPose.CROSSBOW_HOLD : SurvivorArmPose.DEFAULT;
        }
    }

    @Override
    public void setChargingCrossbow(boolean p_32339_) {
        this.entityData.set(DATA_IS_CHARGING_CROSSBOW, p_32339_);
    }

    @Override
    public void shootCrossbowProjectile(LivingEntity p_34707_, ItemStack p_34708_, Projectile p_34709_, float p_34710_) {
        this.shootCrossbowProjectile(this, p_34707_, p_34709_, p_34710_, 1.6F);
    }

    @Override
    public void onCrossbowAttackPerformed() {
        this.noActionTime = 0;
    }

    @Override
    public void performRangedAttack(LivingEntity p_34704_, float p_34705_) {
        this.performCrossbowAttack(this, 1.6F);
    }

    @Override
    public boolean canFireProjectileWeapon(ProjectileWeaponItem p_34715_) {
        return p_34715_ == Items.CROSSBOW;
    }

    private boolean isChargingCrossbow() {
        return this.entityData.get(DATA_IS_CHARGING_CROSSBOW);
    }


}
