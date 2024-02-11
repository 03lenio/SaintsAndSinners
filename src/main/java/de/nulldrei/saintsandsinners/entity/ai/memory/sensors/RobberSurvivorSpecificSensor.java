package de.nulldrei.saintsandsinners.entity.ai.memory.sensors;

import com.google.common.collect.ImmutableSet;
import de.nulldrei.saintsandsinners.entity.ai.memory.SASMemoryModules;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
import net.minecraft.world.entity.ai.sensing.PiglinBruteSpecificSensor;
import net.minecraft.world.entity.ai.sensing.PiglinSpecificSensor;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;

import java.util.Optional;
import java.util.Set;

public class RobberSurvivorSpecificSensor extends Sensor<LivingEntity> {

    public Set<MemoryModuleType<?>> requires() {
        return ImmutableSet.of(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, MemoryModuleType.NEAREST_LIVING_ENTITIES, MemoryModuleType.NEAREST_VISIBLE_PLAYER, SASMemoryModules.VISIBLE_ZOMBIE_COUNT.get());
    }

    protected void doTick(ServerLevel serverLevel, LivingEntity livingEntity) {
        Brain<?> brain = livingEntity.getBrain();
        int zombieCount = 0;
        Optional<Player> playerToDemand = Optional.empty();
        Optional<Zombie> nearestZombie = Optional.empty();
        NearestVisibleLivingEntities nearestvisiblelivingentities = brain.getMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES).orElse(NearestVisibleLivingEntities.empty());

        for (LivingEntity livingentity : nearestvisiblelivingentities.findAll((p_186157_) -> {
            return true;
        })) {
            if(livingentity instanceof Zombie zombie) {
                ++zombieCount;
                if (nearestZombie.isEmpty()) {
                    nearestZombie = Optional.of(zombie);
                }
            }
            if(livingentity instanceof Player player) {
                if (playerToDemand.isEmpty() && !livingentity.isSpectator()) {
                    playerToDemand = Optional.of(player);
                }
            }

            brain.setMemory(MemoryModuleType.NEAREST_VISIBLE_PLAYER, playerToDemand);
            brain.setMemory(SASMemoryModules.NEAREST_VISIBLE_ZOMBIE.get(), nearestZombie);
            brain.setMemory(SASMemoryModules.VISIBLE_ZOMBIE_COUNT.get(), zombieCount);
        }

    }
}
