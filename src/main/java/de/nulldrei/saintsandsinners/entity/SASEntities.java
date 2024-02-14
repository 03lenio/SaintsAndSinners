package de.nulldrei.saintsandsinners.entity;

import de.nulldrei.saintsandsinners.SaintsAndSinners;
import de.nulldrei.saintsandsinners.entity.hostile.ReclaimedFactionSurvivor;
import de.nulldrei.saintsandsinners.entity.hostile.TowerFactionSurvivor;
import de.nulldrei.saintsandsinners.entity.hostile.variant.ReclaimedVariant;
import de.nulldrei.saintsandsinners.entity.neutral.RobberSurvivor;
import de.nulldrei.saintsandsinners.entity.peaceful.BeggarSurvivor;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.animal.goat.GoatAi;
import net.minecraft.world.entity.monster.warden.WardenAi;
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



    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }

}
