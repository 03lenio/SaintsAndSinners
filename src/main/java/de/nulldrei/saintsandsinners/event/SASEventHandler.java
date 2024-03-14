package de.nulldrei.saintsandsinners.event;


import de.nulldrei.saintsandsinners.SASUtil;
import de.nulldrei.saintsandsinners.data.SASTags;
import de.nulldrei.saintsandsinners.entity.dead.Decapitated;
import de.nulldrei.saintsandsinners.entity.hostile.ReclaimedFactionSurvivor;
import de.nulldrei.saintsandsinners.entity.hostile.TowerFactionSurvivor;
import de.nulldrei.saintsandsinners.entity.neutral.AbstractSurvivor;
import de.nulldrei.saintsandsinners.entity.neutral.RobberSurvivor;
import de.nulldrei.saintsandsinners.entity.peaceful.BeggarSurvivor;
import de.nulldrei.saintsandsinners.item.SASItems;
import de.nulldrei.saintsandsinners.sound.SASSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.ZombieAttackGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.entity.monster.Husk;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
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

    @SubscribeEvent
    public void entityDeath(LivingDeathEvent event) {
        if(!event.getEntity().level().isClientSide()) {
            if (event.getSource().getEntity() instanceof Player player) {
                if (player.getMainHandItem().is(SASTags.DECAPITATING_WEAPONS) && player.getOffhandItem() == ItemStack.EMPTY) {
                    if (event.getEntity() instanceof AbstractSurvivor abstractSurvivor) {
                        String variant = "";
                        if (abstractSurvivor instanceof BeggarSurvivor beggarSurvivor) {
                            variant = beggarSurvivor.getVariant().getSerializedName();
                        } else if (abstractSurvivor instanceof RobberSurvivor robberSurvivor) {
                            variant = robberSurvivor.getVariant().getSerializedName();
                        } else if (abstractSurvivor instanceof ReclaimedFactionSurvivor reclaimedFactionSurvivor) {
                            variant = reclaimedFactionSurvivor.getVariant().getSerializedName();
                        } else if (abstractSurvivor instanceof TowerFactionSurvivor towerFactionSurvivor) {
                            variant = towerFactionSurvivor.getVariant().getSerializedName();
                        }
                        Decapitated decapitated = new Decapitated(player.level());
                        decapitated.setPos(event.getEntity().position());
                        decapitated.setVariant("survivor:" + variant);
                        decapitated.finalizeSpawn((ServerLevelAccessor) player.level(), player.level().getCurrentDifficultyAt(new BlockPos(event.getEntity().getBlockX(), event.getEntity().getBlockY(), event.getEntity().getBlockZ())), MobSpawnType.MOB_SUMMONED, null, null);
                        abstractSurvivor.discard();
                        player.level().addFreshEntity(decapitated);
                        player.level().playSound(null, player.blockPosition(), SASSounds.HEAD_DECAPITATED.get(), SoundSource.PLAYERS);
                    } else if (event.getEntity() instanceof Zombie zombie) {
                        if(!zombie.isBaby() && !(zombie instanceof Drowned) && !(zombie instanceof Husk)) {
                            Decapitated decapitated = new Decapitated(player.level());
                            decapitated.setPos(event.getEntity().position());
                            decapitated.setVariant("zombie");
                            decapitated.finalizeSpawn((ServerLevelAccessor) player.level(), player.level().getCurrentDifficultyAt(new BlockPos(event.getEntity().getBlockX(), event.getEntity().getBlockY(), event.getEntity().getBlockZ())), MobSpawnType.MOB_SUMMONED, null, null);
                            zombie.discard();
                            player.level().addFreshEntity(decapitated);
                            player.level().playSound(null, player.blockPosition(), SASSounds.HEAD_DECAPITATED.get(), SoundSource.PLAYERS);
                        }
                    }
                }
            }
        }
    }

}
