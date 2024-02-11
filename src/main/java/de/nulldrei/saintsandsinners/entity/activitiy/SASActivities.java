package de.nulldrei.saintsandsinners.entity.activitiy;

import de.nulldrei.saintsandsinners.SaintsAndSinners;
import de.nulldrei.saintsandsinners.entity.neutral.AbstractSurvivor;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;
import java.util.Optional;

public class SASActivities {


    public static final DeferredRegister<Activity> ACTIVITIES =
            DeferredRegister.create(ForgeRegistries.ACTIVITIES, SaintsAndSinners.MODID);

    public static final RegistryObject<Activity> DEMAND_ITEM = ACTIVITIES.register("demand_item",
            () -> new Activity("demand_item"));


    public static void register(IEventBus eventBus) {
        ACTIVITIES.register(eventBus);
    }


}
