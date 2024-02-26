package de.nulldrei.saintsandsinners.entity.projectile;

import de.nulldrei.saintsandsinners.entity.SASEntities;
import de.nulldrei.saintsandsinners.entity.ai.behavior.goal.ZombieWalkToPositionGoal;
import de.nulldrei.saintsandsinners.entity.neutral.AbstractSurvivor;
import de.nulldrei.saintsandsinners.item.SASItems;
import net.minecraft.client.model.ZombieModel;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.ZombieRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundTakeItemEntityPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.datafix.fixes.AbstractArrowPickupFix;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.*;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerXpEvent;

public class StickyProximityBomb extends ThrowableItemProjectile {

    public StickyProximityBomb(EntityType<? extends StickyProximityBomb> p_37391_, Level p_37392_) {
        super(p_37391_, p_37392_);
    }

    public StickyProximityBomb(Level p_37399_, LivingEntity p_37400_) {
        super(SASEntities.STICKY_PROXIMITY_BOMB.get(), p_37400_, p_37399_);
    }

    public StickyProximityBomb(Level p_37394_, double p_37395_, double p_37396_, double p_37397_) {
        super(SASEntities.STICKY_PROXIMITY_BOMB.get(), p_37395_, p_37396_, p_37397_, p_37394_);
    }

    protected Item getDefaultItem() {
        return SASItems.STICKY_PROXIMITY_BOMB.get();
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide && onGround()) {
            AABB distractionRange = this.getBoundingBox().inflate(2D);
            for (Entity e : level().getEntities(this, distractionRange)) {
                if (e instanceof Zombie || e instanceof AbstractSurvivor) {
                    level().playSound(null, this.getBlockPosBelowThatAffectsMyMovement(), SoundEvents.SCULK_CLICKING, SoundSource.AMBIENT);
                    level().explode(this, getBlockX(), getBlockY(), getBlockZ(), 4.0F, Level.ExplosionInteraction.MOB);
                    discard();
                }
            }
        }
        if(!isPassenger()) {
            AABB aabb = getBoundingBox().inflate(0.06D);
            setNoGravity(!AABBOnlyAir(aabb));
        }
    }

    private boolean AABBOnlyAir(AABB aabb) {
        for (int x = (int) aabb.minX; x <= aabb.maxX; ++x) {
            for (int y = (int) aabb.minY; y <= aabb.maxY; ++y) {
                for (int z = (int) aabb.minZ; z <= aabb.maxZ; ++z) {
                    // Set the current block position
                    if(!level().getBlockState(new BlockPos(x, y, z)).isAir()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    @Override
    protected void updateRotation() {
        setRot(0,0);
    }

    protected void onHitBlock(BlockHitResult p_36755_) {

        super.onHitBlock(p_36755_);
        Vec3 vec3 = p_36755_.getLocation().subtract(this.getX(), this.getY(), this.getZ());
        this.setDeltaMovement(vec3);
        Vec3 vec31 = vec3.normalize().scale((double)0.05F);
        this.setPosRaw(this.getX() - vec31.x, this.getY() - vec31.y, this.getZ() - vec31.z);
        setOnGround(true);
        setRot(0,0);
        setOldPosAndRot();
        updateRotation();
    }

    public void playerTouch(Player p_36766_) {
        if (!this.level().isClientSide() && (this.onGround() || this.isNoGravity())) {
            if (this.tryPickup(p_36766_)) {
                if (!this.isRemoved() && !this.level().isClientSide()) {
                    ((ServerLevel)this.level()).getChunkSource().broadcast(this, new ClientboundTakeItemEntityPacket(this.getId(), p_36766_.getId(), 1));
                }
                this.discard();
            }

        }
    }

    @Override
    protected void onHitEntity(EntityHitResult hitResult) {
        super.onHitEntity(hitResult);
        Entity entity = hitResult.getEntity();
        if (!level().isClientSide && canHitEntity(entity)) {
            if (entity instanceof LivingEntity hitentity && getOwner() instanceof LivingEntity owner) {
                hitentity.setLastHurtByMob(owner);
            }
            this.startRiding(entity, true);
            setOnGround(true);
        }
    }


    @Override
    public double getMyRidingOffset() {
        return 0.5D;
    }

    @Override
    protected boolean updateInWaterStateAndDoFluidPushing() {
        return false;
    }


    protected boolean tryPickup(Player p_150121_) {
        return p_150121_.getInventory().add(this.getPickupItem());
    }

    private ItemStack getPickupItem() {
        return new ItemStack(SASItems.STICKY_PROXIMITY_BOMB.get());
    }

}
