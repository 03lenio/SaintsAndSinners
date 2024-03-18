package de.nulldrei.saintsandsinners.block;

import de.nulldrei.saintsandsinners.SaintsAndSinners;
import de.nulldrei.saintsandsinners.block.skull.SASSkullBlock;
import de.nulldrei.saintsandsinners.block.skull.SASWallSkullBlock;
import de.nulldrei.saintsandsinners.block.utility.RecyclingBinBlock;
import de.nulldrei.saintsandsinners.item.SASItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CauldronBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class SASBlocks {

    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, SaintsAndSinners.MODID);
    public static final RegistryObject<Block> RECYCLING_BIN = registerBlock("recycling_bin",
                () -> new RecyclingBinBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).strength(1.0F)));

    public static final RegistryObject<Block> ABRAHAM_HEAD = BLOCKS.register("abraham_head",
            () -> new SASSkullBlock(SASSkullBlock.Types.ABRAHAM, BlockBehaviour.Properties.of().strength(1.0F).pushReaction(PushReaction.DESTROY)));
    public static final RegistryObject<Block> ABRAHAM_WALL_HEAD = BLOCKS.register("abraham_wall_head",
            () -> new SASWallSkullBlock(SASSkullBlock.Types.ABRAHAM, BlockBehaviour.Properties.of().strength(1.0F)
                    .lootFrom(ABRAHAM_HEAD).pushReaction(PushReaction.DESTROY)));

    public static final RegistryObject<Block> BEN_HEAD = BLOCKS.register("ben_head",
            () -> new SASSkullBlock(SASSkullBlock.Types.BEN, BlockBehaviour.Properties.of().strength(1.0F).pushReaction(PushReaction.DESTROY)));
    public static final RegistryObject<Block> BEN_WALL_HEAD = BLOCKS.register("ben_wall_head",
            () -> new SASWallSkullBlock(SASSkullBlock.Types.BEN, BlockBehaviour.Properties.of().strength(1.0F)
                    .lootFrom(BEN_HEAD).pushReaction(PushReaction.DESTROY)));

    public static final RegistryObject<Block> GEORGIA_HEAD = BLOCKS.register("georgia_head",
            () -> new SASSkullBlock(SASSkullBlock.Types.GEORGIA, BlockBehaviour.Properties.of().strength(1.0F).pushReaction(PushReaction.DESTROY)));
    public static final RegistryObject<Block> GEORGIA_WALL_HEAD = BLOCKS.register("georgia_wall_head",
            () -> new SASWallSkullBlock(SASSkullBlock.Types.GEORGIA, BlockBehaviour.Properties.of().strength(1.0F)
                    .lootFrom(GEORGIA_HEAD).pushReaction(PushReaction.DESTROY)));

    public static final RegistryObject<Block> JESSE_HEAD = BLOCKS.register("jesse_head",
            () -> new SASSkullBlock(SASSkullBlock.Types.JESSE, BlockBehaviour.Properties.of().strength(1.0F).pushReaction(PushReaction.DESTROY)));
    public static final RegistryObject<Block> JESSE_WALL_HEAD = BLOCKS.register("jesse_wall_head",
            () -> new SASWallSkullBlock(SASSkullBlock.Types.JESSE, BlockBehaviour.Properties.of().strength(1.0F)
                    .lootFrom(JESSE_HEAD).pushReaction(PushReaction.DESTROY)));

    public static final RegistryObject<Block> JOE_HEAD = BLOCKS.register("joe_head",
            () -> new SASSkullBlock(SASSkullBlock.Types.JOE, BlockBehaviour.Properties.of().strength(1.0F).pushReaction(PushReaction.DESTROY)));
    public static final RegistryObject<Block> JOE_WALL_HEAD = BLOCKS.register("joe_wall_head",
            () -> new SASWallSkullBlock(SASSkullBlock.Types.JOE, BlockBehaviour.Properties.of().strength(1.0F)
                    .lootFrom(JOE_HEAD).pushReaction(PushReaction.DESTROY)));

    public static final RegistryObject<Block> MISSY_HEAD = BLOCKS.register("missy_head",
            () -> new SASSkullBlock(SASSkullBlock.Types.MISSY, BlockBehaviour.Properties.of().strength(1.0F).pushReaction(PushReaction.DESTROY)));
    public static final RegistryObject<Block> MISSY_WALL_HEAD = BLOCKS.register("missy_wall_head",
            () -> new SASWallSkullBlock(SASSkullBlock.Types.MISSY, BlockBehaviour.Properties.of().strength(1.0F)
                    .lootFrom(MISSY_HEAD).pushReaction(PushReaction.DESTROY)));

    public static final RegistryObject<Block> OSAMA_HEAD = BLOCKS.register("osama_head",
            () -> new SASSkullBlock(SASSkullBlock.Types.OSAMA, BlockBehaviour.Properties.of().strength(1.0F).pushReaction(PushReaction.DESTROY)));
    public static final RegistryObject<Block> OSAMA_WALL_HEAD = BLOCKS.register("osama_wall_head",
            () -> new SASWallSkullBlock(SASSkullBlock.Types.OSAMA, BlockBehaviour.Properties.of().strength(1.0F)
                    .lootFrom(OSAMA_HEAD).pushReaction(PushReaction.DESTROY)));

    public static final RegistryObject<Block> PATRICK_HEAD = BLOCKS.register("patrick_head",
            () -> new SASSkullBlock(SASSkullBlock.Types.PATRICK, BlockBehaviour.Properties.of().strength(1.0F).pushReaction(PushReaction.DESTROY)));
    public static final RegistryObject<Block> PATRICK_WALL_HEAD = BLOCKS.register("patrick_wall_head",
            () -> new SASWallSkullBlock(SASSkullBlock.Types.PATRICK, BlockBehaviour.Properties.of().strength(1.0F)
                    .lootFrom(PATRICK_HEAD).pushReaction(PushReaction.DESTROY)));

    public static final RegistryObject<Block> RANDY_HEAD = BLOCKS.register("randy_head",
            () -> new SASSkullBlock(SASSkullBlock.Types.RANDY, BlockBehaviour.Properties.of().strength(1.0F).pushReaction(PushReaction.DESTROY)));
    public static final RegistryObject<Block> RANDY_WALL_HEAD = BLOCKS.register("randy_wall_head",
            () -> new SASWallSkullBlock(SASSkullBlock.Types.RANDY, BlockBehaviour.Properties.of().strength(1.0F)
                    .lootFrom(RANDY_HEAD).pushReaction(PushReaction.DESTROY)));

    public static final RegistryObject<Block> RICK_HEAD = BLOCKS.register("rick_head",
            () -> new SASSkullBlock(SASSkullBlock.Types.RICK, BlockBehaviour.Properties.of().strength(1.0F).pushReaction(PushReaction.DESTROY)));
    public static final RegistryObject<Block> RICK_WALL_HEAD = BLOCKS.register("rick_wall_head",
            () -> new SASWallSkullBlock(SASSkullBlock.Types.RICK, BlockBehaviour.Properties.of().strength(1.0F)
                    .lootFrom(RICK_HEAD).pushReaction(PushReaction.DESTROY)));

    public static final RegistryObject<Block> TOM_HEAD = BLOCKS.register("tom_head",
            () -> new SASSkullBlock(SASSkullBlock.Types.TOM, BlockBehaviour.Properties.of().strength(1.0F).pushReaction(PushReaction.DESTROY)));
    public static final RegistryObject<Block> TOM_WALL_HEAD = BLOCKS.register("tom_wall_head",
            () -> new SASWallSkullBlock(SASSkullBlock.Types.TOM, BlockBehaviour.Properties.of().strength(1.0F)
                    .lootFrom(TOM_HEAD).pushReaction(PushReaction.DESTROY)));

    public static final RegistryObject<Block> WALTER_HEAD = BLOCKS.register("walter_head",
            () -> new SASSkullBlock(SASSkullBlock.Types.WALTER, BlockBehaviour.Properties.of().strength(1.0F).pushReaction(PushReaction.DESTROY)));
    public static final RegistryObject<Block> WALTER_WALL_HEAD = BLOCKS.register("walter_wall_head",
            () -> new SASWallSkullBlock(SASSkullBlock.Types.WALTER, BlockBehaviour.Properties.of().strength(1.0F)
                    .lootFrom(WALTER_HEAD).pushReaction(PushReaction.DESTROY)));

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
