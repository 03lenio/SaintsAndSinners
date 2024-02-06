package de.nulldrei.saintsandsinners.event;


import de.nulldrei.saintsandsinners.SASUtil;
import de.nulldrei.saintsandsinners.entity.peaceful.BeggarSurvivor;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.MobSpawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "saintsandsinners", bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SASEventHandler {



    @SubscribeEvent
    public void tickPlayer(TickEvent.PlayerTickEvent event) {
        if (event.player.level().isClientSide || event.phase == TickEvent.Phase.END)
                return;
        if (event.player.level().getGameTime() % 20 == 0) {
            SASUtil.tickPlayer(event.player);
        }
    }

    @SubscribeEvent
    public void spawnMob(MobSpawnEvent event) {
        if(event.getEntity() instanceof Zombie) {
            Zombie zombie = (Zombie)event.getEntity();
            zombie.goalSelector.addGoal(3, new NearestAttackableTargetGoal<>(zombie, BeggarSurvivor.class, true));
        }
    }


}
