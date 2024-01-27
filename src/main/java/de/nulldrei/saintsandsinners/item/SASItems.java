package de.nulldrei.saintsandsinners.item;

import de.nulldrei.saintsandsinners.SaintsAndSinners;
import de.nulldrei.saintsandsinners.item.combat.Bottle;
import net.minecraft.world.item.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SASItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, SaintsAndSinners.MODID);

    public static final RegistryObject<Item> BOTTLE = ITEMS.register("bottle", () -> new Bottle(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> SHIV = ITEMS.register("shiv", () -> new SwordItem(Tiers.IRON, 2, -1.5F, new Item.Properties().durability(100)));
    public static final RegistryObject<Item> BROKENBOTTLE = ITEMS.register("broken_bottle", () -> new SwordItem(Tiers.STONE, 3, -0.5F, new Item.Properties().durability(15)));
    public static final RegistryObject<Item> SCREWDRIVER = ITEMS.register("screwdriver", () -> new SwordItem(Tiers.STONE, 2, -0.5F, new Item.Properties().durability(150)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

}
