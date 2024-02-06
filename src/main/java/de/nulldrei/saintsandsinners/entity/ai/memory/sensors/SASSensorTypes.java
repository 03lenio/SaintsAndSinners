package de.nulldrei.saintsandsinners.entity.ai.memory.sensors;

import de.nulldrei.saintsandsinners.SaintsAndSinners;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SASSensorTypes<U> {

    public static final DeferredRegister<SensorType<?>> SENSOR_TYPES =
                DeferredRegister.create(ForgeRegistries.SENSOR_TYPES, SaintsAndSinners.MODID);


   public static final RegistryObject<SensorType<BeggarSurvivorSpecificSensor>> BEGGAR_SURVIVOR_SPECIFIC_SENSOR = SENSOR_TYPES.register("beggar_survivor_specific_sensor",
           () -> new SensorType<>(BeggarSurvivorSpecificSensor::new));




    public static void register(IEventBus eventBus) {
        SENSOR_TYPES.register(eventBus);
    }





}
