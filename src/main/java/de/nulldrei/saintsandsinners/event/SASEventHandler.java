package de.nulldrei.saintsandsinners.event;


import de.nulldrei.saintsandsinners.SASUtil;
import de.nulldrei.saintsandsinners.entity.neutral.AbstractSurvivor;
import de.nulldrei.saintsandsinners.entity.neutral.RobberSurvivor;
import de.nulldrei.saintsandsinners.entity.peaceful.BeggarSurvivor;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.ZombieAttackGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.event.entity.living.MobSpawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(modid = "saintsandsinners", bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SASEventHandler {



    @SubscribeEvent
    public void tickPlayer(TickEvent.PlayerTickEvent event) {
        if (event.player.level().isClientSide || event.phase == TickEvent.Phase.END)
                return;
        if (event.player.level().getGameTime() % 20 == 0) {
            SASUtil.tickPlayer(event.player);
        }
        List<Entity> entitiesInRange =  event.player.level().getEntities(event.player, event.player.getBoundingBox().inflate(20d));
        for(Entity e : entitiesInRange) {
            if(e instanceof Zombie zombie && SASUtil.doesPlayerWearRottenFleshArmor(event.player)) {
                if(zombie.getTarget() instanceof Player) {
                    zombie.setTarget(null);
                }
            }
        }
    }



    @SubscribeEvent
    public void spawnMob(MobSpawnEvent event) {
        if(event.getEntity() instanceof Zombie) {
            Zombie zombie = (Zombie)event.getEntity();
            zombie.goalSelector.addGoal(3, new NearestAttackableTargetGoal<>(zombie, AbstractSurvivor.class, true));
        }
    }


}
