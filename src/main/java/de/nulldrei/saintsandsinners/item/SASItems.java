package de.nulldrei.saintsandsinners.item;

import de.nulldrei.saintsandsinners.SaintsAndSinners;
import de.nulldrei.saintsandsinners.armor.material.MaterialReclaimed;
import de.nulldrei.saintsandsinners.armor.material.MaterialRottenFlesh;
import de.nulldrei.saintsandsinners.armor.material.MaterialTower;
import de.nulldrei.saintsandsinners.data.SASLootTables;
import de.nulldrei.saintsandsinners.entity.SASEntities;
import de.nulldrei.saintsandsinners.item.armor.ReclaimedArmor;
import de.nulldrei.saintsandsinners.item.armor.RottenFleshArmor;
import de.nulldrei.saintsandsinners.item.combat.*;
import de.nulldrei.saintsandsinners.item.stuff.BoxOfStuff;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.projectile.Arrow;
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

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

}
