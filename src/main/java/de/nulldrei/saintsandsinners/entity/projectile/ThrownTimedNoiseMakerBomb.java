package de.nulldrei.saintsandsinners.entity.projectile;

import de.nulldrei.saintsandsinners.entity.SASEntities;
import de.nulldrei.saintsandsinners.entity.ai.behavior.goal.ZombieWalkToPositionGoal;
import de.nulldrei.saintsandsinners.item.SASItems;
import net.minecraft.client.renderer.entity.TntRenderer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundTakeItemEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;

import java.util.ArrayList;
import java.util.List;

public class ThrownTimedNoiseMakerBomb extends ThrowableItemProjectile {

    public ThrownTimedNoiseMakerBomb(EntityType<? extends ThrownTimedNoiseMakerBomb> p_37391_, Level p_37392_) {
        super(p_37391_, p_37392_);
    }

    public ThrownTimedNoiseMakerBomb(Level p_37399_, LivingEntity p_37400_) {
        super(SASEntities.THROWN_TIMED_NOISE_MAKER_BOMB.get(), p_37400_, p_37399_);
    }

    public ThrownTimedNoiseMakerBomb(Level p_37394_, double p_37395_, double p_37396_, double p_37397_) {
        super(SASEntities.THROWN_TIMED_NOISE_MAKER_BOMB.get(), p_37395_, p_37396_, p_37397_, p_37394_);
    }

    @Override
    protected void onHit(HitResult p_37260_) {
        super.onHit(p_37260_);
        TimedNoiseMakerBomb timedNoiseMakerBomb = new TimedNoiseMakerBomb(level(), p_37260_.getLocation().x, p_37260_.getLocation().y + 1, p_37260_.getLocation().z);
        level().addFreshEntity(timedNoiseMakerBomb);
        discard();
    }

    @Override
    protected Item getDefaultItem() {
        return SASItems.TIMED_NOISE_MAKER_BOMB.get();
    }
}
