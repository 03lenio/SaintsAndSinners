package de.nulldrei.saintsandsinners.config;

import de.nulldrei.saintsandsinners.SaintsAndSinners;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Forge's config APIs
@Mod.EventBusSubscriber(modid = SaintsAndSinners.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SASConfig
{
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    //Difficulty
    private static final ForgeConfigSpec.BooleanValue SPAWN_EXTRA_ZOMBIES_DAY_SURFACE = BUILDER
            .comment("If SaintsAndSinners should spawn zombies during the day on the surface")
            .define("extraZombiesSpawningDay", true);
    private static final ForgeConfigSpec.BooleanValue SPAWN_EXTRA_ZOMBIES_NIGHT_SURFACE = BUILDER
            .comment("If SaintsAndSinners should spawn additional zombies during the night on the surface")
            .define("extraZombiesSpawningNight", true);
    private static final ForgeConfigSpec.BooleanValue SPAWN_EXTRA_ZOMBIES_CAVE = BUILDER
            .comment("If SaintsAndSinners should spawn additional zombies in caves")
            .define("extraZombiesSpawningCave", true);
    private static final ForgeConfigSpec.DoubleValue EXTRA_ZOMBIE_SPEED_BOOST = BUILDER
            .comment("The maximum speed boost SaintsAndSinners spawned zombies should receive upon spawning")
            .defineInRange("extraZombiesSpeedBoost", 0.3D, 0D, Double.MAX_VALUE);
    //Spawning
    //PatrolSpawning
    private static final ForgeConfigSpec.IntValue PATROL_SPAWN_POOL = BUILDER
            .comment("The chance every tick to spawn a patrol")
            .defineInRange("patrolSpawnPool", 150, 50, Integer.MAX_VALUE);

    private static final ForgeConfigSpec.IntValue PATROL_SPAWN_MAX_DIST = BUILDER
            .comment("The maximum distance patrols spawn away from the player.")
            .defineInRange("patrolSpawnMaxDist", 40, 20, Integer.MAX_VALUE);

    private static final ForgeConfigSpec.IntValue PATROL_SPAWN_MIN_DIST = BUILDER
            .comment("The minimum distance patrols spawn away from the player")
            .defineInRange("patrolSpawnMinDist", 20, 5, Integer.MAX_VALUE);

    private static final ForgeConfigSpec.IntValue PATROL_MAX_GROUP_SIZE = BUILDER
            .comment("The maximum amount of survivors that can be spawned per patrol")
            .defineInRange("patrolSpawnMaxGroupSize", 7, 2, Integer.MAX_VALUE);


    private static final ForgeConfigSpec.IntValue PATROL_MIN_GROUP_SIZE = BUILDER
            .comment("The minimum amount of survivors that can be spawned per patrol")
            .defineInRange("patrolSpawnMinGroupSize", 4, 2, Integer.MAX_VALUE);

    private static final ForgeConfigSpec.BooleanValue PATROL_AUTO_TARGET_PLAYER = BUILDER
            .comment("If patrols should automatically seek and attack you upon spawning")
            .define("patrolAutoTargetPlayer", true);

    private static final ForgeConfigSpec.IntValue PATROL_RIVAL_SPAWN_POOL = BUILDER
            .comment("The chance that a rivaling patrol spawns in addition to the regular patrol spawn")
            .defineInRange("patrolRivalSpawnPool", 5, 1, Integer.MAX_VALUE);

    private static final ForgeConfigSpec.IntValue PATROL_RIVAL_MAX_GROUP_SIZE = BUILDER
            .comment("The maximum amount of survivors that can be spawned per patrol")
            .defineInRange("patrolRivalMaxGroupSize", 7, 2, Integer.MAX_VALUE);

    //Zombie spawning

    //Day
    private static final ForgeConfigSpec.BooleanValue EXTRA_ZOMBIES_AUTO_TARGET_PLAYER = BUILDER
            .comment("If zombies that are spawned by SaintsAndSinners automatically seek and attack you upon spawning")
            .define("extraZombiesAutoTargetPlayer", false);

    private static final ForgeConfigSpec.IntValue EXTRA_SURFACE_MAX_COUNT = BUILDER
            .comment("The maximum amount of SaintsAndSinners spawned zombies that are allowed to exist on the surface during the day")
            .defineInRange("extraZombieDayMaxCount", 30, 0, Integer.MAX_VALUE);

    private static final ForgeConfigSpec.IntValue EXTRA_SURFACE_SPAWN_POOL = BUILDER
            .comment("The chance every tick to spawn zombies on the surface while it's day")
            .defineInRange("extraZombieDaySpawnPool", 5, 1, Integer.MAX_VALUE);

    private static final ForgeConfigSpec.IntValue EXTRA_SURFACE_SPAWN_MIN_DIST = BUILDER
            .comment("The minimum distance SaintsAndSinners spawned Zombies spawn away from the player on the surface during the day.")
            .defineInRange("extraZombieDaySpawnMinDist", 30, 5, Integer.MAX_VALUE);

    private static final ForgeConfigSpec.IntValue EXTRA_SURFACE_SPAWN_MAX_DIST = BUILDER
            .comment("The maximum distance SaintsAndSinners spawned Zombies spawn away from the player on the surface during the day.")
            .defineInRange("extraZombieDaySpawnMaxDist", 70, 20, Integer.MAX_VALUE);

    private static final ForgeConfigSpec.IntValue EXTRA_SURFACE_MAX_GROUP_SIZE = BUILDER
            .comment("The maximum amount of SaintsAndSinners spawned zombie that can be spawned per spawn Event during the day on the surface")
            .defineInRange("extraZombieDaySpawnMaxGroupSize", 7, 2, Integer.MAX_VALUE);

    //Night
    private static final ForgeConfigSpec.IntValue EXTRA_SURFACE_NIGHT_MAX_COUNT = BUILDER
            .comment("The maximum amount of SaintsAndSinners spawned zombies that are allowed to exist on the surface during the night")
            .defineInRange("extraZombieNightSpawnPool", 50, 0, Integer.MAX_VALUE);

    private static final ForgeConfigSpec.IntValue EXTRA_SURFACE_NIGHT_SPAWN_POOL = BUILDER
            .comment("The chance every tick to spawn zombies on the surface while it's night")
            .defineInRange("extraZombieNightSpawnPool", 3, 1, Integer.MAX_VALUE);

    private static final ForgeConfigSpec.IntValue EXTRA_SURFACE_NIGHT_SPAWN_MIN_DIST = BUILDER
            .comment("The minimum distance SaintsAndSinners spawned Zombies spawn away from the player on the surface during the night.")
            .defineInRange("extraZombieNightSpawnMinDist", 25, 20, Integer.MAX_VALUE);

    private static final ForgeConfigSpec.IntValue EXTRA_SURFACE_NIGHT_SPAWN_MAX_DIST = BUILDER
            .comment("The maximum distance SaintsAndSinners spawned Zombies spawn away from the player on the surface during the night.")
            .defineInRange("extraZombieNightSpawnMaxDist", 70, 5, Integer.MAX_VALUE);

    //Caves
    private static final ForgeConfigSpec.IntValue EXTRA_CAVE_MAX_COUNT = BUILDER
            .comment("The maximum amount of SaintsAndSinners spawned zombies that are allowed to exist on the surface during the night")
            .defineInRange("extraZombieCaveSpawnPool", 50, 0, Integer.MAX_VALUE);

    private static final ForgeConfigSpec.IntValue EXTRA_CAVE_SPAWN_POOL = BUILDER
            .comment("The chance every tick to spawn zombies while in a cave")
            .defineInRange("extraZombieCaveSpawnPool", 1, 1, Integer.MAX_VALUE);

    private static final ForgeConfigSpec.IntValue EXTRA_CAVE_SPAWN_MIN_DIST = BUILDER
            .comment("The minimum distance SaintsAndSinners spawned Zombies spawn away from the player while in a cave")
            .defineInRange("extraZombieCaveSpawnMinDist", 50, 20, Integer.MAX_VALUE);

    private static final ForgeConfigSpec.IntValue EXTRA_CAVE_SPAWN_MAX_DIST = BUILDER
            .comment("The maximum distance SaintsAndSinners spawned Zombies spawn away from the player while in a cave.")
            .defineInRange("extraZombieCaveSpawnMaxDist", 100, 40, Integer.MAX_VALUE);

    private static final ForgeConfigSpec.IntValue EXTRA_CAVE_MAX_GROUP_SIZE = BUILDER
            .comment("The maximum amount of SaintsAndSinners spawned zombie that can be spawned per spawn Event while in a cave")
            .defineInRange("extraZombieCaveSpawnMaxGroupSize", 5, 2, Integer.MAX_VALUE);

    private static final ForgeConfigSpec.IntValue EXTRA_CAVE_SPAWN_ATTEMPTS = BUILDER
            .comment("The amount of attempts SaintsAndSinners should make while trying to spawn a zombie in a cave")
            .defineInRange("extraZombieCaveSpawnTryCount", 15, 0, Integer.MAX_VALUE);


    public static final ForgeConfigSpec SPEC = BUILDER.build();

    public static boolean logDirtBlock;
    public static int magicNumber;
    public static String magicNumberIntroduction;
    public static Set<Item> items;

    private static boolean validateItemName(final Object obj)
    {
        return obj instanceof final String itemName && ForgeRegistries.ITEMS.containsKey(new ResourceLocation(itemName));
    }

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        PatrolSpawning.patrolSpawnRandomPool = PATROL_SPAWN_POOL.get();
        PatrolSpawning.patrolSpawningMaxDist = PATROL_SPAWN_MAX_DIST.get();
        PatrolSpawning.patrolSpawningMinDist = PATROL_SPAWN_MIN_DIST.get();
        PatrolSpawning.patrolGroupMaxSize = PATROL_MAX_GROUP_SIZE.get();
        PatrolSpawning.patrolGroupMinSize = PATROL_MIN_GROUP_SIZE.get();
        PatrolSpawning.patrolAutoTargetPlayer = PATROL_AUTO_TARGET_PLAYER.get();
        PatrolSpawning.patrolRivalSpawnMaxGroupSize = PATROL_RIVAL_SPAWN_POOL.get();
        PatrolSpawning.patrolRivalSpawnMaxGroupSize = PATROL_RIVAL_MAX_GROUP_SIZE.get();

        ZombieDifficulty.spawnDayZombies = SPAWN_EXTRA_ZOMBIES_DAY_SURFACE.get();
        ZombieDifficulty.spawnExtraNightZombies = SPAWN_EXTRA_ZOMBIES_NIGHT_SURFACE.get();
        ZombieDifficulty.extraSpawningCave = SPAWN_EXTRA_ZOMBIES_CAVE.get();
        ZombieDifficulty.randomSpeedBoost = EXTRA_ZOMBIE_SPEED_BOOST.get();

        ZombieSpawning.extraSpawningAutoTarget = EXTRA_ZOMBIES_AUTO_TARGET_PLAYER.get();
        ZombieSpawning.daySpawningSurfaceMaxCount = EXTRA_SURFACE_MAX_COUNT.get();
        ZombieSpawning.daySpawningSurfaceRandomPool = EXTRA_SURFACE_SPAWN_POOL.get();
        ZombieSpawning.daySpawningDistMin = EXTRA_SURFACE_SPAWN_MIN_DIST.get();
        ZombieSpawning.daySpawningDistMax = EXTRA_SURFACE_SPAWN_MAX_DIST.get();
        ZombieSpawning.daySpawningSurfaceMaxGroupSize = EXTRA_SURFACE_MAX_GROUP_SIZE.get();
        ZombieSpawning.extraNightSpawningSurfaceMaxCount = EXTRA_SURFACE_NIGHT_MAX_COUNT.get();
        ZombieSpawning.extraNightSpawningSurfaceRandomPool = EXTRA_SURFACE_NIGHT_SPAWN_POOL.get();
        ZombieSpawning.extraNightSpawningDistMin = EXTRA_SURFACE_NIGHT_SPAWN_MIN_DIST.get();
        ZombieSpawning.extraNightSpawningDistMax = EXTRA_SURFACE_NIGHT_SPAWN_MAX_DIST.get();
        ZombieSpawning.extraSpawningCavesMaxCount = EXTRA_CAVE_MAX_COUNT.get();
        ZombieSpawning.extraSpawningCavesRandomPool = EXTRA_CAVE_SPAWN_POOL.get();
        ZombieSpawning.extraSpawningCavesDistMin = EXTRA_CAVE_SPAWN_MIN_DIST.get();
        ZombieSpawning.extraSpawningCavesDistMax = EXTRA_CAVE_SPAWN_MAX_DIST.get();
        ZombieSpawning.extraSpawningCavesMaxGroupSize = EXTRA_CAVE_MAX_GROUP_SIZE.get();
        ZombieSpawning.extraSpawningCavesTryCount = EXTRA_CAVE_SPAWN_ATTEMPTS.get();
    }
}

