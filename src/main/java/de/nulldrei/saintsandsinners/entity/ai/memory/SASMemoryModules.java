package de.nulldrei.saintsandsinners.entity.ai.memory;

import de.nulldrei.saintsandsinners.SaintsAndSinners;
import de.nulldrei.saintsandsinners.entity.neutral.AbstractSurvivor;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;
import java.util.Optional;

public class SASMemoryModules<U> {

    public static final DeferredRegister<MemoryModuleType<?>> MEMORY_MODULE_TYPES =
                DeferredRegister.create(ForgeRegistries.MEMORY_MODULE_TYPES, SaintsAndSinners.MODID);


   public static final RegistryObject<MemoryModuleType<Integer>> VISIBLE_ZOMBIE_COUNT = MEMORY_MODULE_TYPES.register("visible_zombie_count",
           () -> new MemoryModuleType<>(Optional.empty()));

    public static final RegistryObject<MemoryModuleType<Zombie>> NEAREST_VISIBLE_ZOMBIE = MEMORY_MODULE_TYPES.register("nearest_visible_zombie",
            () -> new MemoryModuleType<>(Optional.empty()));

    public static final RegistryObject<MemoryModuleType<List<Zombie>>> NEAREST_VISIBLE_ZOMBIES = MEMORY_MODULE_TYPES.register("nearest_visible_zombies",
            () -> new MemoryModuleType<>(Optional.empty()));

    public static final RegistryObject<MemoryModuleType<Integer>> VISIBLE_SURVIVOR_COUNT = MEMORY_MODULE_TYPES.register("visible_survivor_count",
            () -> new MemoryModuleType<>(Optional.empty()));

    public static final RegistryObject<MemoryModuleType<AbstractSurvivor>> NEAREST_SURVIVOR = MEMORY_MODULE_TYPES.register("nearest_survivor",
            () -> new MemoryModuleType<>(Optional.empty()));

    public static final RegistryObject<MemoryModuleType<List<AbstractSurvivor>>> NEARBY_SURVIVORS = MEMORY_MODULE_TYPES.register("nearby_survivors",
            () -> new MemoryModuleType<>(Optional.empty()));

    public static final RegistryObject<MemoryModuleType<List<AbstractSurvivor>>> NEAREST_VISIBLE_SURVIVORS = MEMORY_MODULE_TYPES.register("nearest_visible_survivors",
            () -> new MemoryModuleType<>(Optional.empty()));

    public static final RegistryObject<MemoryModuleType<Boolean>> IS_DEMANDING = MEMORY_MODULE_TYPES.register("is_demanding",
            () -> new MemoryModuleType<>(Optional.empty()));


    public static void register(IEventBus eventBus) {
        MEMORY_MODULE_TYPES.register(eventBus);
    }





}
