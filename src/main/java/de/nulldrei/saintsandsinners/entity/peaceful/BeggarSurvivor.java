package de.nulldrei.saintsandsinners.entity.peaceful;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Dynamic;
import de.nulldrei.saintsandsinners.SASUtil;
import de.nulldrei.saintsandsinners.SaintsAndSinners;
import de.nulldrei.saintsandsinners.entity.ai.memory.SASMemoryModules;
import de.nulldrei.saintsandsinners.entity.ai.memory.sensors.SASSensorTypes;
import de.nulldrei.saintsandsinners.entity.ai.survivor.BeggarSurvivorAI;
import de.nulldrei.saintsandsinners.entity.animations.pose.SurvivorArmPose;
import de.nulldrei.saintsandsinners.entity.neutral.AbstractSurvivor;
import de.nulldrei.saintsandsinners.entity.peaceful.variant.Variant;
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
import net.minecraft.util.VisibleForDebug;
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
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.InventoryCarrier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class BeggarSurvivor extends AbstractSurvivor implements InventoryCarrier, VariantHolder<Variant> {


    private final SimpleContainer inventory = new SimpleContainer(8);
    private static final EntityDataAccessor<ItemStack> DATA_ITEM = SynchedEntityData.defineId(BeggarSurvivor.class, EntityDataSerializers.ITEM_STACK);
    public static final EntityDataAccessor<Boolean> DATA_ACTIVELY_BEGGING = SynchedEntityData.defineId(BeggarSurvivor.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Integer> DATA_ID_TYPE_VARIANT = SynchedEntityData.defineId(BeggarSurvivor.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Boolean> DATA_BEGGING = SynchedEntityData.defineId(BeggarSurvivor.class, EntityDataSerializers.BOOLEAN);
    protected static final ImmutableList<SensorType<? extends Sensor<? super BeggarSurvivor>>> SENSOR_TYPES = ImmutableList.of(SensorType.NEAREST_LIVING_ENTITIES, SensorType.NEAREST_PLAYERS, SensorType.NEAREST_ITEMS, SensorType.HURT_BY, SASSensorTypes.BEGGAR_SURVIVOR_SPECIFIC_SENSOR.get());
    protected static final ImmutableList<MemoryModuleType<?>> MEMORY_TYPES = ImmutableList.of(MemoryModuleType.LOOK_TARGET, MemoryModuleType.DOORS_TO_CLOSE, MemoryModuleType.NEAREST_LIVING_ENTITIES, MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, MemoryModuleType.NEAREST_VISIBLE_PLAYER,  MemoryModuleType.NEAREST_PLAYER_HOLDING_WANTED_ITEM,MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM, MemoryModuleType.ITEM_PICKUP_COOLDOWN_TICKS, MemoryModuleType.HURT_BY, MemoryModuleType.HURT_BY_ENTITY, MemoryModuleType.WALK_TARGET, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryModuleType.INTERACTION_TARGET, MemoryModuleType.PATH, MemoryModuleType.AVOID_TARGET, MemoryModuleType.ADMIRING_ITEM, MemoryModuleType.TIME_TRYING_TO_REACH_ADMIRE_ITEM, MemoryModuleType.ADMIRING_DISABLED, MemoryModuleType.DISABLE_WALK_TO_ADMIRE_ITEM, SASMemoryModules.NEAREST_VISIBLE_ZOMBIE.get(), MemoryModuleType.ATE_RECENTLY, MemoryModuleType.HOME);

    public BeggarSurvivor(EntityType<? extends AbstractSurvivor> p_34652_, Level p_34653_) {
        super(p_34652_, p_34653_);
    }

    public ItemStack getNeededItem() {
        return this.getEntityData().get(DATA_ITEM);
    }

    public boolean doesSurvivorNeedItem(ItemStack itemStack) {
        return itemStack.getItem() == getNeededItem().getItem();
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

    public boolean isBegging() {
        return this.getEntityData().get(DATA_BEGGING);
    }

    public void setBegging(boolean begging) {
        entityData.set(DATA_BEGGING, begging);
    }

    public void addAdditionalSaveData(CompoundTag p_34751_) {
        super.addAdditionalSaveData(p_34751_);
        if (!this.getNeededItem().isEmpty()) {
            p_34751_.put(SaintsAndSinners.MODID + ":neededItem", this.getNeededItem().save(new CompoundTag()));
        }
        p_34751_.putInt("Variant", this.getTypeVariant());
        p_34751_.putBoolean(SaintsAndSinners.MODID + ":isBegging", this.isBegging());
        this.writeInventoryToTag(p_34751_);
    }

    public void readAdditionalSaveData(CompoundTag p_34725_) {
        super.readAdditionalSaveData(p_34725_);
        if(p_34725_.contains(SaintsAndSinners.MODID + ":isBegging")) {
            this.setBegging(p_34725_.getBoolean(SaintsAndSinners.MODID + ":isBegging"));
        }
        this.setTypeVariant(p_34725_.getInt("Variant"));
        CompoundTag compoundtag = p_34725_.getCompound(SaintsAndSinners.MODID + ":neededItem");
        if (!compoundtag.isEmpty()) {
            ItemStack neededItemStack = ItemStack.of(compoundtag);
            if (neededItemStack.isEmpty()) {
                SaintsAndSinners.LOGGER.error("Unable to load item from: " + compoundtag);
            } else {
                setNeededItem(neededItemStack);
            }
        }
        this.readInventoryFromTag(p_34725_);
    }

    public void setNeededItem(ItemStack neededItem) {
        getEntityData().set(DATA_ITEM, neededItem);
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor p_21434_, DifficultyInstance p_21435_, MobSpawnType p_21436_, @Nullable SpawnGroupData p_21437_, @Nullable CompoundTag p_21438_) {
        ItemStack neededItem = generateNeededItem();
        BeggarSurvivorAI.initMemories(this);
        this.getEntityData().set(DATA_ITEM, neededItem);
        SASUtil.neededItems.add(neededItem.toString());
        setVariant(Util.getRandom(Variant.values(), p_21434_.getRandom()));
        return super.finalizeSpawn(p_21434_, p_21435_, p_21436_, p_21437_, p_21438_);
    }

    @VisibleForDebug
    public SimpleContainer getInventory() {
        return this.inventory;
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

    protected void defineSynchedData() {
        this.getEntityData().define(DATA_ITEM, ItemStack.EMPTY);
        this.getEntityData().define(DATA_BEGGING, Boolean.TRUE);
        this.getEntityData().define(DATA_ACTIVELY_BEGGING, Boolean.FALSE);
        this.getEntityData().define(DATA_ID_TYPE_VARIANT, 0);
        super.defineSynchedData();
    }


    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 16.0D).add(Attributes.MOVEMENT_SPEED, (double)0.35F).add(Attributes.ATTACK_DAMAGE, 5.0D);
    }


    protected boolean shouldDespawnInPeaceful() {
        return false;
    }

    public boolean removeWhenFarAway(double p_34775_) {
        return !this.isPersistenceRequired();
    }

    protected void populateDefaultEquipmentSlots(RandomSource p_219189_, DifficultyInstance p_219190_) {
             this.maybeWearArmor(EquipmentSlot.HEAD, new ItemStack(Items.LEATHER_HELMET), p_219189_);
            this.maybeWearArmor(EquipmentSlot.CHEST, new ItemStack(Items.LEATHER_CHESTPLATE), p_219189_);
            this.maybeWearArmor(EquipmentSlot.LEGS, new ItemStack(Items.LEATHER_LEGGINGS), p_219189_);
            this.maybeWearArmor(EquipmentSlot.FEET, new ItemStack(Items.LEATHER_BOOTS), p_219189_);


    }

    private void maybeWearArmor(EquipmentSlot p_219192_, ItemStack p_219193_, RandomSource p_219194_) {
        if (p_219194_.nextFloat() < 0.1F) {
            this.setItemSlot(p_219192_, p_219193_);
        }

    }

    protected Brain.Provider<BeggarSurvivor> brainProvider() {
        return Brain.provider(MEMORY_TYPES, SENSOR_TYPES);
    }

    protected Brain<?> makeBrain(Dynamic<?> p_34723_) {
        return BeggarSurvivorAI.makeBrain(this, this.brainProvider().makeBrain(p_34723_));
    }

    public Brain<BeggarSurvivor> getBrain() {
        return (Brain<BeggarSurvivor>)super.getBrain();
    }

    public InteractionResult mobInteract(Player p_34745_, InteractionHand p_34746_) {
        if(isBegging()) {
            InteractionResult interactionresult = super.mobInteract(p_34745_, p_34746_);
            if (interactionresult.consumesAction()) {
                return interactionresult;
            } else if (!this.level().isClientSide) {
                return BeggarSurvivorAI.mobInteract(this, p_34745_, p_34746_);
            } else {
                boolean flag = BeggarSurvivorAI.canAdmire(this, p_34745_.getItemInHand(p_34746_)) && this.getArmPose() != SurvivorArmPose.ADMIRING_ITEM;
                return flag ? InteractionResult.SUCCESS : InteractionResult.PASS;
            }
        } return InteractionResult.PASS;
    }

    protected float getStandingEyeHeight(Pose p_34740_, EntityDimensions p_34741_) {
        float f = super.getStandingEyeHeight(p_34740_, p_34741_);
        return this.isBaby() ? f - 0.82F : f;
    }

    @Override
    public boolean canHunt() {
        return false;
    }

    public double getPassengersRidingOffset() {
        return (double)this.getBbHeight() * 0.92D;
    }


    protected void customServerAiStep() {
        this.level().getProfiler().push("beggarSurvivorBrain");
        this.getBrain().tick((ServerLevel)this.level(), this);
        this.level().getProfiler().pop();
        BeggarSurvivorAI.updateActivity(this);
        BeggarSurvivorAI.maybePlayActivitySound(this);
        super.customServerAiStep();
    }

    public int getExperienceReward() {
        return this.xpReward;
    }

    public SurvivorArmPose getArmPose() {
        if (this.getOffhandItem().getItem() != Items.AIR) {
            if(getOffhandItem().getItem() == getNeededItem().getItem()) {
                setBegging(false);
                SASUtil.neededItems.remove(getNeededItem().toString());
                return SurvivorArmPose.ADMIRING_ITEM;
            }
        } else if (getEntityData().get(DATA_ACTIVELY_BEGGING) && isBegging()) {
            return SurvivorArmPose.BEGGING_FOR_ITEM;
        } else {
           BeggarSurvivorAI.seesPlayer(this);
        }
        return SurvivorArmPose.DEFAULT;
    }

    public boolean hurt(DamageSource p_34694_, float p_34695_) {
        boolean flag = super.hurt(p_34694_, p_34695_);
        if (this.level().isClientSide) {
            return false;
        } else {
            if (flag && p_34694_.getEntity() instanceof LivingEntity) {
                BeggarSurvivorAI.wasHurtBy(this, (LivingEntity)p_34694_.getEntity());
            }
            return flag;
        }
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
        if(isBegging()) {
            return net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.level(), this) && this.canPickUpLoot() && BeggarSurvivorAI.wantsToPickup(this, p_34777_);
        }
            return false;
        }

    public boolean canReplaceCurrentItem(ItemStack p_34788_) {
        EquipmentSlot equipmentslot = Mob.getEquipmentSlotForItem(p_34788_);
        ItemStack itemstack = this.getItemBySlot(equipmentslot);
        return this.canReplaceCurrentItem(p_34788_, itemstack);
    }


    protected void pickUpItem(ItemEntity p_34743_) {
        this.onItemPickup(p_34743_);
        BeggarSurvivorAI.pickUpItem(this, p_34743_);
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.VILLAGER_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource p_34767_) {
        return SoundEvents.VILLAGER_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.VILLAGER_DEATH;
    }

    protected void playStepSound(BlockPos p_34748_, BlockState p_34749_) {
        this.playSound(SoundEvents.ZOMBIE_STEP, 0.15F, 1.0F);
    }

    @Override
    public boolean isDeadOrDying() {
        //SASUtil.neededItems.remove(getNeededItem().toString());
        return super.isDeadOrDying();
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
}
