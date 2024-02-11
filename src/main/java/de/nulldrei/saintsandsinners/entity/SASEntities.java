package de.nulldrei.saintsandsinners.entity;

import de.nulldrei.saintsandsinners.SaintsAndSinners;
import de.nulldrei.saintsandsinners.entity.neutral.RobberSurvivor;
import de.nulldrei.saintsandsinners.entity.peaceful.BeggarSurvivor;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
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


    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }

}
