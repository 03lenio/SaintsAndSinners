package de.nulldrei.saintsandsinners.block;

import de.nulldrei.saintsandsinners.SaintsAndSinners;
import de.nulldrei.saintsandsinners.block.utility.RecyclingBinBlock;
import de.nulldrei.saintsandsinners.item.SASItems;
import de.nulldrei.saintsandsinners.item.combat.Bottle;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

import static de.nulldrei.saintsandsinners.SaintsAndSinners.BLOCKS;

public class SASBlocks {

    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, SaintsAndSinners.MODID);

    public static final RegistryObject<Block> RECYCLINGBIN = registerBlock("recycling_bin",
                () -> new RecyclingBinBlock(BlockBehaviour.Properties.copy(Blocks.CAULDRON).sound(SoundType.STONE)));

        private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
            RegistryObject<T> toReturn = BLOCKS.register(name, block);
            registerBlockItem(name, toReturn);
            return toReturn;
        }

        private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block) {
            return SASItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
        }


        public static void register(IEventBus eventBus) {
            BLOCKS.register(eventBus);
        }


}
