package de.nulldrei.saintsandsinners.event;

import de.nulldrei.saintsandsinners.SaintsAndSinners;
import de.nulldrei.saintsandsinners.entity.SASEntities;
import de.nulldrei.saintsandsinners.entity.hostile.ReclaimedFactionSurvivor;
import de.nulldrei.saintsandsinners.entity.hostile.TowerFactionSurvivor;
import de.nulldrei.saintsandsinners.entity.neutral.Decapitated;
import de.nulldrei.saintsandsinners.entity.peaceful.BeggarSurvivor;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SaintsAndSinners.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SASCustomEventBusEvents {

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(SASEntities.BEGGAR_SURVIVOR.get(), BeggarSurvivor.createAttributes().build());
        event.put(SASEntities.ROBBER_SURVIVOR.get(), BeggarSurvivor.createAttributes().build());
        event.put(SASEntities.TOWER_FACTION_SURVIVOR.get(), TowerFactionSurvivor.createAttributes().build());
        event.put(SASEntities.RECLAIMED_FACTION_SURVIVOR.get(), ReclaimedFactionSurvivor.createAttributes().build());
        event.put(SASEntities.DECAPITATED.get(), Decapitated.createAttributes().build());
    }

}
