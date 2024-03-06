package de.nulldrei.saintsandsinners.data;

import de.nulldrei.saintsandsinners.SaintsAndSinners;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import static de.nulldrei.saintsandsinners.data.SASTags.Items.tag;

public class SASTags {

    public static final TagKey<Item> SURVIVOR_HEADS = tag("survivor_heads");
    public static final TagKey<Item> DECAPITATING_WEAPONS = tag("decapitating_weapons");

    protected static class Items {
        static TagKey<Item> tag(String name) {
            return ItemTags.create(new ResourceLocation(SaintsAndSinners.MODID, name));
        }
    }

}
