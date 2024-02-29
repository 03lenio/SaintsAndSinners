package de.nulldrei.saintsandsinners.entity.projectile;

import de.nulldrei.saintsandsinners.entity.SASEntities;
import de.nulldrei.saintsandsinners.entity.ai.behavior.goal.ZombieWalkToPositionGoal;
import de.nulldrei.saintsandsinners.item.SASItems;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundTakeItemEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class TimedNoiseMakerBomb extends Entity {

    private static final EntityDataAccessor<Integer> DATA_TICKS_UNTIL_EXPLODE = SynchedEntityData.defineId(TimedNoiseMakerBomb.class, EntityDataSerializers.INT);

    private ArrayList<Zombie> affectedZombies = new ArrayList<Zombie>();

    public TimedNoiseMakerBomb(EntityType<? extends TimedNoiseMakerBomb> p_37391_, Level p_37392_) {
        super(p_37391_, p_37392_);
    }

    public TimedNoiseMakerBomb(Level p_32079_, double p_32080_, double p_32081_, double p_32082_) {
        this(SASEntities.TIMED_NOISE_MAKER_BOMB.get(), p_32079_);
        this.setPos(p_32080_, p_32081_, p_32082_);
        double d0 = p_32079_.random.nextDouble() * (double)((float)Math.PI * 2F);
        this.setDeltaMovement(-Math.sin(d0) * 0.02D, (double)0.2F, -Math.cos(d0) * 0.02D);
        this.xo = p_32080_;
        this.yo = p_32081_;
        this.zo = p_32082_;
    }

    protected Item getDefaultItem() {
        return SASItems.TIMED_NOISE_MAKER_BOMB.get();
    }

    protected Entity.MovementEmission getMovementEmission() {
        return Entity.MovementEmission.NONE;
    }

    @Override
    public void tick() {
        if (!this.isNoGravity()) {
            this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -0.04D, 0.0D));
        }

        this.move(MoverType.SELF, this.getDeltaMovement());
        this.setDeltaMovement(this.getDeltaMovement().scale(0.98D));
        if (this.onGround()) {
            this.setDeltaMovement(this.getDeltaMovement().multiply(0.7D, -0.5D, 0.7D));
        }
        if (!this.level().isClientSide) {
            if (getTicksUntilExplode() <= 100) {
                if((getTicksUntilExplode() % 6) == 0) {
                    this.level().playSound(null, this.getBlockPosBelowThatAffectsMyMovement(), SoundEvents.BELL_RESONATE, SoundSource.AMBIENT);
                }
                AABB distractionRange = this.getBoundingBox().inflate(6D);
                for (Entity e : level().getEntities(this, distractionRange)) {
                    if (e instanceof Zombie zombie) {
                        if (random.nextInt(5) == 0) {
                            zombie.goalSelector.addGoal(1, new ZombieWalkToPositionGoal(zombie, 2.0D, 0.75D, getBlockPosBelowThatAffectsMyMovement()));
                            affectedZombies.add(zombie);
                        }
                    }
                }
                System.out.println("holy ohio");
                setTicksUntilExplode(getTicksUntilExplode()+1);
            } else {
                System.out.println("ohio");
                try {
                    if(!affectedZombies.isEmpty()) {
                        removeWalkToPositionGoal(affectedZombies);
                    }
                    level().explode(this, getBlockX(), getBlockY(), getBlockZ(), 4.0F, Level.ExplosionInteraction.MOB);
                    discard();
                } catch (NullPointerException | IndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void defineSynchedData() {
        getEntityData().define(DATA_TICKS_UNTIL_EXPLODE, 0);
    }

    public void setTicksUntilExplode(int ticksUntilStop) {
        getEntityData().set(DATA_TICKS_UNTIL_EXPLODE, ticksUntilStop);
    }

    public int getTicksUntilExplode() {
        return getEntityData().get(DATA_TICKS_UNTIL_EXPLODE);
    }


    private void removeWalkToPositionGoal(ArrayList<Zombie> affectedZombies) {
        ArrayList<Zombie> markedForRemoval = new ArrayList<Zombie>();
        if (!affectedZombies.isEmpty()) {
            for (Zombie zombie : affectedZombies) {
                if (zombie.isAddedToWorld()) {
                    List<WrappedGoal> goalList = zombie.goalSelector.getAvailableGoals().stream().toList();
                    for (int j = 0; j < goalList.size(); j++) {
                        Goal goal = goalList.get(j).getGoal();
                        if (goal instanceof ZombieWalkToPositionGoal) {
                            markedForRemoval.add(zombie);
                            zombie.goalSelector.removeGoal(goal);
                        }
                    }
                }
            }
        }
        for(Zombie zombie : markedForRemoval) {
            affectedZombies.remove(zombie);
        }
    }

    public void readAdditionalSaveData(CompoundTag p_37424_) {
        if (p_37424_.contains("TicksUntilExplode")) {
            setTicksUntilExplode(p_37424_.getInt("TicksUntilExplode"));
        }
    }

    public void addAdditionalSaveData(CompoundTag p_37426_) {
        p_37426_.putInt("TicksUntilExplode", this.getTicksUntilExplode());
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
    protected boolean updateInWaterStateAndDoFluidPushing() {
        return false;
    }


    protected boolean tryPickup(Player p_150121_) {
        try {
            if (!p_150121_.level().isClientSide() && !affectedZombies.isEmpty()) {
                removeWalkToPositionGoal(affectedZombies);
            }
        } catch (NullPointerException | IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        return p_150121_.getInventory().add(this.getPickupItem());
    }

    private ItemStack getPickupItem() {
        return new ItemStack(SASItems.TIMED_NOISE_MAKER_BOMB.get());
    }


}
