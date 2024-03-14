package de.nulldrei.saintsandsinners;

import de.nulldrei.saintsandsinners.config.PatrolSpawning;
import de.nulldrei.saintsandsinners.config.ZombieDifficulty;
import de.nulldrei.saintsandsinners.config.ZombieSpawning;
import de.nulldrei.saintsandsinners.data.SASWorldData;
import de.nulldrei.saintsandsinners.entity.SASEntities;
import de.nulldrei.saintsandsinners.entity.hostile.AbstractFactionSurvivor;
import de.nulldrei.saintsandsinners.entity.hostile.ReclaimedFactionSurvivor;
import de.nulldrei.saintsandsinners.entity.hostile.TowerFactionSurvivor;
import de.nulldrei.saintsandsinners.item.SASItems;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;

import java.util.*;

public class SASUtil {

    public static ArrayList<String> neededItems = new ArrayList<>();
    public static ArrayList<String> demandedItems = new ArrayList<>();
    public static HashMap<String, SASWorldData> lookupWorldData = new HashMap<>();
    public static Random rand = new Random();
    private static int lastFactionSpawned = 999;
    private static int lastFactionSpawnedX = 0;
    private static int lastFactionSapwnedY = 0;
    private static int lastFactionSpawnedZ = 0;

    public static void tickPlayer(Player player) {
        if(player.level().getDifficulty() != Difficulty.PEACEFUL && !player.level().isClientSide()) {
            //Patrol Spawn
            if (PatrolSpawning.patrolSpawnRandomPool <= 0 || rand.nextInt(PatrolSpawning.patrolSpawnRandomPool) == 0) {
                spawnNewPatrol(player);
                if(PatrolSpawning.patrolRivalSpawnRandomPool <= 0 || rand.nextInt(PatrolSpawning.patrolRivalSpawnRandomPool) == 0) {
                    int randSize = player.level().random.nextInt(PatrolSpawning.patrolRivalSpawnMaxGroupSize) + 1;
                    for (int i = 0; i < randSize; i++) {
                        spawnRivalingPatrol(player);
                    }
                }
            }
            if(!isInDarkCave(player.level(), player.getOnPos().below().getX(), player.getOnPos().below().getY(), player.getOnPos().below().getZ(), false)) {
                //Day Zombies
                if (ZombieDifficulty.spawnDayZombies) {
                    if (player.level().isDay()) {
                        if (!isZombieCapReached((ServerLevel) player.level(), player)) {
                            int rand2 = rand.nextInt(ZombieSpawning.daySpawningSurfaceRandomPool);
                            if (ZombieSpawning.daySpawningSurfaceRandomPool <= 0 || rand2 == 0) {
                                spawnNewMobSurface(player);
                            }
                        }
                    }
                }
                //Extra Night
                if (ZombieDifficulty.spawnExtraNightZombies) {
                    if (!player.level().isDay()) {
                        if (!isZombieCapReached((ServerLevel) player.level(), player)) {
                            if (ZombieSpawning.extraNightSpawningSurfaceRandomPool <= 0 || rand.nextInt(ZombieSpawning.extraNightSpawningSurfaceRandomPool) == 0) {
                                spawnNewMobSurface(player);
                            }
                        }
                    }
                }
            } else if (ZombieDifficulty.extraSpawningCave) {
                if (!isZombieCapReached((ServerLevel) player.level(), player)) {
                    if (ZombieSpawning.extraSpawningCavesRandomPool <= 0 || rand.nextInt(ZombieSpawning.extraSpawningCavesRandomPool) == 0) {
                        spawnNewMobCave(player);
                    }
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
                SaintsAndSinners.LOGGER.info("spawnNewMobCave: " + tryX + ", " + tryY + ", " + tryZ + ", At time: " + player.level().getDayTime());
                return;
        }
    }

    public static boolean isInDarkCave(Level world, int x, int y, int z, boolean checkSpaceToSpawn) {
        BlockPos pos = new BlockPos(x, y, z);
        BlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        if (!world.canSeeSky(pos) && world.getLightEmission(pos) < 5) {
            if (!(block instanceof AirBlock) && (block == Blocks.STONE || block == Blocks.DEEPSLATE|| block == Blocks.BLACKSTONE || block == Blocks.MOSS_BLOCK || block == Blocks.AMETHYST_BLOCK || block == Blocks.ANDESITE || block == Blocks.GRANITE || block == Blocks.GRAVEL || block == Blocks.MOSS_BLOCK || block == Blocks.COAL_BLOCK || block == Blocks.IRON_ORE || block == Blocks.COPPER_ORE || block == Blocks.GOLD_ORE || block == Blocks.COBBLESTONE || block == Blocks.DIAMOND_ORE || block == Blocks.OBSIDIAN || block == Blocks.REDSTONE_ORE || block == Blocks.LAPIS_ORE || block == Blocks.DIORITE || block == Blocks.TUFF)) {
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

    public static void spawnNewPatrol(Player player) {
        int minDist = PatrolSpawning.patrolSpawningMinDist;
        int maxDist = PatrolSpawning.patrolSpawningMaxDist;
        int range = maxDist * 2;
        for (int tries = 0; tries < 5; tries++) {
            int tryX = ((int)player.getX() - range / 2 + rand.nextInt(range));
            int tryZ = ((int)player.getZ() - range / 2 + rand.nextInt(range));
            int tryY = player.level().getHeight(Heightmap.Types.MOTION_BLOCKING, (int)Math.floor(tryX), (int)Math.floor(tryZ));
            if ((int)distance((int)player.getX(), (int)player.getZ(), tryX, tryZ) < minDist || (int)distance((int)player.getX(), (int)player.getZ(), tryX, tryZ) > maxDist ||
                    player.level().getLightEmission(new BlockPos(tryX, tryY, tryZ)) >= 6) {
                continue;
            }
            int randSize = player.level().random.nextInt(PatrolSpawning.patrolGroupMaxSize) + 1;
            ServerLevel world = (ServerLevel) player.level();
            int spawned = 0;
            int factionType = world.random.nextInt(2);
            lastFactionSpawnedX = tryX;
            lastFactionSapwnedY = tryY;
            lastFactionSpawnedZ = tryZ;
            lastFactionSpawned = factionType;
            for (int i = 0; i < randSize; i++) {
                spawnFactionSurvivorAllowed(player, world, tryX, tryY, tryZ, factionType);
                spawned++;
            }
            if(spawned < PatrolSpawning.patrolGroupMinSize) {
                spawnFactionSurvivorAllowed(player, world, tryX, tryY, tryZ, factionType);
            }

            return;
        }
    }

    private static void spawnRivalingPatrol(Player player) {
        if(lastFactionSpawned == 0) {
            spawnFactionSurvivorAllowed(player, (ServerLevel) player.level(), lastFactionSpawnedX + 10, lastFactionSapwnedY, lastFactionSpawnedZ + 10,  1);
        } else {
            spawnFactionSurvivorAllowed(player, (ServerLevel) player.level(), lastFactionSpawnedX + 10, lastFactionSapwnedY, lastFactionSpawnedZ + 10, 0);
        }
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

    public static void spawnFactionSurvivorAllowed(Player player, ServerLevel world, int tryX, int tryY, int tryZ, int factionType){
        AbstractFactionSurvivor entFS;
        if(factionType == 0) {
            entFS = new TowerFactionSurvivor(SASEntities.TOWER_FACTION_SURVIVOR.get(), world);
        } else {
            entFS = new ReclaimedFactionSurvivor(SASEntities.RECLAIMED_FACTION_SURVIVOR.get(), world);
        }
        entFS.setPos(tryX + 0.5F, tryY + 1.1F, tryZ + 0.5F);
        entFS.finalizeSpawn(world.getLevel(), world.getCurrentDifficultyAt(new BlockPos(entFS.getBlockX(), entFS.getBlockY(), entFS.getBlockZ())), MobSpawnType.MOB_SUMMONED, null, null);
        giveRandomSpeedBoost(entFS);
        world.addFreshEntity(entFS);

        if (PatrolSpawning.patrolAutoTargetPlayer) entFS.setTarget(player);

        SaintsAndSinners.LOGGER.info("spawnNewPatrolMob: " + tryX + ", " + tryY + ", " + tryZ);

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

    public static boolean doesPlayerWearRottenFleshArmor(Player player) {
        Iterable<ItemStack> armorSlots = player.getArmorSlots();
        boolean isWearingHeadPiece = false;
        boolean isWearingChestPiece = false;
        boolean isWearingLegPiece = false;
        boolean isWearingFootPiece = false;
        for(ItemStack armorPiece : armorSlots) {
            Item armorPieceItem = armorPiece.getItem();
            if (armorPieceItem == SASItems.ROTTEN_BRAIN_MATTER.get()) {
                isWearingHeadPiece = true;
            } else if (armorPieceItem == SASItems.ROTTEN_INTESTINES.get()) {
                isWearingChestPiece = true;
            } else if (armorPieceItem == SASItems.ROTTEN_LEGS.get()) {
                isWearingLegPiece = true;
            } else if (armorPieceItem == SASItems.ROTTEN_TOES.get()) {
                isWearingFootPiece = true;
            }
        }
        return isWearingHeadPiece && isWearingChestPiece && isWearingLegPiece && isWearingFootPiece;
    }

    public static double distance(int x1, int z1, int x2, int z2) {
        return Math.sqrt((Math.pow(x1 - x2, 2)) + (Math.pow(z1 - z2, 2)));
    }

    public static boolean isZombieCapReached(ServerLevel level, Player player) {
        int counter = 0;
        if(!isInDarkCave(level, player.getOnPos().below().getX(), player.getOnPos().below().getY(), player.getOnPos().below().getZ(), false)) {
            for (Entity entity : level.getAllEntities()) {
                //Day
                if (level.isDay()) {
                    if (counter < ZombieSpawning.daySpawningSurfaceMaxCount) {
                        if (entity instanceof Zombie zombie) {
                            counter++;
                        }
                    } else {
                        return true;
                    }
                } else {
                    if (counter < ZombieSpawning.extraNightSpawningSurfaceMaxCount) {
                        if (entity instanceof Zombie zombie) {
                            counter++;
                        }
                    } else {
                        return true;
                    }
                }
            }
        } else {
            AABB playerSpawnRange = player.getBoundingBox().inflate(500, 100, 500);
            for(Entity entity : level.getEntities(player, playerSpawnRange)) {
                if (counter < ZombieSpawning.extraSpawningCavesMaxCount) {
                    if (entity instanceof Zombie zombie) {
                        counter++;
                    }
                } else {
                    return true;
                }
            }
        }
        return false;
    }

}


