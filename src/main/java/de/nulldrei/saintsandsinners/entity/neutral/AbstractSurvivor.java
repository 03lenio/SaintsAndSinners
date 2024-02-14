package de.nulldrei.saintsandsinners.entity.neutral;

import de.nulldrei.saintsandsinners.entity.ai.survivor.BeggarSurvivorAI;
import de.nulldrei.saintsandsinners.entity.animations.pose.SurvivorArmPose;
import de.nulldrei.saintsandsinners.entity.peaceful.BeggarSurvivor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.DebugPackets;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.util.GoalUtils;
import net.minecraft.world.entity.animal.sniffer.Sniffer;
import net.minecraft.world.entity.animal.sniffer.SnifferAi;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.BlockPathTypes;

import javax.annotation.Nullable;

public abstract class AbstractSurvivor extends Monster {

    protected static final float SURVIVOR_EYE_HEIGHT = 1.79F;
    protected int timeInOverworld;

    public AbstractSurvivor(EntityType<? extends AbstractSurvivor> p_34652_, Level p_34653_) {
        super(p_34652_, p_34653_);
        this.setCanPickUpLoot(true);
        this.getNavigation().setCanFloat(true);
        this.applyOpenDoorsAbility();
        this.setPathfindingMalus(BlockPathTypes.DANGER_FIRE, 16.0F);
        this.setPathfindingMalus(BlockPathTypes.DAMAGE_FIRE, -1.0F);
    }

    private void applyOpenDoorsAbility() {
        if (GoalUtils.hasGroundPathNavigation(this)) {
            ((GroundPathNavigation)this.getNavigation()).setCanOpenDoors(true);
        }

    }

    protected float getStandingEyeHeight(Pose p_259213_, EntityDimensions p_259279_) {
        return 1.79F;
    }

    public abstract boolean canHunt();


    public double getMyRidingOffset() {
        return this.isBaby() ? -0.05D : -0.45D;
    }

    public void readAdditionalSaveData(CompoundTag p_34659_) {
        super.readAdditionalSaveData(p_34659_);
        this.timeInOverworld = p_34659_.getInt("TimeInOverworld");
    }

    protected void customServerAiStep() {
        super.customServerAiStep();
        this.timeInOverworld = 0;
    }


    public abstract SurvivorArmPose getArmPose();

    @Nullable
    public LivingEntity getTarget() {
        if (this instanceof BeggarSurvivor) {
            return null;
        }
        return this.brain.getMemory(MemoryModuleType.ATTACK_TARGET).orElse((LivingEntity)null);
    }

    protected boolean isHoldingMeleeWeapon() {
        return this.getMainHandItem().getItem() instanceof TieredItem;
    }

    public void playAmbientSound() {
        if (BeggarSurvivorAI.isIdle(this)) {
            super.playAmbientSound();
        }
    }

    protected void sendDebugPackets() {
        super.sendDebugPackets();
        DebugPackets.sendEntityBrain(this);
    }



}
