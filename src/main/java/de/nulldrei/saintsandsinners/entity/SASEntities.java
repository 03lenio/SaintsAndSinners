package de.nulldrei.saintsandsinners.entity;

import de.nulldrei.saintsandsinners.SaintsAndSinners;
import de.nulldrei.saintsandsinners.entity.hostile.ReclaimedFactionSurvivor;
import de.nulldrei.saintsandsinners.entity.hostile.TowerFactionSurvivor;
import de.nulldrei.saintsandsinners.entity.dead.Decapitated;
import de.nulldrei.saintsandsinners.entity.neutral.RobberSurvivor;
import de.nulldrei.saintsandsinners.entity.peaceful.BeggarSurvivor;
import de.nulldrei.saintsandsinners.entity.projectile.NailBomb;
import de.nulldrei.saintsandsinners.entity.projectile.StickyProximityBomb;
import de.nulldrei.saintsandsinners.entity.projectile.ThrownTimedNoiseMakerBomb;
import de.nulldrei.saintsandsinners.entity.projectile.TimedNoiseMakerBomb;
import de.nulldrei.saintsandsinners.entity.projectile.arrow.ExplosiveArrow;
import de.nulldrei.saintsandsinners.entity.projectile.arrow.LureArrow;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.monster.Monster;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SASEntities {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, SaintsAndSinners.MODID);


    public static final RegistryObject<EntityType<BeggarSurvivor>> BEGGAR_SURVIVOR =
            ENTITY_TYPES.register("beggar_survivor", () -> EntityType.Builder.of(BeggarSurvivor::new, MobCategory.CREATURE)
                    .sized(1, 2).build("beggar_survivor"));

    public static final RegistryObject<EntityType<RobberSurvivor>> ROBBER_SURVIVOR =
            ENTITY_TYPES.register("robber_survivor", () -> EntityType.Builder.of(RobberSurvivor::new, MobCategory.CREATURE)
                    .sized(1, 2).build("robber_survivor"));

    public static final RegistryObject<EntityType<TowerFactionSurvivor>> TOWER_FACTION_SURVIVOR =
            ENTITY_TYPES.register("tower_guard", () -> EntityType.Builder.of(TowerFactionSurvivor::new, MobCategory.CREATURE)
                    .sized(1, 2).build("tower_guard"));

    public static final RegistryObject<EntityType<ReclaimedFactionSurvivor>> RECLAIMED_FACTION_SURVIVOR =
            ENTITY_TYPES.register("reclaimed_cultist", () -> EntityType.Builder.of(ReclaimedFactionSurvivor::new, MobCategory.CREATURE)
                    .sized(1, 2).build("reclaimed_cultist"));

    public static final RegistryObject<EntityType<ExplosiveArrow>> EXPLOSIVE_ARROW =
            ENTITY_TYPES.register("explosive_arrow", () -> EntityType.Builder.<ExplosiveArrow>of(ExplosiveArrow::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(20).build("explosive_arrow"));

    public static final RegistryObject<EntityType<LureArrow>> LURE_ARROW =
            ENTITY_TYPES.register("lure_arrow", () -> EntityType.Builder.<LureArrow>of(LureArrow::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(20).build("lure_arrow"));

    public static final RegistryObject<EntityType<NailBomb>> NAIL_BOMB =
            ENTITY_TYPES.register("nail_bomb", () -> EntityType.Builder.<NailBomb>of(NailBomb::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(20).build("nail_bomb"));

    public static final RegistryObject<EntityType<StickyProximityBomb>> STICKY_PROXIMITY_BOMB =
            ENTITY_TYPES.register("sticky_proximity_bomb", () -> EntityType.Builder.<StickyProximityBomb>of(StickyProximityBomb::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(20).build("sticky_proximity_bomb"));

    public static final RegistryObject<EntityType<Decapitated>> DECAPITATED =
            ENTITY_TYPES.register("decapitated", () -> EntityType.Builder.<Decapitated>of(Decapitated::new, MobCategory.CREATURE)
                    .sized(1, 2).build("decapitated"));

    public static final RegistryObject<EntityType<ThrownTimedNoiseMakerBomb>> THROWN_TIMED_NOISE_MAKER_BOMB =
            ENTITY_TYPES.register("thrown_timed_noise_maker_bomb", () -> EntityType.Builder.<ThrownTimedNoiseMakerBomb>of(ThrownTimedNoiseMakerBomb::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(20).build("thrown_timed_noise_maker_bomb"));

    public static final RegistryObject<EntityType<TimedNoiseMakerBomb>> TIMED_NOISE_MAKER_BOMB =
            ENTITY_TYPES.register("timed_noise_maker_bomb", () -> EntityType.Builder.<TimedNoiseMakerBomb>of(TimedNoiseMakerBomb::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(20).build("timed_noise_maker_bomb"));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }

}
