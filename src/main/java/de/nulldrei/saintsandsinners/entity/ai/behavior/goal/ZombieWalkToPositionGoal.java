package de.nulldrei.saintsandsinners.entity.ai.behavior.goal;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.entity.animal.Turtle;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.TurtleEggBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class ZombieWalkToPositionGoal extends Goal {
    final Zombie zombie;
    final double stopDistance;
    final double speedModifier;
    final BlockPos blockPos;

    public ZombieWalkToPositionGoal(Zombie p_35899_, double p_35900_, double p_35901_, BlockPos blockPos) {
        this.zombie = p_35899_;
        this.stopDistance = p_35900_;
        this.speedModifier = p_35901_;
        this.blockPos = blockPos;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    public void stop() {
        zombie.getNavigation().stop();
    }

    public boolean canUse() {
        return blockPos != null;
    }

    public void tick() {
        if (blockPos != null && zombie.getNavigation().isDone()) {
                zombie.getNavigation().moveTo((double) blockPos.getX(), (double) blockPos.getY(), (double) blockPos.getZ(), this.speedModifier);
        }
       //System.out.println("ohio");

    }
}