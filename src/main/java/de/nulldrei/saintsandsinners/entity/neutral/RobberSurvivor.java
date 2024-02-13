package de.nulldrei.saintsandsinners.entity.neutral;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Dynamic;
import de.nulldrei.saintsandsinners.SASUtil;
import de.nulldrei.saintsandsinners.SaintsAndSinners;
import de.nulldrei.saintsandsinners.entity.ai.memory.SASMemoryModules;
import de.nulldrei.saintsandsinners.entity.ai.memory.sensors.SASSensorTypes;
import de.nulldrei.saintsandsinners.entity.ai.survivor.BeggarSurvivorAI;
import de.nulldrei.saintsandsinners.entity.ai.survivor.RobberSurvivorAI;
import de.nulldrei.saintsandsinners.entity.animations.pose.SurvivorArmPose;
import de.nulldrei.saintsandsinners.entity.neutral.variant.Variant;
import de.nulldrei.saintsandsinners.item.SASItems;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.data.worldgen.PillagerOutpostPools;
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
import net.minecraft.world.entity.projectile.Arrow;
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

public class RobberSurvivor extends AbstractSurvivor implements CrossbowAttackMob, InventoryCarrier,VariantHolder<Variant> {

        private final SimpleContainer inventory = new SimpleContainer(8);
        private static final int MAX_HEALTH = 50;
        private static final float MOVEMENT_SPEED_WHEN_FIGHTING = 0.35F;
        private static final int ATTACK_DAMAGE = 7;
        private static final EntityDataAccessor<ItemStack> DATA_ITEM = SynchedEntityData.defineId(RobberSurvivor.class, EntityDataSerializers.ITEM_STACK);
        public static final EntityDataAccessor<Integer> DATA_ID_TYPE_VARIANT = SynchedEntityData.defineId(RobberSurvivor.class, EntityDataSerializers.INT);
        private static final EntityDataAccessor<Boolean> DATA_IS_CHARGING_CROSSBOW = SynchedEntityData.defineId(RobberSurvivor.class, EntityDataSerializers.BOOLEAN);
        private static final EntityDataAccessor<Boolean> DATA_DEMANDING = SynchedEntityData.defineId(RobberSurvivor.class, EntityDataSerializers.BOOLEAN);
        private static final EntityDataAccessor<Boolean> DATA_ACTIVELY_DEMANDING = SynchedEntityData.defineId(RobberSurvivor.class, EntityDataSerializers.BOOLEAN);

        public static final EntityDataAccessor<Integer> DATA_TICKS_UNTIL_ANGRY = SynchedEntityData.defineId(RobberSurvivor.class, EntityDataSerializers.INT);
        private static final EntityDataAccessor<Boolean> DATA_ITEM_RECEIVED = SynchedEntityData.defineId(RobberSurvivor.class, EntityDataSerializers.BOOLEAN);
        private static final EntityDataAccessor<Boolean> DATA_IS_FED_UP = SynchedEntityData.defineId(RobberSurvivor.class, EntityDataSerializers.BOOLEAN);
        protected static final ImmutableList<SensorType<? extends Sensor<? super RobberSurvivor>>> SENSOR_TYPES = ImmutableList.of(SensorType.NEAREST_LIVING_ENTITIES, SensorType.NEAREST_PLAYERS, SensorType.NEAREST_ITEMS, SensorType.HURT_BY, SASSensorTypes.ROBBER_SURVIVOR_SPECIFIC_SENSOR.get());
        protected static final ImmutableList<MemoryModuleType<?>> MEMORY_TYPES = ImmutableList.of(MemoryModuleType.LOOK_TARGET, MemoryModuleType.DOORS_TO_CLOSE, MemoryModuleType.NEAREST_LIVING_ENTITIES, MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, MemoryModuleType.NEAREST_VISIBLE_PLAYER, MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER, MemoryModuleType.ITEM_PICKUP_COOLDOWN_TICKS, MemoryModuleType.ADMIRING_ITEM, MemoryModuleType.ADMIRING_DISABLED, MemoryModuleType.DISABLE_WALK_TO_ADMIRE_ITEM, MemoryModuleType.TIME_TRYING_TO_REACH_ADMIRE_ITEM, MemoryModuleType.NEAREST_VISIBLE_ADULT_PIGLINS, MemoryModuleType.NEARBY_ADULT_PIGLINS, MemoryModuleType.HURT_BY, MemoryModuleType.HURT_BY_ENTITY, MemoryModuleType.WALK_TARGET, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryModuleType.ATTACK_TARGET, MemoryModuleType.ATTACK_COOLING_DOWN, MemoryModuleType.INTERACTION_TARGET, MemoryModuleType.PATH, MemoryModuleType.ANGRY_AT, MemoryModuleType.NEAREST_VISIBLE_NEMESIS, MemoryModuleType.HOME, MemoryModuleType.AVOID_TARGET, SASMemoryModules.IS_DEMANDING.get(), SASMemoryModules.VISIBLE_ZOMBIE_COUNT.get(), SASMemoryModules.NEAREST_VISIBLE_ZOMBIE.get());

        public RobberSurvivor(EntityType<? extends RobberSurvivor> p_35048_, Level p_35049_) {
            super(p_35048_, p_35049_);
            this.xpReward = 20;
        }

        public static AttributeSupplier.Builder createAttributes() {
            return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 50.0D).add(Attributes.MOVEMENT_SPEED, (double)0.35F).add(Attributes.ATTACK_DAMAGE, 7.0D);
        }

        @Nullable
        public SpawnGroupData finalizeSpawn(ServerLevelAccessor p_35058_, DifficultyInstance p_35059_, MobSpawnType p_35060_, @javax.annotation.Nullable SpawnGroupData p_35061_, @Nullable CompoundTag p_35062_) {
            ItemStack neededItem = generateNeededItem();
            RobberSurvivorAI.initMemories(this);
            this.getBrain().setMemory(SASMemoryModules.IS_DEMANDING.get(), Boolean.TRUE);
            setVariant(Util.getRandom(Variant.values(), p_35058_.getRandom()));
            this.getEntityData().set(DATA_ITEM, neededItem);
            SASUtil.demandedItems.add(neededItem.toString());
            this.setItemSlot(EquipmentSlot.MAINHAND, this.createSpawnWeapon());
            return super.finalizeSpawn(p_35058_, p_35059_, p_35060_, p_35061_, p_35062_);
        }


        public void addAdditionalSaveData(CompoundTag p_34751_) {
            super.addAdditionalSaveData(p_34751_);
            p_34751_.putInt("Variant", this.getTypeVariant());
            p_34751_.putInt(SaintsAndSinners.MODID + ":ticksUntilAngry", getTicksUntilAngry());
            p_34751_.putBoolean(SaintsAndSinners.MODID + ":isDemanding", isDemanding());
            p_34751_.putBoolean(SaintsAndSinners.MODID + ":hasReceivedItem", hasReceivedItem());
            p_34751_.putBoolean(SaintsAndSinners.MODID + ":isFedUp", isFedUp());
            if (!this.getNeededItem().isEmpty()) {
                p_34751_.put(SaintsAndSinners.MODID + ":neededItem", this.getNeededItem().save(new CompoundTag()));
            }
            this.writeInventoryToTag(p_34751_);
        }

        public void readAdditionalSaveData(CompoundTag p_34725_) {
            super.readAdditionalSaveData(p_34725_);
            if(p_34725_.contains(SaintsAndSinners.MODID + ":isDemanding")) {
                setDemanding(p_34725_.getBoolean(SaintsAndSinners.MODID + ":isDemanding"));
            }
            if(p_34725_.contains(SaintsAndSinners.MODID + ":isFedUp")) {
                setFedUp(p_34725_.getBoolean(SaintsAndSinners.MODID + ":isFedUp"));
            }
            if(p_34725_.contains(SaintsAndSinners.MODID + ":hasReceivedItem")) {
                setItemReceived(p_34725_.getBoolean(SaintsAndSinners.MODID + ":hasReceivedItem"));
            }
            CompoundTag compoundtag = p_34725_.getCompound(SaintsAndSinners.MODID + ":neededItem");
            if (!compoundtag.isEmpty()) {
                ItemStack neededItemStack = ItemStack.of(compoundtag);
                if (neededItemStack.isEmpty()) {
                    SaintsAndSinners.LOGGER.error("Unable to load item from: " + compoundtag);
                } else {
                    setNeededItem(neededItemStack);
                }
            }
            setTypeVariant(p_34725_.getInt("Variant"));
            setTicksUntilAngry(p_34725_.getInt(SaintsAndSinners.MODID + ":ticksUntilAngry"));
            this.readInventoryFromTag(p_34725_);
        }


        protected void defineSynchedData() {
            this.getEntityData().define(DATA_ID_TYPE_VARIANT, 0);
            this.getEntityData().define(DATA_IS_CHARGING_CROSSBOW, false);
            this.getEntityData().define(DATA_DEMANDING, true);
            this.getEntityData().define(DATA_ACTIVELY_DEMANDING, Boolean.FALSE);
            this.getEntityData().define(DATA_TICKS_UNTIL_ANGRY, 0);
            this.getEntityData().define(DATA_IS_FED_UP, false);
            this.getEntityData().define(DATA_ITEM_RECEIVED, false);
            this.getEntityData().define(DATA_ITEM, ItemStack.EMPTY);
            super.defineSynchedData();
        }

        public void setDemanding(boolean demanding) {
            if(demanding) {
                getBrain().setMemory(SASMemoryModules.IS_DEMANDING.get(), Boolean.TRUE);
            } else {
                getBrain().eraseMemory(SASMemoryModules.IS_DEMANDING.get());
            }
            getEntityData().set(DATA_DEMANDING, demanding);
        }

        public boolean isDemanding() {
            return getEntityData().get(DATA_DEMANDING);
        }

        public void setTicksUntilAngry(int ticks) {
            getEntityData().set(DATA_TICKS_UNTIL_ANGRY, ticks);
        }

        public int getTicksUntilAngry() {
            return getEntityData().get(DATA_TICKS_UNTIL_ANGRY);
        }

        public void setItemReceived(boolean received) {
            getEntityData().set(DATA_ITEM_RECEIVED, received);
        }

        public boolean hasReceivedItem() {
            return getEntityData().get(DATA_ITEM_RECEIVED);
        }

        public void setFedUp(boolean fedUp) {
            getEntityData().set(DATA_IS_FED_UP, fedUp);
        }

        public boolean isFedUp() {
            return getEntityData().get(DATA_IS_FED_UP);
        }

        public void setNeededItem(ItemStack neededItem) {
            getEntityData().set(DATA_ITEM, neededItem);
        }

        public ItemStack getNeededItem() {
            return this.getEntityData().get(DATA_ITEM);
        }

        public void setActivelyDemanding(boolean activelyDemanding) {
            getEntityData().set(DATA_ACTIVELY_DEMANDING, activelyDemanding);
        }

        public boolean isActivelyDemanding() {
            return this.getEntityData().get(DATA_ACTIVELY_DEMANDING);
        }

        public ItemStack generateNeededItem() {
            Random random = new Random();
            int rand = random.nextInt(5);
            switch(rand) {
                case 1 -> { return new ItemStack(Items.GUNPOWDER); }
                case 2 -> { return new ItemStack(Items.IRON_CHESTPLATE); }
                case 3 -> { return new ItemStack(Items.ARROW); }
                case 4 -> { return new ItemStack(Items.BOW); }
                default -> { return new ItemStack(SASItems.BOTTLE.get()); }
            }
        }


        protected Brain.Provider<RobberSurvivor> brainProvider() {
            return Brain.provider(MEMORY_TYPES, SENSOR_TYPES);
        }

        protected Brain<?> makeBrain(Dynamic<?> p_35064_) {
            return RobberSurvivorAI.makeBrain(this, this.brainProvider().makeBrain(p_35064_));
        }

        public Brain<RobberSurvivor> getBrain() {
            return (Brain<RobberSurvivor>)super.getBrain();
        }

        public boolean canHunt() {
            return false;
        }

        protected void customServerAiStep() {
            this.level().getProfiler().push("RobberSurvivorBrain");
            this.getBrain().tick((ServerLevel)this.level(), this);

            if(!this.level().isClientSide() && isDemanding() && getBrain().hasMemoryValue(MemoryModuleType.NEAREST_VISIBLE_PLAYER)) {
                setTicksUntilAngry(getTicksUntilAngry()+1);
            }
            System.out.println(getTicksUntilAngry());
            if(getTicksUntilAngry() >= 600 && !hasReceivedItem()) {
                setDemanding(false);
            }
            this.level().getProfiler().pop();
            RobberSurvivorAI.updateActivity(this);
            RobberSurvivorAI.maybePlayActivitySound(this);
            super.customServerAiStep();
        }

        public  SurvivorArmPose getArmPose() {
            if (this.getOffhandItem().getItem() != Items.AIR) {
                if (getOffhandItem().getItem() == getNeededItem().getItem()) {
                    setDemanding(false);
                    SASUtil.demandedItems.remove(getNeededItem().toString());
                    return SurvivorArmPose.ADMIRING_ITEM;
                }
            } else if (this.isAggressive() && this.isHoldingMeleeWeapon()) {
                return SurvivorArmPose.ATTACKING_WITH_MELEE_WEAPON;
            } else if (this.isChargingCrossbow()) {
                return SurvivorArmPose.CROSSBOW_CHARGE;
            } else if (this.isDemanding() && this.isActivelyDemanding()) {
                return SurvivorArmPose.DEMANDING_ITEM;
            } else {
                return this.isAggressive() && this.isHolding(is -> is.getItem() instanceof net.minecraft.world.item.CrossbowItem) ? SurvivorArmPose.CROSSBOW_HOLD : SurvivorArmPose.DEFAULT;
            }
            return SurvivorArmPose.DEFAULT;
        }

        public boolean hurt(DamageSource p_35055_, float p_35056_) {
            boolean flag = super.hurt(p_35055_, p_35056_);
            if (this.level().isClientSide) {
                return false;
            } else {
                if (flag && p_35055_.getEntity() instanceof LivingEntity) {
                    RobberSurvivorAI.wasHurtBy(this, (LivingEntity)p_35055_.getEntity());
                }

                return flag;
            }
        }


        public InteractionResult mobInteract(Player p_34745_, InteractionHand p_34746_) {
            if(isDemanding()) {
                InteractionResult interactionresult = super.mobInteract(p_34745_, p_34746_);
                if (interactionresult.consumesAction()) {
                    return interactionresult;
                } else if (!this.level().isClientSide) {
                    return RobberSurvivorAI.mobInteract(this, p_34745_, p_34746_);
                } else {
                    boolean flag = RobberSurvivorAI.canAdmire(this, p_34745_.getItemInHand(p_34746_)) && this.getArmPose() != SurvivorArmPose.ADMIRING_ITEM;
                    return flag ? InteractionResult.SUCCESS : InteractionResult.PASS;
                }
            } return InteractionResult.PASS;
        }


    public boolean doesSurvivorNeedItem(ItemStack itemStack) {
            return itemStack.getItem() == getNeededItem().getItem();
        }

        public void holdInOffHand(ItemStack p_34786_) {
            if (p_34786_.isPiglinCurrency()) {
                this.setItemSlot(EquipmentSlot.OFFHAND, p_34786_);
                this.setGuaranteedDrop(EquipmentSlot.OFFHAND);
            } else {
                this.setItemSlotAndDropWhenKilled(EquipmentSlot.OFFHAND, p_34786_);
            }

        }

        public boolean wantsToPickUp(ItemStack p_34777_) {
            if(isDemanding()) {
                return net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.level(), this) && this.canPickUpLoot() && RobberSurvivorAI.wantsToPickup(this, p_34777_);
            }
            return false;
        }

        protected void pickUpItem(ItemEntity p_34743_) {
            this.onItemPickup(p_34743_);
            RobberSurvivorAI.pickUpItem(this, p_34743_);
        }

        public boolean canReplaceCurrentItem(ItemStack p_34788_) {
            EquipmentSlot equipmentslot = Mob.getEquipmentSlotForItem(p_34788_);
            ItemStack itemstack = this.getItemBySlot(equipmentslot);
            return this.canReplaceCurrentItem(p_34788_, itemstack);
        }

        protected SoundEvent getAmbientSound() {
            return SoundEvents.VINDICATOR_AMBIENT;
        }

        protected SoundEvent getHurtSound(DamageSource p_35072_) {
            return SoundEvents.PILLAGER_HURT;
        }

        protected SoundEvent getDeathSound() {
            return SoundEvents.VINDICATOR_DEATH;
        }

        protected void playStepSound(BlockPos p_35066_, BlockState p_35067_) {
            this.playSound(SoundEvents.PIGLIN_BRUTE_STEP, 0.15F, 1.0F);
        }

        protected void playAngrySound() {
            this.playSound(SoundEvents.EVOKER_CELEBRATE, 1.0F, this.getVoicePitch());
        }


        private void setTypeVariant(int typeVariant) {
            this.entityData.set(DATA_ID_TYPE_VARIANT, typeVariant);
        }

        private int getTypeVariant() {
            return this.entityData.get(DATA_ID_TYPE_VARIANT);
        }

        @Override
        public void setVariant(Variant p_262689_) {
            this.setTypeVariant(p_262689_.getId() & 255 | this.getTypeVariant() & -256);
        }

        public @NotNull Variant getVariant() {
            return Variant.byId(this.getTypeVariant() & 255);
        }

        private ItemStack createSpawnWeapon() {
            return (double)this.random.nextFloat() < 0.5D ? new ItemStack(Items.CROSSBOW) : new ItemStack(SASItems.SHIV.get());
        }

        protected void dropCustomDeathLoot(DamageSource p_34697_, int p_34698_, boolean p_34699_) {
            super.dropCustomDeathLoot(p_34697_, p_34698_, p_34699_);
            // TODO // Implement drop of Survivor Heads if killed with cleaver or katana
            Entity entity = p_34697_.getEntity();
            if (entity instanceof Creeper creeper) {
                if (creeper.canDropMobsSkull()) {
                    ItemStack itemstack = new ItemStack(Items.PIGLIN_HEAD);
                    creeper.increaseDroppedSkulls();
                    this.spawnAtLocation(itemstack);
                }
            }

            this.inventory.removeAllItems().forEach(this::spawnAtLocation);
        }

        public ItemStack addToInventory(ItemStack p_34779_) {
            return this.inventory.addItem(p_34779_);
        }

        public boolean canAddToInventory(ItemStack p_34781_) {
                return this.inventory.canAddItem(p_34781_);
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

        public @NotNull SimpleContainer getInventory() {
            return this.inventory;
        }
}
