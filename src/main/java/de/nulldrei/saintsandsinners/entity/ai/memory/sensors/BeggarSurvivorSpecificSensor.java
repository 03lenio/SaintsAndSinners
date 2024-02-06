package de.nulldrei.saintsandsinners.entity.ai.memory.sensors;

import com.google.common.collect.ImmutableSet;
import de.nulldrei.saintsandsinners.entity.ai.memory.SASMemoryModules;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;

import java.util.Optional;
import java.util.Set;

public class BeggarSurvivorSpecificSensor extends Sensor<LivingEntity> {
    public Set<MemoryModuleType<?>> requires() {
        return ImmutableSet.of(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, MemoryModuleType.NEAREST_LIVING_ENTITIES, MemoryModuleType.NEAREST_VISIBLE_PLAYER, MemoryModuleType.NEAREST_PLAYER_HOLDING_WANTED_ITEM,SASMemoryModules.NEAREST_VISIBLE_ZOMBIE.get());
    }

    protected void doTick(ServerLevel serverLevel, LivingEntity livingEntity) {
        Brain<?> brain = livingEntity.getBrain();
        Optional<Zombie> zombie = Optional.empty();
        Optional<Player> playerToBeg = Optional.empty();
        NearestVisibleLivingEntities nearestvisiblelivingentities = brain.getMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES).orElse(NearestVisibleLivingEntities.empty());

        for (LivingEntity livingentity : nearestvisiblelivingentities.findAll((p_186157_) -> {
            return true;
        })) {
            if(zombie.isEmpty() && livingentity instanceof Zombie) {
                zombie = Optional.of((Zombie) livingentity);
            }
            if(livingentity instanceof Player player) {
                if (playerToBeg.isEmpty() && !livingentity.isSpectator()) {
                    playerToBeg = Optional.of(player);
                }
            }

            brain.setMemory(SASMemoryModules.NEAREST_VISIBLE_ZOMBIE.get(), zombie);
            brain.setMemory(MemoryModuleType.NEAREST_VISIBLE_PLAYER, playerToBeg);
            brain.setMemory(MemoryModuleType.NEAREST_PLAYER_HOLDING_WANTED_ITEM, playerToBeg);
        }

    }
}
