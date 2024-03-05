package de.nulldrei.saintsandsinners.entity.dead;

import de.nulldrei.saintsandsinners.entity.SASEntities;
import de.nulldrei.saintsandsinners.item.SASItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class Decapitated extends Monster  {

        public static final EntityDataAccessor<String> DATA_VARIANT = SynchedEntityData.defineId(Decapitated.class, EntityDataSerializers.STRING);

        public Decapitated(EntityType p_35048_, Level p_35049_) {
            super(p_35048_, p_35049_);
            this.xpReward = 5;
        }

        public Decapitated(Level p_34274_) {
            this(SASEntities.DECAPITATED.get(), p_34274_);
        }


    public static AttributeSupplier.Builder createAttributes() {
            return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 0).add(Attributes.MOVEMENT_SPEED, 0).add(Attributes.ATTACK_DAMAGE, 0);
        }

        @Nullable
        public SpawnGroupData finalizeSpawn(ServerLevelAccessor p_35058_, DifficultyInstance p_35059_, MobSpawnType p_35060_, @Nullable SpawnGroupData p_35061_, @Nullable CompoundTag p_35062_) {
            //setVariant("survivor:ben");
            //setVariant("survivor:abraham");
            kill();
            return super.finalizeSpawn(p_35058_, p_35059_, p_35060_, p_35061_, p_35062_);
        }

    protected void dropCustomDeathLoot(DamageSource p_34697_, int p_34698_, boolean p_34699_) {
        super.dropCustomDeathLoot(p_34697_, p_34698_, p_34699_);
        ItemStack itemStack = ItemStack.EMPTY;
        if(getVariant().startsWith("survivor")) {
            switch (getVariant().toLowerCase().split(":")[1]) {
                case "abraham":
                    itemStack = new ItemStack(SASItems.ABRAHAM_HEAD.get());
                    break;
                case "ben":
                    itemStack = new ItemStack(SASItems.BEN_HEAD.get());
                    break;
                case "georgia":
                    itemStack = new ItemStack(SASItems.GEORGIA_HEAD.get());
                    break;
                case "jesse":
                    itemStack = new ItemStack(SASItems.JESSE_HEAD.get());
                    break;
                case "joe":
                    itemStack = new ItemStack(SASItems.JOE_HEAD.get());
                    break;
                case "missy":
                    itemStack = new ItemStack(SASItems.MISSY_HEAD.get());
                    break;
                case "osama":
                    itemStack = new ItemStack(SASItems.OSAMA_HEAD.get());
                    break;
                case "patrick":
                    itemStack = new ItemStack(SASItems.PATRICK_HEAD.get());
                    break;
                case "randy":
                    itemStack = new ItemStack(SASItems.RANDY_HEAD.get());
                    break;
                case "rick":
                    itemStack = new ItemStack(SASItems.RICK_HEAD.get());
                    break;
                case "tom":
                    itemStack = new ItemStack(SASItems.TOM_HEAD.get());
                    break;
                case "walter":
                    itemStack = new ItemStack(SASItems.WALTER_HEAD.get());
                    break;
            }
        }
        else {
            itemStack = new ItemStack(Items.ZOMBIE_HEAD);
        }
        this.spawnAtLocation(itemStack);
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

}
