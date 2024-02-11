package de.nulldrei.saintsandsinners;

import de.nulldrei.saintsandsinners.config.ZombieDifficulty;
import de.nulldrei.saintsandsinners.config.ZombieSpawning;
import de.nulldrei.saintsandsinners.data.WorldData;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

public class SASUtil {

    public static ArrayList<String> neededItems = new ArrayList<>();
    public static ArrayList<String> demandedItems = new ArrayList<>();
    public static HashMap<String, WorldData> lookupWorldData = new HashMap<>();
    public static Random rand = new Random();

    public static void tickPlayer(Player player) {

        //Day Zombies
        if(ZombieDifficulty.spawnDayZombies) {
            if (player.level().isDay()) {
                if (getWorldData(player.level().dimension().toString()).lastMobsCountSurface < ZombieSpawning.daySpawningSurfaceMaxCount) {
                    int rand2 =  rand.nextInt(ZombieSpawning.daySpawningSurfaceRandomPool);
                    if (ZombieSpawning.daySpawningSurfaceRandomPool <= 0 || rand2 == 0) {
                        spawnNewMobSurface(player);
                    }
                }
            }
        }

        //Extra Night
        if(ZombieDifficulty.spawnExtraNightZombies) {
            if (!player.level().isDay()) {
                if (getWorldData(player.level().dimension().toString()).lastMobsCountSurface < ZombieSpawning.extraNightSpawningSurfaceMaxCount) {
                    if (ZombieSpawning.extraNightSpawningSurfaceRandomPool <= 0 || rand.nextInt(ZombieSpawning.extraNightSpawningSurfaceRandomPool) == 0) {
                        spawnNewMobSurface(player);
                    }
                }
            }
        }

        if (ZombieDifficulty.extraSpawningCave) {
            if (getWorldData(player.level().dimension().toString()).lastMobsCountSurface < ZombieSpawning.extraSpawningCavesMaxCount) {
                if (ZombieSpawning.extraSpawningCavesRandomPool <= 0 || rand.nextInt(ZombieSpawning.extraSpawningCavesRandomPool) == 0) {
                    spawnNewMobCave(player);
                }
            }
        }
    }

    public static void spawnNewMobSurface(Player player) {
        int minDist = 0;
        int maxDist = 0;
        int randSize = 0;
        if(player.level().isDay()) {
            minDist = ZombieSpawning.daySpawningDistMin;
            maxDist = ZombieSpawning.daySpawningDistMax;
            randSize = player.level().random.nextInt(ZombieSpawning.daySpawningSurfaceMaxGroupSize) + 1;
        } else {
            minDist = ZombieSpawning.extraNightSpawningDistMin;
            maxDist = ZombieSpawning.extraNightSpawningDistMax;
            randSize = player.level().random.nextInt(ZombieSpawning.daySpawningSurfaceMaxGroupSize) + 1;
        }
        int range = maxDist * 2;

        for (int tries = 0; tries < 5; tries++) {
            int tryX = ((int)player.getX() - range / 2 + rand.nextInt(range));
            int tryZ = ((int)player.getZ() - range / 2 + rand.nextInt(range));
            int tryY = player.level().getHeight(Heightmap.Types.MOTION_BLOCKING, (int)Math.floor(tryX), (int)Math.floor(tryZ));
            if ((int)distance((int)player.getX(), (int)player.getZ(), tryX, tryZ) < minDist || (int)distance((int)player.getX(), (int)player.getZ(), tryX, tryZ) > maxDist ||
                    player.level().getLightEmission(new BlockPos(tryX, tryY, tryZ)) >= 6) {
               continue;
            }
            ServerLevel world = (ServerLevel) player.level();
            for (int i = 0; i < randSize; i++) {
                spawnMobsAllowed(player, world, tryX, tryY, tryZ);
            }


            SaintsAndSinners.LOGGER.info("spawnNewMobSurface: " + tryX + ", " + tryY + ", " + tryZ + ", At time: " + player.level().getDayTime());

            return;
        }
    }

    public static void spawnNewMobCave(Player player) {
        int minDist = ZombieSpawning.extraSpawningCavesDistMin;
        int maxDist = ZombieSpawning.extraSpawningCavesDistMax;
        int range = maxDist * 2;
        for (int tries = 0; tries < ZombieSpawning.extraSpawningCavesTryCount; tries++) {
            int tryX = ((int)player.getX() - range / 2 + rand.nextInt(range));
            int tryY = ((int)player.getY());
            int tryZ = ((int)player.getZ() - range / 2 + rand.nextInt(range));

           if ((int)distance((int)player.getX(), (int)player.getZ(), tryX, tryZ) < minDist || (int)distance((int)player.getX(), (int)player.getZ(), tryX, tryZ) > maxDist || !canSpawnMobOnGround(player.level(), tryX, tryY, tryZ) || !isInDarkCave(player.level(), tryX, tryY, tryZ, true)) {
                continue;
           }
                int randSize = player.level().random.nextInt(ZombieSpawning.extraSpawningCavesMaxGroupSize) + 1;
                ServerLevel world = (ServerLevel) player.level();
                for (int i = 0; i < randSize; i++) {
                    spawnMobsAllowed(player, world, tryX, tryY, tryZ);
                }
                return;
        }
    }

    public static boolean isInDarkCave(Level world, int x, int y, int z, boolean checkSpaceToSpawn) {
        BlockPos pos = new BlockPos(x, y, z);
        BlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        if (!world.canSeeSky(pos) && world.getLightEmission(pos) < 5) {

            if (!(block instanceof AirBlock) && (block == Blocks.STONE || block == Blocks.DEEPSLATE|| block == Blocks.BLACKSTONE || block == Blocks.MOSS_BLOCK) /*(block != Blocks.grass || block.getMaterial() != Material.grass)*/) {
                if (!checkSpaceToSpawn) {
                    return true;
                } else {
                    Block blockAir1 = world.getBlockState(new BlockPos(x, y+1, z)).getBlock();
                    if (blockAir1 instanceof AirBlock) {
                        Block blockAir2 = world.getBlockState(new BlockPos(x, y+2, z)).getBlock();
                        return blockAir2 instanceof AirBlock;
                    }
                }
            }
        }
        return false;
    }

    public static boolean canSpawnMobOnGround(Level world, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        BlockState state = world.getBlockState(pos);
        Block block = state.getBlock();//Block.pressurePlatePlanks.blockID;


        return (!(block instanceof AirBlock)) && block.isValidSpawn(state, world, pos, SpawnPlacements.Type.ON_GROUND, EntityType.ZOMBIE);
    }

    public static void spawnMobsAllowed(Player player, ServerLevel world, int tryX, int tryY, int tryZ){
            Zombie entZ = new Zombie(world);
            entZ.setPos(tryX + 0.5F, tryY + 1.1F, tryZ + 0.5F);
            entZ.finalizeSpawn(world.getLevel(), world.getCurrentDifficultyAt(new BlockPos(entZ.getBlockX(), entZ.getBlockY(), entZ.getBlockZ())), MobSpawnType.MOB_SUMMONED, null, null);
            giveRandomSpeedBoost(entZ);
            world.addFreshEntity(entZ);

            if (ZombieSpawning.extraSpawningAutoTarget) entZ.setTarget(player);

            SaintsAndSinners.LOGGER.info("spawnNewMob: " + tryX + ", " + tryY + ", " + tryZ);

    }

    public static void giveRandomSpeedBoost(LivingEntity ent) {
        if (ZombieDifficulty.randomSpeedBoost > 0) {
            double randBoost = ent.level().random.nextDouble() * ZombieDifficulty.randomSpeedBoost;
            AttributeModifier speedBoostModifier = new AttributeModifier(UUID.fromString("8dd7fab2-5bf6-4d07-9c0f-22b3512c1494"), "SAS speed boost", randBoost, AttributeModifier.Operation.fromValue(AttributeModifier.Operation.MULTIPLY_BASE.ordinal()));
            if (!ent.getAttribute(Attributes.MOVEMENT_SPEED).hasModifier(speedBoostModifier)) {

                ent.getAttribute(Attributes.MOVEMENT_SPEED).addPermanentModifier(speedBoostModifier);
            }
        }

    }


    public static boolean isInventoryFull(Player player) {
        for(ItemStack item : player.getInventory().items) {
            if(item.isEmpty()) return false;
        }
        return true;
    }

    public static boolean doSurvivorsFindItemUseful(ItemStack itemStack) {
        return neededItems.contains(itemStack.toString());
    }

    public static boolean doSurvivorsDemandItem(ItemStack itemStack) {
        return demandedItems.contains(itemStack.toString());
    }

    public static double distance(int x1, int z1, int x2, int z2) {
        return Math.sqrt((Math.pow(x1 - x2, 2)) + (Math.pow(z1 - z2, 2)));
    }

    public static WorldData getWorldData(String dimension) {
        if (!lookupWorldData.containsKey(dimension)) {
            lookupWorldData.put(dimension, new WorldData());
        }
        return lookupWorldData.get(dimension);
    }

}


