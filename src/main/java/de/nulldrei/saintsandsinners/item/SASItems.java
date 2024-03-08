package de.nulldrei.saintsandsinners.item;

import de.nulldrei.saintsandsinners.SaintsAndSinners;
import de.nulldrei.saintsandsinners.armor.material.MaterialReclaimed;
import de.nulldrei.saintsandsinners.armor.material.MaterialRottenFlesh;
import de.nulldrei.saintsandsinners.armor.material.MaterialTower;
import de.nulldrei.saintsandsinners.block.SASBlocks;
import de.nulldrei.saintsandsinners.item.combat.melee.EsteemedMortal;
import de.nulldrei.saintsandsinners.item.combat.melee.SamedisHand;
import de.nulldrei.saintsandsinners.item.combat.melee.TheBat;
import de.nulldrei.saintsandsinners.item.combat.projectile.arrow.ExplosiveArrowItem;
import de.nulldrei.saintsandsinners.item.combat.projectile.arrow.LureArrowItem;
import de.nulldrei.saintsandsinners.item.skull.SurvivorHeadItem;
import de.nulldrei.saintsandsinners.data.SASLootTables;
import de.nulldrei.saintsandsinners.entity.SASEntities;
import de.nulldrei.saintsandsinners.item.armor.ReclaimedArmor;
import de.nulldrei.saintsandsinners.item.armor.RottenFleshArmor;
import de.nulldrei.saintsandsinners.item.combat.melee.Bottle;
import de.nulldrei.saintsandsinners.item.combat.projectile.*;
import de.nulldrei.saintsandsinners.item.stuff.BoxOfStuff;
import net.minecraft.core.Direction;
import net.minecraft.world.item.*;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SASItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, SaintsAndSinners.MODID);
    public static final RegistryObject<Item> BOTTLE = ITEMS.register("bottle", () -> new Bottle(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> DIAMOND_SHARD = ITEMS.register("diamond_shard", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> NETHERITE_FRAGMENT = ITEMS.register("netherite_fragment", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> COPPER_NUGGET = ITEMS.register("copper_nugget", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SHIV = ITEMS.register("shiv", () -> new SwordItem(Tiers.IRON, 2, -1.5F, new Item.Properties().durability(100)));
    public static final RegistryObject<Item> BROKEN_BOTTLE = ITEMS.register("broken_bottle", () -> new SwordItem(Tiers.STONE, 3, -0.5F, new Item.Properties().durability(15)));
    public static final RegistryObject<Item> SCREWDRIVER = ITEMS.register("screwdriver", () -> new SwordItem(Tiers.STONE, 2, -0.5F, new Item.Properties().durability(150)));
    public static final RegistryObject<ForgeSpawnEggItem> SPAWN_BEGGAR = ITEMS.register("beggar_spawn_egg", () -> new ForgeSpawnEggItem(SASEntities.BEGGAR_SURVIVOR, 0x965D22, 0xFFC51C, new Item.Properties()));
    public static final RegistryObject<Item>  GREEN_BOX_OF_STUFF = ITEMS.register("green_box_of_stuff", () -> new BoxOfStuff(new Item.Properties(), SASLootTables.GREEN_BOX_OF_STUFF_LOOT_TABLE));
    public static final RegistryObject<Item>  GREY_BOX_OF_STUFF = ITEMS.register("grey_box_of_stuff", () -> new BoxOfStuff(new Item.Properties(), SASLootTables.GREY_BOX_OF_STUFF_LOOT_TABLE));
    public static final RegistryObject<Item>  ORANGE_BOX_OF_STUFF = ITEMS.register("orange_box_of_stuff", () -> new BoxOfStuff(new Item.Properties(), SASLootTables.ORANGE_BOX_OF_STUFF_LOOT_TABLE));
    public static final RegistryObject<ForgeSpawnEggItem> SPAWN_ROBBER = ITEMS.register("robber_spawn_egg", () -> new ForgeSpawnEggItem(SASEntities.ROBBER_SURVIVOR, 0xff0303, 0xFFC51C, new Item.Properties()));
    public static final RegistryObject<ForgeSpawnEggItem> SPAWN_TOWER_GUARD = ITEMS.register("tower_guard_spawn_egg", () -> new ForgeSpawnEggItem(SASEntities.TOWER_FACTION_SURVIVOR, 0x030ffc, 0x3d3e4d, new Item.Properties()));
    public static final RegistryObject<ForgeSpawnEggItem> SPAWN_RECLAIMED_CULTIST = ITEMS.register("reclaimed_cultist_spawn_egg", () -> new ForgeSpawnEggItem(SASEntities.RECLAIMED_FACTION_SURVIVOR, 0x852424, 0x3d3e4d, new Item.Properties()));
    public static final RegistryObject<Item> RECLAIMED_MASK = ITEMS.register("reclaimed_mask",
            () -> new ReclaimedArmor(MaterialReclaimed.RECLAIMED, ArmorItem.Type.HELMET, new Item.Properties()));
    public static final RegistryObject<Item> TOWER_HELMET = ITEMS.register("tower_helmet",
            () -> new ArmorItem(MaterialTower.TOWER, ArmorItem.Type.HELMET, new Item.Properties()));
    public static final RegistryObject<Item> TOWER_VEST = ITEMS.register("tower_vest",
            () -> new ArmorItem(MaterialTower.TOWER, ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<Item> ROTTEN_BRAIN_MATTER = ITEMS.register("rotten_brain_matter",
            () -> new RottenFleshArmor(MaterialRottenFlesh.ROTTEN_FLESH, ArmorItem.Type.HELMET,  new Item.Properties().durability(10)));
    public static final RegistryObject<Item> ROTTEN_INTESTINES = ITEMS.register("rotten_intestines",
            () -> new RottenFleshArmor(MaterialRottenFlesh.ROTTEN_FLESH, ArmorItem.Type.CHESTPLATE,  new Item.Properties().durability(10)));
    public static final RegistryObject<Item> ROTTEN_LEGS = ITEMS.register("rotten_legs",
            () -> new RottenFleshArmor(MaterialRottenFlesh.ROTTEN_FLESH, ArmorItem.Type.LEGGINGS,  new Item.Properties().durability(10)));
    public static final RegistryObject<Item> ROTTEN_TOES = ITEMS.register("rotten_toes",
            () -> new RottenFleshArmor(MaterialRottenFlesh.ROTTEN_FLESH, ArmorItem.Type.BOOTS,  new Item.Properties().durability(10)));
    public static final RegistryObject<ArrowItem> EXPLOSIVE_ARROW = ITEMS.register("explosive_arrow",
            () -> new ExplosiveArrowItem(new Item.Properties()));
    public static final RegistryObject<ArrowItem> LURE_ARROW = ITEMS.register("lure_arrow",
            () -> new LureArrowItem(new Item.Properties()));
    public static final RegistryObject<NailBombItem> NAIL_BOMB = ITEMS.register("nail_bomb",
            () -> new NailBombItem(new Item.Properties()));
    public static final RegistryObject<StickyProximityBombItem> STICKY_PROXIMITY_BOMB = ITEMS.register("sticky_proximity_bomb",
            () -> new StickyProximityBombItem(new Item.Properties()));
    public static final RegistryObject<TimedNoiseMakerBombItem> TIMED_NOISE_MAKER_BOMB = ITEMS.register("timed_noise_maker_bomb",
            () -> new TimedNoiseMakerBombItem(new Item.Properties()));

    public static final RegistryObject<Item> ABRAHAM_HEAD = ITEMS.register("abraham_head", () ->
            new SurvivorHeadItem(SASBlocks.ABRAHAM_HEAD.get(), SASBlocks.ABRAHAM_WALL_HEAD.get(),
                    (new Item.Properties()).rarity(Rarity.UNCOMMON), Direction.DOWN));

    public static final RegistryObject<Item> BEN_HEAD = ITEMS.register("ben_head", () ->
            new SurvivorHeadItem(SASBlocks.BEN_HEAD.get(), SASBlocks.BEN_WALL_HEAD.get(),
                    (new Item.Properties()).rarity(Rarity.UNCOMMON), Direction.DOWN));

    public static final RegistryObject<Item> GEORGIA_HEAD = ITEMS.register("georgia_head", () ->
            new SurvivorHeadItem(SASBlocks.GEORGIA_HEAD.get(), SASBlocks.GEORGIA_WALL_HEAD.get(),
                    (new Item.Properties()).rarity(Rarity.UNCOMMON), Direction.DOWN));

    public static final RegistryObject<Item> JESSE_HEAD = ITEMS.register("jesse_head", () ->
            new SurvivorHeadItem(SASBlocks.JESSE_HEAD.get(), SASBlocks.JESSE_WALL_HEAD.get(),
                    (new Item.Properties()).rarity(Rarity.UNCOMMON), Direction.DOWN));

    public static final RegistryObject<Item> JOE_HEAD = ITEMS.register("joe_head", () ->
            new SurvivorHeadItem(SASBlocks.JOE_HEAD.get(), SASBlocks.JOE_WALL_HEAD.get(),
                    (new Item.Properties()).rarity(Rarity.UNCOMMON), Direction.DOWN));

    public static final RegistryObject<Item> MISSY_HEAD = ITEMS.register("missy_head", () ->
            new SurvivorHeadItem(SASBlocks.MISSY_HEAD.get(), SASBlocks.MISSY_WALL_HEAD.get(),
                    (new Item.Properties()).rarity(Rarity.UNCOMMON), Direction.DOWN));

    public static final RegistryObject<Item> OSAMA_HEAD = ITEMS.register("osama_head", () ->
            new SurvivorHeadItem(SASBlocks.OSAMA_HEAD.get(), SASBlocks.OSAMA_WALL_HEAD.get(),
                    (new Item.Properties()).rarity(Rarity.UNCOMMON), Direction.DOWN));

    public static final RegistryObject<Item> PATRICK_HEAD = ITEMS.register("patrick_head", () ->
            new SurvivorHeadItem(SASBlocks.PATRICK_HEAD.get(), SASBlocks.PATRICK_WALL_HEAD.get(),
                    (new Item.Properties()).rarity(Rarity.UNCOMMON), Direction.DOWN));

    public static final RegistryObject<Item> RANDY_HEAD = ITEMS.register("randy_head", () ->
            new SurvivorHeadItem(SASBlocks.RANDY_HEAD.get(), SASBlocks.RANDY_WALL_HEAD.get(),
                    (new Item.Properties()).rarity(Rarity.UNCOMMON), Direction.DOWN));

    public static final RegistryObject<Item> RICK_HEAD = ITEMS.register("rick_head", () ->
            new SurvivorHeadItem(SASBlocks.RICK_HEAD.get(), SASBlocks.RICK_WALL_HEAD.get(),
                    (new Item.Properties()).rarity(Rarity.UNCOMMON), Direction.DOWN));

    public static final RegistryObject<Item> TOM_HEAD = ITEMS.register("tom_head", () ->
            new SurvivorHeadItem(SASBlocks.TOM_HEAD.get(), SASBlocks.TOM_WALL_HEAD.get(),
                    (new Item.Properties()).rarity(Rarity.UNCOMMON), Direction.DOWN));

    public static final RegistryObject<Item> WALTER_HEAD = ITEMS.register("walter_head", () ->
            new SurvivorHeadItem(SASBlocks.WALTER_HEAD.get(), SASBlocks.WALTER_WALL_HEAD.get(),
                    (new Item.Properties()).rarity(Rarity.UNCOMMON), Direction.DOWN));

    public static final RegistryObject<Item> CLEAVER = ITEMS.register("cleaver", () ->
            new SwordItem(Tiers.IRON, 3, -2F, new Item.Properties().durability(200)));

    public static final RegistryObject<Item> ESTEEMED_MORTAL = ITEMS.register("esteemed_mortal", () ->
            new EsteemedMortal(Tiers.IRON, 6, -3F, new Item.Properties().durability(1000).rarity(Rarity.RARE)));

    public static final RegistryObject<Item> FOURTH_N_PAIN = ITEMS.register("4th_n_pain", () ->
            new SwordItem(Tiers.IRON, 3, -1.5F, new Item.Properties().durability(1200).rarity(Rarity.UNCOMMON)));

    public static final RegistryObject<Item> THE_BAT = ITEMS.register("the_bat", () ->
            new TheBat(Tiers.IRON, 4, -2.6F, new Item.Properties().durability(500).rarity(Rarity.EPIC)));

    public static final RegistryObject<Item> NAIL_BAT = ITEMS.register("nail_bat", () ->
            new SwordItem(Tiers.IRON, 3, -2.6F, new Item.Properties().durability(250)));

    public static final RegistryObject<Item> HATCHET = ITEMS.register("hatchet", () ->
            new SwordItem(Tiers.IRON, 6, -3.1F, new Item.Properties().durability(350)));

    public static final RegistryObject<Item> SAMEDIS_HAND = ITEMS.register("samedis_hand", () ->
            new SamedisHand(Tiers.IRON, 3, -2.4F, new Item.Properties().durability(600)));

    public static final RegistryObject<Item> NATIONAL_GUARD_KNIFE = ITEMS.register("national_guard_knife", () ->
            new SwordItem(Tiers.IRON, 3, -2.2F, new Item.Properties().durability(500)));

    public static final RegistryObject<NinjaStarItem> NINJA_STAR = ITEMS.register("ninja_star",
            () -> new NinjaStarItem(new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

}
