package de.nulldrei.saintsandsinners.entity.ai.memory.sensors;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import de.nulldrei.saintsandsinners.entity.ai.memory.SASMemoryModules;
import de.nulldrei.saintsandsinners.entity.hostile.AbstractFactionSurvivor;
import de.nulldrei.saintsandsinners.entity.hostile.ReclaimedFactionSurvivor;
import de.nulldrei.saintsandsinners.entity.hostile.TowerFactionSurvivor;
import de.nulldrei.saintsandsinners.entity.neutral.RobberSurvivor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
import net.minecraft.world.entity.ai.sensing.PiglinSpecificSensor;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.entity.player.Player;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public class FactionSurvivorSpecificSensor extends Sensor<LivingEntity> {
    public Set<MemoryModuleType<?>> requires() {
        return ImmutableSet.of(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, MemoryModuleType.NEAREST_LIVING_ENTITIES, MemoryModuleType.NEAREST_VISIBLE_PLAYER, SASMemoryModules.NEAREST_VISIBLE_ZOMBIE.get(), SASMemoryModules.VISIBLE_ZOMBIE_COUNT.get(), SASMemoryModules.VISIBLE_SURVIVOR_COUNT.get(), SASMemoryModules.NEARBY_SURVIVORS.get(), SASMemoryModules.NEAREST_RIVALING_SURVIVOR.get());
    }

    protected void doTick(ServerLevel serverLevel, LivingEntity livingEntity) {
        Brain<?> brain = livingEntity.getBrain();
        int zombieCount = 0;
        int factionSurvivorCount = 1;
        Optional<Player> nearestPlayer = Optional.empty();
        Optional<AbstractFactionSurvivor> nearestRivalingFactionSurvivor = Optional.empty();
        Optional<Zombie> nearestZombie = Optional.empty();
        List<AbstractFactionSurvivor> nearbyFactionSurvivors = Lists.newArrayList();
        NearestVisibleLivingEntities nearestvisiblelivingentities = brain.getMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES).orElse(NearestVisibleLivingEntities.empty());

        for (LivingEntity livingentity : nearestvisiblelivingentities.findAll((p_186157_) -> {
            return true;
        })) {
            if (livingentity instanceof Zombie zombie) {
                ++zombieCount;
                if (nearestZombie.isEmpty()) {
                    nearestZombie = Optional.of(zombie);
                }
            }
            if (livingentity instanceof Zombie zombie) {
                ++zombieCount;
                if (nearestZombie.isEmpty()) {
                    nearestZombie = Optional.of(zombie);
                }
            }
            if (livingentity instanceof AbstractFactionSurvivor abstractFactionSurvivor) {
                if(livingEntity instanceof TowerFactionSurvivor && livingentity instanceof TowerFactionSurvivor) {
                    ++factionSurvivorCount;
                    nearbyFactionSurvivors.add((AbstractFactionSurvivor) livingentity);
                } else if(livingEntity instanceof  TowerFactionSurvivor && livingentity instanceof ReclaimedFactionSurvivor rival) {
                    if(nearestRivalingFactionSurvivor.isEmpty()) {
                        nearestRivalingFactionSurvivor = Optional.of(rival);
                    }
                }
                if(livingEntity instanceof ReclaimedFactionSurvivor && livingentity instanceof ReclaimedFactionSurvivor) {
                    ++factionSurvivorCount;
                    nearbyFactionSurvivors.add((AbstractFactionSurvivor) livingentity);
                } else if(livingEntity instanceof  ReclaimedFactionSurvivor && livingentity instanceof TowerFactionSurvivor rival) {
                    if(nearestRivalingFactionSurvivor.isEmpty()) {
                        nearestRivalingFactionSurvivor = Optional.of(rival);
                    }
                }


            }

            if (livingentity instanceof Player player) {
                if (nearestPlayer.isEmpty() && !livingentity.isSpectator()) {
                    nearestPlayer = Optional.of(player);
                }
            }

            brain.setMemory(MemoryModuleType.NEAREST_VISIBLE_PLAYER, nearestPlayer);
            brain.setMemory(SASMemoryModules.NEAREST_VISIBLE_ZOMBIE.get(), nearestZombie);
            brain.setMemory(SASMemoryModules.VISIBLE_ZOMBIE_COUNT.get(), zombieCount);
            brain.setMemory(SASMemoryModules.NEARBY_SURVIVORS.get(), nearbyFactionSurvivors);
            brain.setMemory(SASMemoryModules.NEAREST_RIVALING_SURVIVOR.get(), nearestRivalingFactionSurvivor);
            brain.setMemory(SASMemoryModules.VISIBLE_SURVIVOR_COUNT.get(), factionSurvivorCount);
        }
    }
}
